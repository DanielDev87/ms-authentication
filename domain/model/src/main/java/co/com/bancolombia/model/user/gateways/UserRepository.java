package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Esta interfaz es PURA. Solo conoce objetos del dominio como 'User'.
// No sabe nada de Spring, ni de UserData, ni de la base de datos.
public interface UserRepository {

    Mono<User> save(User user);

    Mono<User> findById(Long id);

    Mono<User> findByEmail(String email);

    Flux<User> findAll();

    Mono<Void> deleteById(Long id);
}