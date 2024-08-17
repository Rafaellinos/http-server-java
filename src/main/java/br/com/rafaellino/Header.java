package br.com.rafaellino;

public record Header(String key, String value) {

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof final Header header) {
     return header.key().equalsIgnoreCase(this.key());
    }
    return false;
  }
}
