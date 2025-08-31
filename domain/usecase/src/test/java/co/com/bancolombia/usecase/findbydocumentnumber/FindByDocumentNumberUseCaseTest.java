package co.com.bancolombia.usecase.findbydocumentnumber;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindByDocumentNumberUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindByDocumentNumberUseCase useCase;

    @Test
    void shouldReturnUserWhenFound() {
        // Arrange
        String documentNumber = "10203040";
        User expectedUser = User.builder().id(1L).documentNumber(documentNumber).build();

        when(userRepository.findByDocumentNumber(documentNumber)).thenReturn(Mono.just(expectedUser));

        // Act
        Mono<User> result = useCase.execute(documentNumber);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userRepository).findByDocumentNumber(documentNumber);
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        // Arrange
        String documentNumber = "999999";
        when(userRepository.findByDocumentNumber(documentNumber)).thenReturn(Mono.empty());

        // Act
        Mono<User> result = useCase.execute(documentNumber);

        // Assert
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(userRepository).findByDocumentNumber(documentNumber);
    }
}
