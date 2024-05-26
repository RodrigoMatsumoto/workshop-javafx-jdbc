package org.example.workshopjavafxjdbc.database;

public class DataBaseIntegrityException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DataBaseIntegrityException(String message) {
    super(message);
  }
}