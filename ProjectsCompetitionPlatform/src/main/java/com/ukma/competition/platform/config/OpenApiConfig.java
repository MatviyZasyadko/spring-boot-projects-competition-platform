package com.ukma.competition.platform.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Project-competition-platform team",
            email = "project-competitions@mail.com"
        ),
        title = "Project competitions platform",
        description = " OpenAPI documentation for Spring Boot project called \"Project competitions platform\" that allows users to compete with each other and to win valuable prizes.",
        version = "1.0",
        license = @License(
            name = "Ukma license"
        )
    ),
    servers = {
        @Server(
            description = "Local ENV",
            url = "http://localhost:8080"
        )
    }
)
public class OpenApiConfig {
}
