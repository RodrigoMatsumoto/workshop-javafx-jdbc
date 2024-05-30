package org.example.workshopjavafxjdbc.model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final Map<String, String> errors = new HashMap<>();

  public ValidationException(final String message) {
    super(message);
  }

  public Map<String, String> getErrors() {
    return errors;
  }

  public void addError(String fieldName, String errorMessage) {
    errors.put(fieldName, errorMessage);
  }
}