package cl.portafolio.m7.usuarios.web;

import cl.portafolio.m7.usuarios.service.DuplicateEmailException;
import cl.portafolio.m7.usuarios.service.NotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String,Object>> notFound(NotFoundException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("NOT_FOUND", ex.getMessage()));
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<Map<String,Object>> duplicate(DuplicateEmailException ex){
    return ResponseEntity.status(HttpStatus.CONFLICT).body(err("DUPLICATE_EMAIL", ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,Object>> badRequest(IllegalArgumentException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("VALIDATION_ERROR", ex.getMessage()));
  }

  private Map<String,Object> err(String code, String message){
    Map<String,Object> m = new HashMap<>();
    m.put("code", code);
    m.put("message", message);
    return m;
  }
}