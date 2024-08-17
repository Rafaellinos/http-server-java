package br.com.rafaellino.resources;

import br.com.rafaellino.*;
import br.com.rafaellino.utils.HeaderList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserAgentResource extends RequestHandler {

  @Override
  public HttpResponse get(HttpRequest httpRequest) {
    Optional<Header> header = httpRequest.getHeaders().stream().filter(h -> h.key().contains("User-Agent")).findFirst();
    List<Header> headers = new HeaderList();
    String result = header.isEmpty() ? "" : header.get().value();
    headers.add(Constants.CONTENT_TYPE_TXT);
    headers.add(new Header(Constants.HEADER_LENGTH, String.valueOf(result.length())));
    return new HttpResponse(headers, result, Constants.STATUS_OK);
  }

  @Override
  public HttpResponse post(HttpRequest httpRequest) {
    throw new RuntimeException();
  }

}
