package co.com.bancolombia.usecase.login;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.security.gateways.JwtProvider;
import co.com.bancolombia.usecase.security.gateways.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.LocalDateTime;
import static co.com.bancolombia.usecase.login.LoginUseCaseConstants.*;

@RequiredArgsConstructor
public class LoginUseCase {
    private static final Logger logger = LoggerFactory.getLogger(LoginUseCase.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Mono<String> execute(String email, String password) {
        logger.info("LOGIN-DEBUG: Iniciando flujo de login para {}", email);

        return userRepository.findByEmail(email)
                .doOnSubscribe(s -> logger.info("LOGIN-DEBUG: Suscribiendo a userRepository.findByEmail para {}", email))
                .doOnSuccess(user -> {
                    if (user != null) {
                        logger.info("LOGIN-DEBUG: Éxito en findByEmail. Usuario encontrado: {}", email);
                    } else {
                        // Este caso es manejado por switchIfEmpty, pero lo logueamos por si acaso.
                        logger.warn("LOGIN-DEBUG: Éxito en findByEmail, pero el resultado es nulo para: {}", email);
                    }
                })
                .doOnError(error -> logger.error("LOGIN-DEBUG: Error durante userRepository.findByEmail para {}", email, error))
                .switchIfEmpty(Mono.error(new BusinessException(ERROR_INVALID_CREDENTIALS)))
                .publishOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> logger.info("LOGIN-FIX-V2: La operación se movió a un hilo BoundedElastic."))
                .flatMap(user -> {
                    logger.info("LOGIN-DEBUG: Dentro de flatMap, verificando contraseña para {}", user.getEmail());

                    if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
                        logger.warn("LOGIN-DEBUG: Cuenta bloqueada para {}", user.getEmail());
                        return Mono.error(new BusinessException(ERROR_ACCOUNT_LOCKED));
                    }

                    if (passwordEncoder.matches(password, user.getPassword())) {
                        logger.info("LOGIN-DEBUG: Contraseña correcta. Actualizando e iniciando guardado para {}", user.getEmail());
                        user.setFailedLoginAttempts(0);
                        user.setAccountLockedUntil(null);
                        return userRepository.save(user)
                                .doOnSubscribe(s -> logger.info("LOGIN-DEBUG: Suscribiendo a userRepository.save (login exitoso) para {}", user.getEmail()))
                                .doOnSuccess(savedUser -> logger.info("LOGIN-DEBUG: Éxito en userRepository.save (login exitoso) para {}", savedUser.getEmail()))
                                .doOnError(error -> logger.error("LOGIN-DEBUG: Error durante userRepository.save (login exitoso)", error))
                                .map(jwtProvider::generateToken);
                    } else {
                        logger.warn("LOGIN-DEBUG: Contraseña incorrecta para {}", user.getEmail());
                        return handleFailedLoginAttempt(user);
                    }
                });
    }

    private Mono<String> handleFailedLoginAttempt(User user) {
        logger.info("LOGIN-DEBUG: Manejando intento de login fallido para {}", user.getEmail());
        int attempts = user.getFailedLoginAttempts() == null ? 1 : user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            logger.warn("LOGIN-DEBUG: Bloqueando cuenta por {} minutos para {}", LOCK_DURATION_MINUTES, user.getEmail());
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
        }

        return userRepository.save(user)
                .doOnSubscribe(s -> logger.info("LOGIN-DEBUG: Suscribiendo a userRepository.save (login fallido) para {}", user.getEmail()))
                .doOnSuccess(savedUser -> logger.info("LOGIN-DEBUG: Éxito en userRepository.save (login fallido) para {}", savedUser.getEmail()))
                .doOnError(error -> logger.error("LOGIN-DEBUG: Error durante userRepository.save (login fallido)", error))
                .then(Mono.error(new BusinessException(ERROR_INVALID_CREDENTIALS)));
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}