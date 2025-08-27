package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.data.UserData;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
// La implementación debe ser simple, sin parámetros genéricos
public class UserRepositoryAdapter implements UserRepository {

    private final UserDataRepository repository;

    @Override
    public Mono<User> save(User user) {
        return repository.save(toData(user))
                .map(this::toDomain);
    }

    // ... (El resto de la clase se mantiene igual)

    @Override
    public Mono<User> findById(Long id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Flux<User> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    private User toDomain(UserData userData) {
        return User.builder()
                .id(userData.getId())
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .email(userData.getEmail())
                .password(userData.getPassword())
                .role(User.Role.valueOf(userData.getRole()))
                .birthDate(userData.getBirthDate())
                .address(userData.getAddress())
                .phoneNumber(userData.getPhoneNumber())
                .baseSalary(userData.getBaseSalary())
                .build();
    }

    private UserData toData(User user) {
        return UserData.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().name())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .baseSalary(user.getBaseSalary())
                .build();
    }
}