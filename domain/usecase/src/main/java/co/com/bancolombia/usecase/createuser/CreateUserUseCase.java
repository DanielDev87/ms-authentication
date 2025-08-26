package co.com.bancolombia.usecase.createuser;

import co.com.bancolombia.model.log.gateways.LoggerService;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.PasswordEncryptionGateway;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncryptionGateway passwordEncryptionGateway;
    private final LoggerService logger;

    public Mono<User> execute(User user) {
        logger.info("Iniciando creación para usuario con email: {}", user.getEmail());

        return userRepository.findByEmail(user.getEmail())
                // Si el Mono emite un usuario, significa que el email ya existe.
                .flatMap(existingUser -> {
                    logger.warn("El email {} ya está registrado.", user.getEmail());
                    return Mono.error(new BusinessException("El correo electrónico ya está en uso."));
                })
                // Si el Mono está vacío (switchIfEmpty), el email no existe y procedemos.
                .switchIfEmpty(Mono.defer(() -> {
                    logger.info("Email disponible. Encriptando contraseña para el usuario: {}", user.getEmail());
                    return passwordEncryptionGateway.encode(user.getPassword())
                            .flatMap(hashedPassword -> {
                                user.setPassword(hashedPassword);
                                logger.info("Contraseña encriptada. Guardando usuario: {}", user.getEmail());
                                return userRepository.save(user);
                            });
                }))
                // Limpiamos la contraseña antes de devolver el objeto por seguridad.
                .ofType(User.class)
                .map(savedUser -> {
                    logger.info("Usuario {} guardado exitosamente.", savedUser.getEmail());
                    return savedUser.toBuilder().password(null).build();
                });
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}