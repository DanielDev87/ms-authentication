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
@Table("users")
public class UserData {

    @Id
    private Long id;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private BigDecimal baseSalary;
}