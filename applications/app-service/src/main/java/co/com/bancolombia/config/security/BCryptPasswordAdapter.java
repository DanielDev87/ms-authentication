package co.com.bancolombia.config.security;

import co.com.bancolombia.model.user.gateways.PasswordEncryptionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BCryptPasswordAdapter implements PasswordEncryptionGateway {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> encode(String rawPassword) {
        return Mono.fromSupplier(() -> passwordEncoder.encode(rawPassword));
    }
}