package co.com.bancolombia;

import co.com.bancolombia.config.AdapterConfig;
import co.com.bancolombia.config.UseCasesConfig;
import co.com.bancolombia.r2dbc.config.PostgreSQLConnectionPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication

@Import({UseCasesConfig.class, AdapterConfig.class, PostgreSQLConnectionPool.class})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}