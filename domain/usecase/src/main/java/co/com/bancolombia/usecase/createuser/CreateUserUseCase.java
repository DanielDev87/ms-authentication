package co.com.bancolombia.usecase.createuser;

import co.com.bancolombia.model.log.gateways.LoggerService;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final LoggerService logger;

    public Mono<User> execute(User user) {
        logger.info("Ejecutando caso de uso para crear usuario con email: {}", user.getEmail());
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> {
                    logger.warn("Intento de registro con email duplicado: {}", user.getEmail());
                    // Añade la pista de tipo <User> al Mono.error
                    return Mono.<User>error(new BusinessException("El correo electrónico ya se encuentra registrado."));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("Email {} disponible, procediendo a guardar.", user.getEmail());
                    return userRepository.save(user);
                }));
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}