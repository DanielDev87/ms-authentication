package co.com.bancolombia.api.documentation;

import co.com.bancolombia.api.handler.Handler;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface UserApiDocumentation {
    RouterFunction<ServerResponse> routerFunction(Handler handler);
}