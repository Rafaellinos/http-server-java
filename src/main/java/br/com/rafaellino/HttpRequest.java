package br.com.rafaellino;

import br.com.rafaellino.utils.HeaderList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {

  private static final Pattern regexSplitHeader = Pattern.compile("([^:]+):\\s*(.*)");

  private final HttpMethod method;
  private final String path;
  private final String rootPath;
  private final String httpVersion;
  private final List<Header> headers;
  private final String body;

  public HttpRequest(
          HttpMethod method, String path, String rootPath, String httpVersion, List<Header> headers,
          String body) {
    if (!httpVersion.equalsIgnoreCase("HTTP/1.1")) {
      throw new RuntimeException("HTTP version not supported!");
    }
    this.method = method;
    this.path = path;
    this.rootPath = rootPath;
    this.httpVersion = httpVersion;
    this.headers = headers;
    this.body = body;
  }

  private static List<Header> readHeader(String[] headersString) {
    List<Header> headers = new HeaderList();
    String occurency;
    for (int i = 1; i < headersString.length; i++) {
      occurency = headersString[i];
      if (occurency.isEmpty()) {
        return headers; // means reach the end of headers (CRLF)
      }
      Matcher matcher = regexSplitHeader.matcher(occurency);
      if (matcher.matches()) {
        headers.add(new Header(matcher.group(1), matcher.group(2)));
      }
    }
    return headers;
  }

  public static HttpRequest makeFromString(String rawHttpInfo) {
    String[] data = rawHttpInfo.split(Constants.CRLF, -1);
    String[] requestLine = data[0].split(" ");
    String rootPath = requestLine[1].length() > 1 ? "/" + requestLine[1].split("/")[1] : "/";
    String body = data[data.length - 1];
    return new HttpRequest(
            HttpMethod.valueOf(requestLine[0].toUpperCase()),
            requestLine[1],
            rootPath,
            requestLine[2],
            readHeader(data),
            body);
  }

  public String getBody() {
    return body;
  }

  public List<Header> getHeaders() {
    return headers;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getHttpVersion() {
    return httpVersion;
  }

  public String getRootPath() {
    return rootPath;
  }

  public String getPath() {
    return path;
  }

}
