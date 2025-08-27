package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.reactive.TransactionalOperator; // <-- Importar
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ApiRest.class)
@Import(TestApplication.class)
class ApiRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private TransactionalOperator transactionalOperator;

    @Test
    void registerUserShouldReturnCreated() {
        // Arrange
        UserDTO userToRegister = new UserDTO();
        userToRegister.setFirstName("Test");
        userToRegister.setLastName("User");
        userToRegister.setEmail("test.user@example.com");
        userToRegister.setPassword("pass");
        userToRegister.setRole(User.Role.APPLICANT);
        userToRegister.setBaseSalary(new BigDecimal("1000"));
        userToRegister.setBirthDate(LocalDate.now());

        User userSavedInDB = User.builder()
                .id(1L)
                .email("test.user@example.com")
                .firstName("Test")
                .build();

        when(createUserUseCase.execute(any(User.class))).thenReturn(Mono.just(userSavedInDB));
        // Simula el comportamiento del TransactionalOperator
        when(transactionalOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        webTestClient.post().uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToRegister)
                .exchange()
                .expectStatus().isCreated()
                // 2. CORRIGE LA ASERCIÓN: Espera un DTO y valida sus campos
                .expectBody(UserDTO.class)
                .value(userResponse -> {
                    assert userResponse.getId().equals(1L);
                    assert userResponse.getEmail().equals("test.user@example.com");
                    assert userResponse.getPassword() == null;
                });
    }
}