package co.com.bancolombia.usecase.createuser;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository<U, U1, Number, org.springframework.data.repository.reactive.ReactiveCrudRepository> userRepository;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void createUserSuccessfully() {
        // Arrange
        User userToCreate = User.builder().email("new@test.com").password("pass").build();
        User userSaved = userToCreate.toBuilder().id(1L).build();

        // 1. Simula que el email NO existe
        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Mono.empty());
        // 2. Simula la operación de guardado
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(userSaved));

        // Act & Assert
        StepVerifier.create(createUserUseCase.execute(userToCreate))
                .expectNext(userSaved)
                .verifyComplete();
    }

    @Test
    void createUserFailsWhenEmailAlreadyExists() {
        // Arrange
        User existingUser = User.builder().id(1L).email("existing@test.com").password("pass").build();
        User userToCreate = User.builder().email("existing@test.com").password("pass").build();

        // 1. Simula que el email SÍ existe
        when(userRepository.findByEmail(userToCreate.getEmail())).thenReturn(Mono.just(existingUser));

        // Act & Assert
        StepVerifier.create(createUserUseCase.execute(userToCreate))
                .expectError(CreateUserUseCase.BusinessException.class)
                .verify();
    }
}