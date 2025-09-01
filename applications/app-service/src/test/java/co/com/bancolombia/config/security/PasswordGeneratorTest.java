package co.com.bancolombia.config.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGeneratorTest {

    @Test
    void generatePasswordHash() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123"; // La contraseña que quieres usar
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("CONTRASEÑA EN TEXTO PLANO: " + rawPassword);
        System.out.println("HASH GENERADO: " + encodedPassword);
    }
}
