package co.com.bancolombia.r2dbc.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users") // Nombre exacto de la tabla en la base de datos
public class UserData {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role; // Guardaremos el rol como un String (ej. "APPLICANT" o "ADMIN")
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private BigDecimal baseSalary;
}