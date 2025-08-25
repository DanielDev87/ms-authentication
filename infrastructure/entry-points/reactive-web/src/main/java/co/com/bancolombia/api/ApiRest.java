package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ApiRest {

    private final CreateUserUseCase createUserUseCase;
    private final TransactionalOperator transactionalOperator;

    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("Recibida petición para crear usuario con email: {}", userDTO.getEmail());
        return createUserUseCase.execute(toModel(userDTO))
                .map(userSaved -> {
                    log.info("Usuario creado exitosamente con ID: {}", userSaved.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
                })
                // Envuelve toda la operación en una transacción
                .as(transactionalOperator::transactional)
                .onErrorResume(CreateUserUseCase.BusinessException.class, e -> {
                    log.warn("Conflicto al crear usuario con email {}: {}", userDTO.getEmail(), e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                });
    }

    // Método privado para convertir el DTO al modelo de dominio
    private User toModel(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(User.Role.valueOf(userDTO.getRole()))
                .birthDate(userDTO.getBirthDate())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .baseSalary(userDTO.getBaseSalary())
                .build();
    }
}