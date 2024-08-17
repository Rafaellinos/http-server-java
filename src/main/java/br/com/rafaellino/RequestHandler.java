package br.com.rafaellino;

public abstract class RequestHandler {

  public HttpResponse handle(HttpRequest httpRequest) {
    if (httpRequest.getMethod().equals(HttpMethod.GET)) {
      return this.get(httpRequest);
    }
    if (httpRequest.getMethod().equals(HttpMethod.POST)) {
      return this.post(httpRequest);
    }
    throw new RuntimeException("method " + httpRequest.getMethod() + " no implemented");
  }

  protected abstract HttpResponse get(HttpRequest httpRequest);

  protected abstract HttpResponse post(HttpRequest httpRequest);
}
