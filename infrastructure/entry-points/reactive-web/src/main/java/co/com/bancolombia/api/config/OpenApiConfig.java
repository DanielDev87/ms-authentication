package co.com.bancolombia.api.config;

import co.com.bancolombia.api.constants.ApiConstants;
import co.com.bancolombia.api.dto.LoginDTO;
import co.com.bancolombia.api.dto.TokenDTO;
import co.com.bancolombia.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Microservicio de Autenticación",
                version = "1.0.0",
                description = "API REST para la gestión de usuarios y autenticación"
        )
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer userApiCustomizer() {
        return openApi -> {
            final String userDtoSchemaRef = UserDTO.class.getSimpleName();
            final String loginDtoSchemaRef = LoginDTO.class.getSimpleName();
            final String tokenDtoSchemaRef = TokenDTO.class.getSimpleName();

            openApi.getPaths().addPathItem("/api/v1/users", new PathItem()
                    .post(new Operation()
                            .addTagsItem(ApiConstants.USER_TAG)
                            .summary(ApiConstants.CREATE_USER_SUMMARY)
                            .description(ApiConstants.CREATE_USER_DESC)
                            .requestBody(new RequestBody()
                                    .description(ApiConstants.CREATE_USER_REQ_BODY_DESC)
                                    .required(true)
                                    .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                            new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + userDtoSchemaRef))))
                            )
                            .responses(new ApiResponses()
                                    .addApiResponse("201", new ApiResponse().description(ApiConstants.RESPONSE_201))
                                    .addApiResponse("400", new ApiResponse().description(ApiConstants.RESPONSE_400))
                            )
                    )
            );

            openApi.getPaths().addPathItem("/api/v1/users/document/{documentNumber}", new PathItem()
                    .get(new Operation()
                            .addTagsItem(ApiConstants.USER_TAG)
                            .summary(ApiConstants.GET_USER_BY_DOC_SUMMARY)
                            .description(ApiConstants.GET_USER_BY_DOC_DESC)
                            .addParametersItem(new Parameter()
                                    .in("path")
                                    .name("documentNumber")
                                    .description(ApiConstants.GET_USER_BY_DOC_PARAM_DESC)
                                    .required(true)
                                    .schema(new Schema<>().type("string").example("1037665432"))
                            )
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description(ApiConstants.RESPONSE_200)
                                            .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + userDtoSchemaRef)))))
                                    .addApiResponse("404", new ApiResponse().description(ApiConstants.RESPONSE_404))
                            )
                    )
            );

            openApi.getPaths().addPathItem("/api/v1/login", new PathItem()
                    .post(new Operation()
                            .addTagsItem(ApiConstants.AUTH_TAG)
                            .summary(ApiConstants.LOGIN_USER_SUMMARY)
                            .description(ApiConstants.LOGIN_USER_DESC)
                            .requestBody(new RequestBody()
                                    .description(ApiConstants.LOGIN_REQ_BODY_DESC)
                                    .required(true)
                                    .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                            new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + loginDtoSchemaRef))))
                            )
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description(ApiConstants.RESPONSE_200_LOGIN)
                                            .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + tokenDtoSchemaRef)))))
                                    .addApiResponse("401", new ApiResponse().description(ApiConstants.RESPONSE_401))
                            )
                    )
            );
        };
    }
}