package br.com.rafaellino.exception;

public class FileNotFoundException extends Throwable {
  public FileNotFoundException(String message) {
    super(message);
  }

  public FileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileNotFoundException(Throwable cause) {
    super(cause);
  }
}
