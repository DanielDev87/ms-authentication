package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.model.user.User;
// Importa el caso de uso correcto
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ApiRest.class)
// Importamos la configuración para que Spring sepa qué es un UseCase
@Import(TestApplication.class)
class ApiRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    // Usa el nombre del caso de uso correcto
    private CreateUserUseCase createUserUseCase;

    @Test
    void registerUserShouldReturnCreated() {
        // Arrange
        // La petición ahora usa un DTO
        UserDTO userToRegister = new UserDTO();
        userToRegister.setFirstName("Test");
        userToRegister.setLastName("User");
        userToRegister.setEmail("test.user@example.com");
        userToRegister.setPassword("pass");
        userToRegister.setRole("APPLICANT");
        userToRegister.setBaseSalary(new BigDecimal("1000"));
        userToRegister.setBirthDate(LocalDate.now());

        User userSaved = User.builder().id(1L).email("test.user@example.com").build();

        // Configura el mock para que use el caso de uso correcto
        when(createUserUseCase.execute(any(User.class))).thenReturn(Mono.just(userSaved));

        // Act & Assert
        webTestClient.post().uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userToRegister)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .isEqualTo(userSaved);
    }
}