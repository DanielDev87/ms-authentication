package co.com.bancolombia.api.handler;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserTransactionalUseCase {

    private final CreateUserUseCase createUserUseCase;
    private final TransactionalOperator transactionalOperator;

    public Mono<User> createUser(User user) {
        return createUserUseCase.execute(user)
                .as(transactionalOperator::transactional);
    }
}
