package co.com.bancolombia.r2dbc.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public abstract class ReactiveAdapterOperations<E, D, I, R extends ReactiveCrudRepository<D, I>> {
    protected R repository;
    protected ObjectMapper mapper;
    private Class<D> dataClass;
    private Function<D, E> toEntityFn;

    protected ReactiveAdapterOperations(R repository, ObjectMapper mapper, Function<D, E> toEntityFn) {
        this.repository = repository;
        this.mapper = mapper;
        this.toEntityFn = toEntityFn;
    }

    public Mono<E> save(E entity) {
        return Mono.just(entity)
                .map(this::toData)
                .flatMap(repository::save)
                .map(toEntityFn);
    }

    protected D toData(E entity) {

        return mapper.convertValue(entity, dataClass);
    }

    public Mono<E> findById(I id) {
        return repository.findById(id).map(toEntityFn);
    }

    public Flux<E> findAll() {
        return repository.findAll().map(toEntityFn);
    }

    public Mono<Void> deleteById(I id) {
        return repository.deleteById(id);
    }
}