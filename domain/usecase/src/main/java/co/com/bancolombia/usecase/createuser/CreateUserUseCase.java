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
                .flatMap(existingUser -> {
                    logger.warn("El email {} ya está registrado.", user.getEmail());
                    return Mono.error(new BusinessException("El correo electrónico ya está en uso."));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Email disponible, ahora verificamos el número de documento
                    logger.info("Email {} disponible. Verificando número de documento...", user.getEmail());
                    return userRepository.findByDocumentNumber(user.getDocumentNumber())
                            .flatMap(existingUserByDoc -> {
                                logger.warn("El número de documento {} ya está registrado.", user.getDocumentNumber());
                                return Mono.error(new BusinessException("El número de documento ya está en uso."));
                            })
                            // Si el documento también está disponible, procedemos a crear el usuario
                            .switchIfEmpty(Mono.defer(() -> {
                                logger.info("Documento disponible. Encriptando contraseña para el usuario: {}", user.getEmail());
                                return passwordEncryptionGateway.encode(user.getPassword())
                                        .flatMap(hashedPassword -> {
                                            user.setPassword(hashedPassword);
                                            logger.info("Contraseña encriptada. Guardando usuario: {}", user.getEmail());
                                            return userRepository.save(user);
                                        });
                            }));
                }))
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