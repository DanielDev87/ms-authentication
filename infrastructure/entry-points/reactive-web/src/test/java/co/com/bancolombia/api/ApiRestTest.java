package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.api.handler.Handler;
import co.com.bancolombia.api.handler.UserTransactionalUseCase;
import co.com.bancolombia.model.user.User;

import co.com.bancolombia.usecase.findbydocumentnumber.FindByDocumentNumberUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@WebFluxTest
@Import({ApiRest.class, Handler.class})
class ApiRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserTransactionalUseCase userTransactionalUseCase; // Mock del wrapper transaccional
    @MockBean
    private FindByDocumentNumberUseCase findByDocumentNumberUseCase;

    @Test
    void createUserShouldReturnCreated() {
        // Arrange
        UserDTO userToRegister = new UserDTO();
        userToRegister.setEmail("test@example.com");

        User savedUser = User.builder().id(1L).build();

        // Simula el comportamiento del wrapper transaccional
        when(userTransactionalUseCase.createUser(any(User.class))).thenReturn(Mono.just(savedUser));

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToRegister)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
    }

    @Test
    void getUserByDocumentNumberShouldReturnUser() {
        // Arrange
        String documentNumber = "12345";
        User foundUser = User.builder()
                .id(1L)
                .documentNumber(documentNumber)
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .role(User.Role.CLIENT)
                .build();

        // Simula el comportamiento del caso de uso de búsqueda
        when(findByDocumentNumberUseCase.execute(anyString())).thenReturn(Mono.just(foundUser));

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/users/document/{doc}", documentNumber)
                .exchange()
                .expectStatus().isOk() // Verifica que el estado sea 200 OK
                .expectBody(UserDTO.class)
                .value(userResponse -> {
                    // Valida que los datos en el DTO de respuesta sean los correctos
                    assert userResponse.getId().equals(1L);
                    assert userResponse.getDocumentNumber().equals(documentNumber);
                    assert userResponse.getEmail().equals("test@example.com");
                });
    }
}