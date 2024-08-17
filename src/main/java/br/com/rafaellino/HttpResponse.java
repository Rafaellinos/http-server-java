package br.com.rafaellino;

import br.com.rafaellino.utils.HeaderList;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpResponse {
  private List<Header> headers;
  private String body;
  private String statusCode;
  private byte[] bodyBytes;

  public HttpResponse(String statusCode) {
    headers = new HeaderList();
    body = "";
    this.statusCode = statusCode;
    bodyBytes = new byte[0];
  }

  public HttpResponse(List<Header> headers, String body, String statusCode) {
    this.headers = headers;
    this.setBody(body);
    this.statusCode = statusCode;
  }

  public byte[] getBytes() {
    StringBuilder response = new StringBuilder();
    response.append(statusCode).append(Constants.CRLF);
    for (Header header : headers) {
      response.append(header.key()).append(":").append(header.value()).append(Constants.CRLF);
    }
    response.append(Constants.CRLF);

    byte[] headers = response.toString().getBytes(StandardCharsets.UTF_8);

    byte[] byteResponse = new byte[headers.length + this.bodyBytes.length];

    System.arraycopy(headers, 0, byteResponse, 0, headers.length);
    if (this.bodyBytes.length > 0) {
      System.arraycopy(this.bodyBytes, 0, byteResponse, headers.length, this.bodyBytes.length);
    }

    return byteResponse;

  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.bodyBytes = body.getBytes(StandardCharsets.UTF_8);
    this.body = body;
  }

  public List<Header> getHeaders() {
    return headers;
  }

  public void setHeaders(List<Header> headers) {
    this.headers = headers;
  }

  public void setBodyBytes(byte[] bodyBytes) {
    this.bodyBytes = bodyBytes;
    this.body = new String(bodyBytes);
  }

  public byte[] getBodyBytes() {
    return bodyBytes;
  }
}
