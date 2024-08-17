package br.com.rafaellino.utils;

import br.com.rafaellino.Header;
import br.com.rafaellino.HttpResponse;

import java.util.List;

public interface HttpEnconding {

  HttpResponse encode(HttpResponse httpResponse);

  boolean equals(String encodeName);

}
