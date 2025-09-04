package co.com.bancolombia.usecase.login;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.security.gateways.JwtProvider;
import co.com.bancolombia.usecase.security.gateways.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static co.com.bancolombia.usecase.login.LoginUseCaseConstants.ERROR_ACCOUNT_LOCKED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = User.builder()
                .email("test@domain.com")
                .password("hashedPassword")
                .role(User.Role.ADMIN)
                .failedLoginAttempts(0)
                .accountLockedUntil(null)
                .build();
    }

    @Test
    void shouldLoginSuccessfullyAndResetAttempts() {
        // Arrange
        sampleUser.setFailedLoginAttempts(2);
        when(userRepository.findByEmail("test@domain.com")).thenReturn(Mono.just(sampleUser));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(jwtProvider.generateToken(any(User.class))).thenReturn("dummy.jwt.token");

        // Act & Assert
        StepVerifier.create(loginUseCase.execute("test@domain.com", "plainPassword"))
                .expectNext("dummy.jwt.token")
                .verifyComplete();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldFailLoginAndIncrementAttempts() {
        // Arrange
        when(userRepository.findByEmail("test@domain.com")).thenReturn(Mono.just(sampleUser));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(sampleUser));

        // Act & Assert
        StepVerifier.create(loginUseCase.execute("test@domain.com", "wrongPassword"))
                .expectError(LoginUseCase.BusinessException.class)
                .verify();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldLockAccountAfterMaxFailedAttempts() {
        // Arrange
        sampleUser.setFailedLoginAttempts(2);
        when(userRepository.findByEmail("test@domain.com")).thenReturn(Mono.just(sampleUser));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act & Assert
        StepVerifier.create(loginUseCase.execute("test@domain.com", "wrongPassword"))
                .expectError(LoginUseCase.BusinessException.class)
                .verify();
    }


    @Test
    void shouldFailLoginIfAccountIsAlreadyLocked() {
        // Arrange
        sampleUser.setAccountLockedUntil(LocalDateTime.now().plusMinutes(15));
        when(userRepository.findByEmail("test@domain.com")).thenReturn(Mono.just(sampleUser));

        // Act & Assert
        StepVerifier.create(loginUseCase.execute("test@domain.com", "anyPassword"))
                .expectErrorMessage(ERROR_ACCOUNT_LOCKED)
                .verify();
    }
}