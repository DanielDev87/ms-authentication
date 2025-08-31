package co.com.bancolombia.api.dto;

import co.com.bancolombia.api.constants.ValidationConstants;
import co.com.bancolombia.model.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "UserDTO", description = "DTO para la creación y visualización de usuarios")
public class UserDTO {

    @Schema(description = "ID único del usuario (generado automáticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = ValidationConstants.DOCUMENT_NUMBER_NOT_BLANK)
    @Schema(description = "Número de documento del usuario", example = "1037665432", required = true)
    private String documentNumber;

    @NotBlank(message = ValidationConstants.FIRST_NAME_NOT_BLANK)
    private String firstName;

    @NotBlank(message = ValidationConstants.LAST_NAME_NOT_BLANK)
    private String lastName;

    private LocalDate birthDate;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK)
    private String password;

    private User.Role role;
    private String address;
    private String phoneNumber;

    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK)
    @Email(message = ValidationConstants.EMAIL_FORMAT_INVALID)
    private String email;

    @NotNull(message = ValidationConstants.SALARY_NOT_NULL)
    @DecimalMin(value = "0.0", message = ValidationConstants.SALARY_NOT_NEGATIVE)
    @DecimalMax(value = "15000000.0", message = ValidationConstants.SALARY_MAX_EXCEEDED)
    private BigDecimal baseSalary;
}