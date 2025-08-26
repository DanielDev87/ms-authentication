package co.com.bancolombia.config;

import co.com.bancolombia.model.log.gateways.LoggerService;
import co.com.bancolombia.model.user.gateways.PasswordEncryptionGateway;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository,PasswordEncryptionGateway passwordEncryptionGateway, LoggerService loggerService) {
        return new CreateUserUseCase(userRepository, passwordEncryptionGateway, loggerService);
    }
}