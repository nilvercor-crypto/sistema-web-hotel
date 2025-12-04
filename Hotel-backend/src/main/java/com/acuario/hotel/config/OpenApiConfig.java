package com.acuario.hotel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI().info(new Info().title("API - Hotel Acuario")
				.description("Servicios REST para reservas, clientes y habitaciones").version("v1.0")
				.contact(new Contact().name("Equipo Integrador I").email("contacto@acuario.com")));
	}
}
