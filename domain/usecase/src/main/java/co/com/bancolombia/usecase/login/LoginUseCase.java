package co.com.bancolombia.usecase.login;

import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.security.gateways.JwtProvider;
import co.com.bancolombia.usecase.security.gateways.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Mono<String> execute(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(jwtProvider.generateToken(user));
                    }
                    return Mono.error(new BusinessException("Credenciales inválidas"));
                })
                .switchIfEmpty(Mono.error(new BusinessException("Credenciales inválidas")));
    }

    // Excepción de negocio para manejar errores de login
    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}