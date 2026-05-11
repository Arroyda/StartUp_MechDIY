package com.mechdiy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação MechDIY.
 * @SpringBootApplication engloba:
 *   - @Configuration: marca esta classe como fonte de beans
 *   - @EnableAutoConfiguration: ativa configuração automática do Spring Boot
 *   - @ComponentScan: varre o pacote em busca de @Component, @Service, @Repository, etc.
 */
@SpringBootApplication
public class MechDiyAplicacao {

    public static void main(String[] args) {
        SpringApplication.run(MechDiyAplicacao.class, args);
    }
}
