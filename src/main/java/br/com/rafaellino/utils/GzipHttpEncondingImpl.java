package br.com.rafaellino.utils;

import br.com.rafaellino.Constants;
import br.com.rafaellino.Header;
import br.com.rafaellino.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class GzipHttpEncondingImpl implements HttpEnconding {

  private static final String encodeName = "Gzip";
  private static final Header gzipHeader = new Header("Content-Encoding", encodeName.toLowerCase());

  @Override
  public HttpResponse encode(HttpResponse httpResponse) {
    return this.compress(httpResponse);
  }

  @Override
  public boolean equals(String obj) {
    if (obj == null) {
      return false;
    }
    return obj.equalsIgnoreCase(encodeName);
  }

  private HttpResponse compress(HttpResponse httpResponse) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
      gzipOutputStream.write(httpResponse.getBody().getBytes(StandardCharsets.UTF_8));
      gzipOutputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
      return httpResponse;
    }
    httpResponse.setBodyBytes(byteArrayOutputStream.toByteArray());
    List<Header> headers = httpResponse.getHeaders();
    headers.add(gzipHeader);
    headers.add(Constants.CONTENT_TYPE_TXT);
    headers.add(new Header(Constants.HEADER_LENGTH, String.valueOf(httpResponse.getBody().length())));
    return httpResponse;
  }
}
