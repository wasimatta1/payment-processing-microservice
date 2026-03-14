package payment.processing

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    GroupedOpenApi paymentApi() {
        GroupedOpenApi.builder()
                .group("payment-api")
                .pathsToMatch("/api/**")          // ← Scans all your /api endpoints
                .build()
    }

    @Bean
    OpenAPI customOpenAPI() {
        new OpenAPI()
                .info(new Info()
                        .title("Payment Processing Microservice")
                        .version("1.0")
                        .description("Backend Developer Technical Task - Full Implementation"))
    }
}