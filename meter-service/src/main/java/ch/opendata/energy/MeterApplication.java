package ch.opendata.energy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
TODO:   Once the service is ready for release make sure the service is registered
        under services including url, add the service to docs.service in api-gateway and
        add the newly config url https://api.core.${env}.akenza.io/docs/${service}/api-docs.yaml to urls
        property in swagger-ui config map in order to expose the docs on
        https://docs.core.${env}.akenza.io.
        Use springdoc.paths-to-exclude property in application properties or config maps in
        production environment in order to exclude private endpoints which should not be
        exposed in public api docs.
        The docs are accessible under /v3/api-docs (json), /v3/api-docs.yaml & /swagger-ui.html.
 */
@OpenAPIDefinition(
		info = @Info(title = "Akenza Core Prototype Service API", version = "3.0", description = "Internal Documentation Prototype API v3.0"),
		security = {
				@SecurityRequirement(name = "Authorization"),
				@SecurityRequirement(name = "OAuth2")}
)

@SecuritySchemes(value = {
		@SecurityScheme(
				name = "OAuth2",
				type = SecuritySchemeType.OAUTH2,
				in = SecuritySchemeIn.HEADER,
				bearerFormat = "jwt",
				flows = @OAuthFlows(
//	TODO: Don't enable this flow for production, feel free to use in development
//						clientCredentials = @OAuthFlow(
//								authorizationUrl = "${service.auth.host:https://auth.akenza.io}/oauth/authorize",
//								scopes = {
//										@OAuthScope(name = "core.ui", description = "UI Access"),
//										@OAuthScope(name = "core.api", description = "API Access"),
//										@OAuthScope(name = "core.auth", description = "Auth Access"),
//										@OAuthScope(name = "core.system", description = "System Access")
//								},
//								tokenUrl = "${service.auth.host:https://auth.akenza.io}/oauth/token",
//								refreshUrl = "${service.auth.host:https://auth.akenza.io}/oauth/refresh"
//
//						),
						password = @OAuthFlow(
								authorizationUrl = "${service.auth.host:https://auth.akenza.io}/oauth/authorize",
								scopes = {
										@OAuthScope(name = "core.ui", description = "UI Access"),
										@OAuthScope(name = "core.api", description = "API Access")
								},
								tokenUrl = "${service.auth.host:https://auth.akenza.io}/oauth/token",
								refreshUrl = "${service.auth.host:https://auth.akenza.io}/oauth/refresh"
						)
				)
		),
		@SecurityScheme(
				description = "JWT Authorization header using the Bearer scheme. (Use \"Bearer {token}\") ",
				name = "Authorization",
				type = SecuritySchemeType.APIKEY,
				in = SecuritySchemeIn.HEADER,
				bearerFormat = "jwt"
		)}
)
@SpringBootApplication
public class MeterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeterApplication.class, args);
	}
}
