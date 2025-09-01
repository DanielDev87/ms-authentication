package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.LoginDTO;
import co.com.bancolombia.api.dto.TokenDTO;
import co.com.bancolombia.api.dto.UserDTO;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.createuser.CreateUserUseCase;
import co.com.bancolombia.usecase.findbydocumentnumber.FindByDocumentNumberUseCase;
import co.com.bancolombia.usecase.login.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.constants.LogConstants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {
    private final UserTransactionalUseCase userTransactionalUseCase;
    private final FindByDocumentNumberUseCase findByDocumentNumberUseCase;
    private final LoginUseCase loginUseCase;

    public Mono<ServerResponse> getUserByDocumentNumber(ServerRequest serverRequest) {
        String documentNumber = serverRequest.pathVariable("documentNumber");
        log.info(USER_SEARCH_BY_DOCUMENT_STARTED, documentNumber);
        return findByDocumentNumberUseCase.execute(documentNumber)
                .flatMap(user -> ServerResponse.ok().bodyValue(toDTO(user)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(userDTO -> {
                    log.info(USER_CREATION_REQUEST_RECEIVED, userDTO.getEmail());
                    return userTransactionalUseCase.createUser(toModel(userDTO))
                            .flatMap(userSaved -> {
                                log.info(USER_CREATED_SUCCESSFULLY_WITH_ID, userSaved.getId());
                                return ServerResponse.status(HttpStatus.CREATED).build();
                            })
                            .onErrorResume(CreateUserUseCase.BusinessException.class, e -> {
                                log.warn(CONFLICT_CREATING_USER, userDTO.getEmail(), e.getMessage());
                                return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
                            })
                            .onErrorResume(Exception.class, e -> {
                                log.error(UNEXPECTED_ERROR_CREATING_USER_DETAIL, e.getClass().getName(), e.getMessage());
                                return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                            });
                });
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginDTO.class)
                .flatMap(dto -> loginUseCase.execute(dto.getEmail(), dto.getPassword()))
                .flatMap(token -> ServerResponse.ok().bodyValue(new TokenDTO(token)))
                .onErrorResume(LoginUseCase.BusinessException.class, e ->
                        ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }

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
                .build();
    }
}