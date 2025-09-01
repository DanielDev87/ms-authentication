package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.LoginDTO;
import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import co.com.bancolombia.usecase.login.LoginUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HandlerTest {

    // Mockeamos las dependencias que el Handler necesita
    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private LoginUseCase loginUseCase;
    @Mock
    private ServerRequest serverRequest; // Mockeamos la petición entrante

    // Inyectamos los mocks en nuestra clase Handler
    @InjectMocks
    private Handler handler;

    @BeforeEach
    void setUp() {
        // Inicializamos los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange (Organizar)
        UserDTO userDTO = new UserDTO(); // Asigna valores si es necesario
        User userModel = User.builder().build();

        when(serverRequest.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));
        when(createUserUseCase.execute(any(User.class))).thenReturn(Mono.just(userModel));

        // Act & Assert (Actuar y Afirmar)
        StepVerifier.create(handler.createUser(serverRequest))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();
    }

    @Test
    void shouldLoginAndReturnOkStatus() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@domain.com");
        loginDTO.setPassword("password");
        String dummyToken = "dummy.jwt.token";

        when(serverRequest.bodyToMono(LoginDTO.class)).thenReturn(Mono.just(loginDTO));
        when(loginUseCase.execute(loginDTO.getEmail(), loginDTO.getPassword())).thenReturn(Mono.just(dummyToken));

        // Act & Assert
        StepVerifier.create(handler.login(serverRequest))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    void shouldFailLoginAndReturnUnauthorizedStatus() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@domain.com");
        loginDTO.setPassword("wrong-password");

        when(serverRequest.bodyToMono(LoginDTO.class)).thenReturn(Mono.just(loginDTO));
        when(loginUseCase.execute(loginDTO.getEmail(), loginDTO.getPassword()))
                .thenReturn(Mono.error(new LoginUseCase.BusinessException("Credenciales inválidas")));

        // Act & Assert
        StepVerifier.create(handler.login(serverRequest))
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.UNAUTHORIZED))
                .verifyComplete();
    }
}