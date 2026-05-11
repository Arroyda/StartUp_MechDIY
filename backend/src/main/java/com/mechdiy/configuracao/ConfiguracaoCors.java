package com.mechdiy.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing).
 *
 * Browsers bloqueiam por padrão requisições entre origens diferentes.
 * Como o frontend React (porta 5173) e o backend (porta 8080) são origens
 * distintas, precisamos liberar explicitamente o acesso.
 */
@Configuration
public class ConfiguracaoCors {

    @Bean
    public WebMvcConfigurer configuradorCors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registro) {
                registro
                    .addMapping("/api/**")
                    // Origens permitidas: frontends de desenvolvimento
                    .allowedOrigins(
                            "http://localhost:5173",  // Vite dev server padrão
                            "http://localhost:3000",  // CRA / Next.js
                            "http://localhost:5500",  // Live Server (VSCode)
                            "http://127.0.0.1:5500",  // Live Server (VSCode) — variante 127.0.0.1
                            "http://localhost:8000",  // python -m http.server
                            "http://127.0.0.1:8000"   // python -m http.server — variante 127.0.0.1
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
