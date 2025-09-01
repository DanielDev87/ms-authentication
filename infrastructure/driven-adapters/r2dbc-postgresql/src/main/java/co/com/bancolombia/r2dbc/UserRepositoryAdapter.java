package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;

import co.com.bancolombia.r2dbc.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserDataRepository repository; // Repositorio de Spring Data
    private final UserMapper mapper;             // El mapper que creamos

    @Override
    public Mono<User> save(User user) {
        // Convierte el modelo de dominio a datos, lo guarda, y lo reconvierte a dominio
        return Mono.just(user)
                .map(mapper::toData)
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findById(Long id) {
        return null;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        // Busca en la BD y convierte el resultado a un modelo de dominio
        return repository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<User> findByDocumentNumber(String documentNumber) {
        return null;
    }

    @Override
    public Flux<User> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return null;
    }
}
