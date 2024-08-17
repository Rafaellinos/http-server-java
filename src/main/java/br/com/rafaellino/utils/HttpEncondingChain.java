package br.com.rafaellino.utils;

import br.com.rafaellino.Constants;
import br.com.rafaellino.Header;
import br.com.rafaellino.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HttpEncondingChain {

  private List<HttpEnconding> chain = new ArrayList<>();

  public HttpEnconding addEnconde(HttpEnconding httpEnconding) {
    this.chain.add(httpEnconding);
    return httpEnconding;
  }

  public HttpResponse process(HttpResponse httpResponse, List<Header> requestHeaders) {
    Optional<Header> encode = requestHeaders.stream().filter(h -> h.key().equalsIgnoreCase(Constants.HEADER_ENCODE))
            .findFirst();
    if (encode.isEmpty()) {
      return httpResponse;
    }
    String[] encodings = encode.get().value().replace(" ", "").split(",");
    // melhorar algoritmo para evitar nested for
    for (String encodeStr : encodings) {
      Optional<HttpEnconding> encondingAlgoritmn = chain.stream().filter(
              i -> i.equals(encodeStr)).findFirst();
      if (encondingAlgoritmn.isPresent()) {
        httpResponse = encondingAlgoritmn.get().encode(httpResponse);
      }
    }
    return httpResponse;
  }

}
