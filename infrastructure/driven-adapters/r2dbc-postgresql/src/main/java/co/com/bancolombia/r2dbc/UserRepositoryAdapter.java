package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.data.UserData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserDataRepository repository;
    private final ObjectMapper mapper;

    @Override
    public Mono<User> save(User user) {
        return repository.save(toData(user))
                .map(this::toDomain);
    }


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
    public Mono<User> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber)
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
                .documentNumber(userData.getDocumentNumber())
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
                .documentNumber(user.getDocumentNumber())
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