package br.com.rafaellino.exception;

public class CouldNotReadFileException extends Throwable {
  public CouldNotReadFileException(String message) {
    super(message);
  }

  public CouldNotReadFileException(String message, Throwable cause) {
    super(message, cause);
  }

  public CouldNotReadFileException(Throwable cause) {
    super(cause);
  }
}
