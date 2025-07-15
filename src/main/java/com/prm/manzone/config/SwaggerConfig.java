package com.prm.manzone.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API for ManZone", version = "1.0", description = "API for ManZone application", contact = @Contact(name = "ManZone Team", email = "manzone@gmail.com")), servers = {
                @Server(url = "https://manzone.wizlab.io.vn", description = "Production Server"),
                @Server(url = "http://localhost:5173", description = "Frontend Development Server"),
                @Server(url = "https://manzone-admin.vercel.app/", description = "Frontend Production Server"),
                @Server(url = "http://localhost:3001", description = "Local Development Server")
})
@SecurityScheme(name = "Bearer Authentication", description = "JWT Token Authentication", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {
}
