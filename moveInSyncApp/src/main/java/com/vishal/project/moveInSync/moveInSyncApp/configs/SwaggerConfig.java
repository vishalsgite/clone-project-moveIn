package com.vishal.project.moveInSync.moveInSyncApp.configs;

import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("MoveInSync - CLONE PROJECT API Documentation")
                        .version("v1.0")
                        .description("This API documentation provides comprehensive details about the CLONE -MoveInSync project, a corporate transportation management system. It includes endpoints for managing user authentication, trip scheduling, cab allocation, and corporate shift rosters, ensuring seamless and efficient commute operations. " +
                                "The API is designed to facilitate integration with corporate systems, " +
                                "offering secure and scalable solutions for employee transportation needs .KINDLY NOTE: " +
                                "Project Developed For Learning Purposes Only")
                        .contact(new Contact()
                                .name("Vishal GITE")
                                .email("vshlsgite.98.vg@gmail.com")
                                .url("https://example.com")))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}
