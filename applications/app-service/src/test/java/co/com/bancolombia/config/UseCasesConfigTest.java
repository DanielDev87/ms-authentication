package co.com.bancolombia.config;

import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.createuser.UserRegisterUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = UseCasesConfig.class) // 1. Carga solo tu configuración de Casos de Uso
class UseCasesConfigTest {

    @Autowired // 2. Pide a Spring que inyecte el bean que queremos probar
    private UserRegisterUseCase userRegisterUseCase;

    @MockBean // 3. Provee un mock para la dependencia que necesita el UseCase
    private UserRepository<U, U1, Number, ReactiveCrudRepository> userRepository;

    @Test
    void userRegisterUseCaseBeanShouldBeCreated() {
        // 4. La prueba es simple: si el bean se inyectó, no será nulo.
        assertNotNull(userRegisterUseCase, "El bean UserRegisterUseCase no se creó correctamente.");
    }
}