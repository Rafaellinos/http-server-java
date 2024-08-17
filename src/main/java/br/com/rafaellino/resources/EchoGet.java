package br.com.rafaellino.resources;

import br.com.rafaellino.*;
import br.com.rafaellino.utils.HeaderList;

import java.util.ArrayList;
import java.util.List;

public class EchoGet extends RequestHandler {

  @Override
  public HttpResponse get(HttpRequest httpRequest) {
    String[] paths = httpRequest.getPath().split("/");
    String result = "";
    if (paths.length > 2) {
      result = paths[2];
    }
    List<Header> headers = new HeaderList();
    headers.add(new Header(Constants.HEADER_CONTENT, "text/plain"));
    headers.add(new Header(Constants.HEADER_LENGTH, String.valueOf(result.length())));
    return new HttpResponse(headers, result, Constants.STATUS_OK);
  }

  @Override
  public HttpResponse post(HttpRequest httpRequest) {
    throw new RuntimeException();
  }

}
