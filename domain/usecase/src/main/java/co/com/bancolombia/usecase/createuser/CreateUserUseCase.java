package co.com.bancolombia.usecase.createuser;
import co.com.bancolombia.model.log.gateways.LoggerService;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.PasswordEncryptionGateway;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import co.com.bancolombia.model.user.gateways.PasswordEncryptionGateway.*;

import static co.com.bancolombia.model.constants.BusinessErrorMessageConstants.*;
import static co.com.bancolombia.model.constants.LogConstants.*;

@RequiredArgsConstructor
public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncryptionGateway passwordEncryptionGateway;
    private final LoggerService logger;

    public Mono<User> execute(User user) {
        logger.info(CREATE_USER_USE_CASE_STARTED, user.getEmail());
        return validateEmailDoesNotExist(user)
                .then(validateDocumentDoesNotExist(user))
                .then(encryptPasswordAndSave(user))
                .map(this::clearPasswordAndLogSuccess);
    }

    private Mono<Void> validateEmailDoesNotExist(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> {
                    logger.warn(EMAIL_ALREADY_EXISTS_WARN, user.getEmail());
                    return Mono.error(new BusinessException(EMAIL_ALREADY_IN_USE));
                }).then();
    }

    private Mono<Void> validateDocumentDoesNotExist(User user) {
        return userRepository.findByDocumentNumber(user.getDocumentNumber())
                .flatMap(existingUser -> {
                    logger.warn(DOCUMENT_ALREADY_EXISTS_WARN, user.getDocumentNumber());
                    return Mono.error(new BusinessException(DOCUMENT_NUMBER_ALREADY_IN_USE));
                }).then();
    }

    private Mono<User> encryptPasswordAndSave(User user) {
        logger.info(ENCRYPTING_PASSWORD, user.getEmail());
        return passwordEncryptionGateway.encode(user.getPassword())
                .map(hashedPassword -> user.toBuilder().password(hashedPassword).build())
                .flatMap(userRepository::save);
    }

    private User clearPasswordAndLogSuccess(User savedUser) {
        logger.info(USER_CREATED_SUCCESSFULLY, savedUser.getEmail());
        return savedUser.toBuilder().password(null).build();
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}