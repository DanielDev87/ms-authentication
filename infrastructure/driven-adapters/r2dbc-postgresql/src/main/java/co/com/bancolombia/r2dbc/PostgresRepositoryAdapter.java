package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.data.UserData;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PostgresRepositoryAdapter implements UserRepository {

    private final UserDataRepository repository;
    private final ObjectMapper mapper;

    @Override
    public Mono<User> save(User user) {
        UserData userData = mapper.convertValue(user, UserData.class);
        return repository.save(userData)
                .map(this::toModel);
    }

    @Override
    public Mono<User> findById(Long id) {
        return null;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toModel);
    }

    @Override
    public Mono<User> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber)
                .map(this::toModel);
    }

    @Override
    public Flux<User> findAll() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return null;
    }

    private User toModel(UserData userData) {
        return mapper.convertValue(userData, User.class);
    }
}