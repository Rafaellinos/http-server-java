package br.com.rafaellino.exception;

public class CouldNotSaveFileException extends Throwable {
  public CouldNotSaveFileException(String message) {
    super(message);
  }

  public CouldNotSaveFileException(String message, Throwable cause) {
    super(message, cause);
  }

  public CouldNotSaveFileException(Throwable cause) {
    super(cause);
  }
}
