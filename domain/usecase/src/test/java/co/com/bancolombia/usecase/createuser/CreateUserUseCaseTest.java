package co.com.bancolombia.usecase.createuser;

import co.com.bancolombia.model.log.gateways.LoggerService;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.PasswordEncryptionGateway;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncryptionGateway passwordEncryptionGateway;
    @Mock
    private LoggerService logger;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private User userToCreate;

    @BeforeEach
    void setUp() {
        // Creamos un usuario de prueba con todos los campos requeridos por la HU1
        userToCreate = User.builder()
                .firstName("Daniel")
                .lastName("Agudelo")
                .email("new@test.com")
                .password("plainPassword123")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle Falsa 123")
                .phoneNumber("3001234567")
                .baseSalary(new BigDecimal("5000000"))
                .role(User.Role.APPLICANT)
                .build();
    }

    @Test
    void shouldCreateUserSuccessfullyWhenEmailDoesNotExist() {
        // Arrange
        String hashedPassword = "a-very-secure-hashed-password";
        User userWithHashedPassword = userToCreate.toBuilder().password(hashedPassword).build();
        User savedUser = userWithHashedPassword.toBuilder().id(1L).build();

        // 1. Simula que el email NO existe
        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Mono.empty());
        // 2. Simula la encriptación
        when(passwordEncryptionGateway.encode(userToCreate.getPassword())).thenReturn(Mono.just(hashedPassword));
        // 3. Simula el guardado
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(savedUser));

        // Act
        Mono<User> result = createUserUseCase.execute(userToCreate);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals(1L) && user.getEmail().equals("new@test.com"))
                .verifyComplete();

        verify(userRepository).findByEmail("new@test.com");
        verify(passwordEncryptionGateway).encode("plainPassword123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        // Arrange
        User existingUser = User.builder().id(2L).email("new@test.com").build();

        // 1. Simula que el email SÍ existe
        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Mono.just(existingUser));

        // Act
        Mono<User> result = createUserUseCase.execute(userToCreate);

        // Assert
        StepVerifier.create(result)
                .expectError(CreateUserUseCase.BusinessException.class)
                .verify();

        verify(userRepository).findByEmail("new@test.com");
        // Verifica que la encriptación y el guardado NUNCA fueron llamados
        verify(passwordEncryptionGateway, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}