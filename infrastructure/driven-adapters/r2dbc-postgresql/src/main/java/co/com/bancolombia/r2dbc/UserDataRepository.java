package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.data.UserData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface UserDataRepository extends ReactiveCrudRepository<UserData, Long> {
    Mono<UserData> findByEmail(String email);
}