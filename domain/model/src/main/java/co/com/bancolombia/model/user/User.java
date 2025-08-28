package co.com.bancolombia.model.user;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    public enum Role {
        APPLICANT, // Solicitante
        ADMIN      // Administrador
    }
}
