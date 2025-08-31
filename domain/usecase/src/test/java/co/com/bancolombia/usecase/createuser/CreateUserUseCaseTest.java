package co.com.bancolombia.usecase.createuser;

import co.com.bancolombia.model.log.gateways.LoggerService;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.PasswordEncryptionGateway;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncryptionGateway passwordEncryptionGateway;
    @Mock
    private LoggerService logger;

    private CreateUserUseCase createUserUseCase;
    private User userToCreate;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCase(userRepository, passwordEncryptionGateway, logger);
        userToCreate = User.builder()
                .documentNumber("123456")
                .email("new@test.com")
                .password("plainPassword123")
                .role(User.Role.APPLICANT)
                .build();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        String hashedPassword = "a-very-secure-hashed-password";
        User savedUser = userToCreate.toBuilder().id(1L).password(hashedPassword).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.findByDocumentNumber(anyString())).thenReturn(Mono.empty());
        when(passwordEncryptionGateway.encode(anyString())).thenReturn(Mono.just(hashedPassword));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        // Act
        Mono<User> result = createUserUseCase.execute(userToCreate);

        // Assert
        StepVerifier.create(result).expectNextCount(1).verifyComplete();
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        // Arrange
        User existingUser = User.builder().id(2L).email("new@test.com").build();
        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Mono.just(existingUser));
        when(userRepository.findByDocumentNumber(anyString())).thenReturn(Mono.empty());

        // --- ESTA ES LA LÍNEA QUE FALTABA ---
        // Se añade esta simulación para evitar el NullPointerException en la construcción del flujo reactivo.
        when(passwordEncryptionGateway.encode(anyString())).thenReturn(Mono.just("some-fake-hash"));

        // Act
        Mono<User> result = createUserUseCase.execute(userToCreate);

        // Assert
        StepVerifier.create(result)
                .expectError(CreateUserUseCase.BusinessException.class)
                .verify();
    }

    @Test
    void shouldFailWhenDocumentNumberAlreadyExists() {
        // Arrange
        User existingUser = User.builder().id(3L).documentNumber("123456").build();
        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Mono.empty());
        when(userRepository.findByDocumentNumber(userToCreate.getDocumentNumber())).thenReturn(Mono.just(existingUser));
        when(passwordEncryptionGateway.encode(anyString())).thenReturn(Mono.just("some-fake-hash"));

        // Act
        Mono<User> result = createUserUseCase.execute(userToCreate);

        // Assert
        StepVerifier.create(result)
                .expectError(CreateUserUseCase.BusinessException.class)
                .verify();
    }
}