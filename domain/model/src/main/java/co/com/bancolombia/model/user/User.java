package co.com.bancolombia.model.user;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class User {
    private Long id;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String password;
    private Role role;
    private String address;
    private String phoneNumber;
    private String email;
    private BigDecimal baseSalary;
    private Integer failedLoginAttempts;
    private LocalDateTime accountLockedUntil;

    public enum Role {
        CLIENT,    // Reemplaza a APPLICANT
        ADMIN,     // Admin
        ADVISER    // Nuevo rol para crear usuarios
    }
}
