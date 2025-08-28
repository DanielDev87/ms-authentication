package co.com.bancolombia.usecase.findbydocumentnumber;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindByDocumentNumberUseCase {

    private final UserRepository userRepository;

    public Mono<User> execute(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber);
    }
}
