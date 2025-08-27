package co.com.bancolombia.config;

import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.UserDataRepository;
import co.com.bancolombia.r2dbc.UserRepositoryAdapter;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }

    @Bean
    public UserRepository userRepository(UserDataRepository repository, ObjectMapper mapper) {
        return new UserRepositoryAdapter(repository);
    }

}