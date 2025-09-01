package co.com.bancolombia.usecase.login;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.security.gateways.JwtProvider;
import co.com.bancolombia.usecase.security.gateways.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import static co.com.bancolombia.usecase.login.LoginUseCaseConstants.*;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Mono<String> execute(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new BusinessException(ERROR_INVALID_CREDENTIALS)))
                .flatMap(user -> {
                    if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
                        return Mono.error(new BusinessException(ERROR_ACCOUNT_LOCKED));
                    }

                    if (passwordEncoder.matches(password, user.getPassword())) {
                        user.setFailedLoginAttempts(0);
                        user.setAccountLockedUntil(null);
                        return userRepository.save(user).map(jwtProvider::generateToken);
                    } else {
                        return handleFailedLoginAttempt(user);
                    }
                });
    }

    private Mono<String> handleFailedLoginAttempt(User user) {
        int attempts = user.getFailedLoginAttempts() == null ? 1 : user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
        }

        return userRepository.save(user)
                .then(Mono.error(new BusinessException(ERROR_INVALID_CREDENTIALS)));
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}