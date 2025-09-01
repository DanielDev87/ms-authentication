package co.com.bancolombia.api;

import co.com.bancolombia.api.documentation.UserApiDocumentation;
import co.com.bancolombia.api.handler.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ApiRest implements UserApiDocumentation {

    @Bean
    @Override
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/login"), handler::login)
                .andRoute(POST("/api/v1/users"), handler::createUser)
                .andRoute(GET("/api/v1/users/document/{documentNumber}"), handler::getUserByDocumentNumber);
    }
}