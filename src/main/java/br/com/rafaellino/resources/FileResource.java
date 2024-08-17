package br.com.rafaellino.resources;

import br.com.rafaellino.*;
import br.com.rafaellino.exception.CouldNotReadFileException;
import br.com.rafaellino.exception.CouldNotSaveFileException;
import br.com.rafaellino.exception.FileNotFoundException;
import br.com.rafaellino.utils.FileHandler;
import br.com.rafaellino.utils.HeaderList;

import java.util.ArrayList;
import java.util.List;

public class FileResource extends RequestHandler {

  private FileHandler fileHandler;

  public FileResource(FileHandler fileHandler) {
    this.fileHandler = fileHandler;
  }

  public HttpResponse get(HttpRequest httpRequest) {
    List<Header> headers = new HeaderList();
    String filename = httpRequest.getPath()
            .replace(httpRequest.getRootPath(), "");
    try {
      String fileContent = fileHandler.get(fileHandler.findByName(filename));
      headers.add(Constants.CONTENT_TYPE_FILE);
      headers.add(new Header(Constants.HEADER_LENGTH, String.valueOf(fileContent.length())));
      HttpResponse httpResponse = new HttpResponse(headers, fileContent, Constants.STATUS_OK);
      return httpResponse;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return new HttpResponse(Constants.STATUS_NOT_FOUND);
    } catch (CouldNotReadFileException e) {
      return new HttpResponse(Constants.STATUS_BAD_REQUEST);
    }
  }

  public HttpResponse post(HttpRequest httpRequest) {
    if (httpRequest.getHeaders().stream().anyMatch(
            h -> h.value().equalsIgnoreCase(Constants.CONTENT_TYPE_FILE.value()))) {
      final String fileContent = httpRequest.getBody();
      final String filename = httpRequest.getPath()
              .replace(httpRequest.getRootPath(), "");
      try {
        fileHandler.save(filename, fileContent);
        return new HttpResponse(Constants.STATUS_CREATED);
      } catch (CouldNotSaveFileException e) {
        e.printStackTrace();
        return new HttpResponse(Constants.STATUS_INTERNAL_SERVER_ERROR);
      }
    }
    return new HttpResponse(Constants.STATUS_BAD_REQUEST);
  }


}
