package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.user.User;
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
public class UserDTO {
    private Long id;

    @NotBlank(message = "El número de documento no puede estar vacío")
    private String documentNumber;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    private LocalDate birthDate;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
    private User.Role role;
    private String address;
    private String phoneNumber;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email es inválido")
    private String email;

    @NotNull(message = "El salario base no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El salario base no puede ser negativo")
    @DecimalMax(value = "15000000.0", message = "El salario base no puede exceder 15,000,000")
    private BigDecimal baseSalary;


}