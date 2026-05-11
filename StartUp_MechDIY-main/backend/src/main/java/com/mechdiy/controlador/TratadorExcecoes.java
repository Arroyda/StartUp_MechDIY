package com.mechdiy.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centraliza o tratamento de erros da API.
 *
 * @RestControllerAdvice intercepta exceções lançadas por qualquer Controlador
 * e retorna uma resposta HTTP padronizada em vez do stack trace padrão do Spring.
 */
@RestControllerAdvice
public class TratadorExcecoes {

    /**
     * Trata RuntimeException lançada nos Serviços quando um recurso não é encontrado.
     * Retorna HTTP 404 com um corpo de erro padronizado.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(RuntimeException ex) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("momento", LocalDateTime.now().toString());
        corpo.put("status", HttpStatus.NOT_FOUND.value());
        corpo.put("erro", "Não Encontrado");
        corpo.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
    }

    /**
     * Trata qualquer outra exceção não prevista.
     * Retorna HTTP 500 sem expor detalhes internos ao cliente.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> tratarErroGenerico(Exception ex) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("momento", LocalDateTime.now().toString());
        corpo.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        corpo.put("erro", "Erro Interno do Servidor");
        corpo.put("mensagem", "Ocorreu um erro interno. Tente novamente mais tarde.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(corpo);
    }
}
