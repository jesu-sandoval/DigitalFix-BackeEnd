package com.digitalfix.catalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresDeEjecucion(RuntimeException ex) {
        Map<String, Object> respuestaError = new HashMap<>();
        respuestaError.put("timestamp", LocalDateTime.now());
        respuestaError.put("estado", HttpStatus.BAD_REQUEST.value());
        respuestaError.put("error", "Peticion incorrecta");
        respuestaError.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(respuestaError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErroresGenerales(Exception ex) {
        Map<String, Object> respuestaError = new HashMap<>();
        respuestaError.put("timestamp", LocalDateTime.now());
        respuestaError.put("estado", HttpStatus.INTERNAL_SERVER_ERROR.value());
        respuestaError.put("error", "Falla interna del servidor");
        respuestaError.put("mensaje", "Ocurrio un error inesperado al procesar el catalogo");


        return new ResponseEntity<>(respuestaError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}