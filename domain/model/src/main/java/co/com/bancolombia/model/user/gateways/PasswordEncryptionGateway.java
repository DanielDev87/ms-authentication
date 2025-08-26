package co.com.bancolombia.model.user.gateways;

import reactor.core.publisher.Mono;

public interface PasswordEncryptionGateway {
    Mono<String> encode(String rawPassword);
}