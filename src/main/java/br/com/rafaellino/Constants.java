package br.com.rafaellino;

import java.util.List;

public class Constants {

  public static final List<String> httpVersionsSupported = List.of("HTTP/1.1");

  public static final String STATUS_OK = "HTTP/1.1 200 OK";
  public static final String STATUS_INTERNAL_SERVER_ERROR = "HTTP/1.1 500 Internal Server Error";
  public static final String STATUS_NOT_FOUND = "HTTP/1.1 404 Not Found";
  public static final String STATUS_BAD_REQUEST = "HTTP/1.1 400 Bad Request";
  public static final String STATUS_CREATED = "HTTP/1.1 201 Created";
  public static final String CRLF = "\r\n";
  public static final String HEADER_CONTENT = "Content-Type";
  public static final String HEADER_LENGTH = "Content-Length";
  public static final String HEADER_ENCODE = "Accept-Encoding";

  public static final Header CONTENT_TYPE_JSON = new Header(HEADER_CONTENT, "application/json");
  public static final Header CONTENT_TYPE_TXT = new Header(HEADER_CONTENT, "text/plain");
  public static final Header CONTENT_TYPE_FILE = new Header(HEADER_CONTENT, "application/octet-stream");

  public static final String ARG_DIRECTORY = "--directory";
  public static final String ARG_PORT = "--port";
  public static final String ARG_THEAD_POOL = "--thread-pool";
}
