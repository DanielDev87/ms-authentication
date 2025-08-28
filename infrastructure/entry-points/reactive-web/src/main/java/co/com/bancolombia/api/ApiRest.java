package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import co.com.bancolombia.usecase.findbydocumentnumber.FindByDocumentNumberUseCase;
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
    private final FindByDocumentNumberUseCase  findByDocumentNumberUseCase;
    private final TransactionalOperator transactionalOperator;

    @GetMapping("/document/{documentNumber}")
    public Mono<ResponseEntity<UserDTO>> getUserByDocumentNumber(@PathVariable("documentNumber") String documentNumber) {
        log.info("Buscando usuario con documento: {}", documentNumber);
        return findByDocumentNumberUseCase.execute(documentNumber)
                .map(user -> ResponseEntity.ok(toDTO(user)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("Recibida petición para crear usuario con email: {}", userDTO.getEmail());


        Mono<User> userMono = createUserUseCase.execute(toModel(userDTO));

        return userMono
                .as(transactionalOperator::transactional)
                .map(userSaved -> {
                    log.info("Usuario creado exitosamente con ID: {}", userSaved.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(userSaved));
                })
                .onErrorResume(CreateUserUseCase.BusinessException.class, e -> {
                    log.warn("Conflicto al crear usuario con email {}: {}", userDTO.getEmail(), e.getMessage());
                    // Devolvemos 409 Conflict, que es más específico para recursos duplicados
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build());
                })
                .onErrorResume(Exception.class, e -> {
                    log.error("Error inesperado al crear usuario: {} - {}", e.getClass().getName(), e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    // Metodo privado para convertir el DTO al modelo de dominio
    private User toModel(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .documentNumber(userDTO.getDocumentNumber())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .birthDate(userDTO.getBirthDate())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .email(userDTO.getEmail())
                .baseSalary(userDTO.getBaseSalary())
                .role(userDTO.getRole() == null ? User.Role.APPLICANT : userDTO.getRole())
                .build();
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .documentNumber(user.getDocumentNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .baseSalary(user.getBaseSalary())
                // La contraseña se deja en null por seguridad
                .build();
    }
}