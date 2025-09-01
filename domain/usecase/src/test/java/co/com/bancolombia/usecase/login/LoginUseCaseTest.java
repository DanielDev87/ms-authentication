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
                .build();
    }

    @Test
    void shouldLoginSuccessfullyAndReturnToken() {
        // Arrange
        when(userRepository.findByEmail("test@domain.com")).thenReturn(Mono.just(sampleUser));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);
        when(jwtProvider.generateToken(sampleUser)).thenReturn("dummy.jwt.token");

        // Act & Assert
        StepVerifier.create(loginUseCase.execute("test@domain.com", "plainPassword"))
                .expectNext("dummy.jwt.token")
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenPasswordIsIncorrect() {
        // Arrange
        when(userRepository.findByEmail("test@domain.com")).thenReturn(Mono.just(sampleUser));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        // Act & Assert
        StepVerifier.create(loginUseCase.execute("test@domain.com", "wrongPassword"))
                .expectError(LoginUseCase.BusinessException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("notfound@domain.com")).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(loginUseCase.execute("notfound@domain.com", "anyPassword"))
                .expectError(LoginUseCase.BusinessException.class)
                .verify();
    }
}
