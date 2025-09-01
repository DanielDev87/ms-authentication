package co.com.bancolombia.config;

import co.com.bancolombia.api.handler.UserTransactionalUseCase;
import co.com.bancolombia.model.log.gateways.LoggerService;
import co.com.bancolombia.model.user.gateways.PasswordEncryptionGateway;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import co.com.bancolombia.usecase.findbydocumentnumber.FindByDocumentNumberUseCase;
import co.com.bancolombia.usecase.login.LoginUseCase;
import co.com.bancolombia.usecase.security.gateways.JwtProvider;
import co.com.bancolombia.usecase.security.gateways.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
public class UseCasesConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository,PasswordEncryptionGateway passwordEncryptionGateway, LoggerService loggerService) {
        return new CreateUserUseCase(userRepository, passwordEncryptionGateway, loggerService);
    }

    @Bean
    public FindByDocumentNumberUseCase findByDocumentNumberUseCase(UserRepository userRepository) {
        return new FindByDocumentNumberUseCase(userRepository);
    }

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        return new LoginUseCase(userRepository, passwordEncoder, jwtProvider);
    }

    @Bean
    public UserTransactionalUseCase userTransactionalUseCase(
            CreateUserUseCase createUserUseCase,
            TransactionalOperator transactionalOperator
    ) {
        return new UserTransactionalUseCase(createUserUseCase, transactionalOperator);
    }
}