package br.com.rafaellino.utils;

import br.com.rafaellino.exception.CouldNotReadFileException;
import br.com.rafaellino.exception.CouldNotSaveFileException;
import br.com.rafaellino.exception.FileNotFoundException;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileHandlerIoImpl implements FileHandler {

  private Path defaultPath = Path.of("/tmp");

  public FileHandlerIoImpl(Path defaultPath) {
    try {
      if (!Files.exists(defaultPath)) {
        this.defaultPath = Files.createDirectories(defaultPath);
      } else {
        this.defaultPath = defaultPath;
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Invalid path " + defaultPath);
    }
  }

  public FileHandlerIoImpl(String defaultPath) {
    this(Path.of(defaultPath));
  }

  public FileHandlerIoImpl() {
  }

  @Override
  public String findByName(final String filename) throws FileNotFoundException {
    final String filenameFormatted = filename.replace("/", "");
    try (Stream<Path> stream = Files.walk(defaultPath, FileVisitOption.FOLLOW_LINKS)) {
      return stream
              .filter(Files::isRegularFile)
              .filter(path -> path.getFileName().toString().equals(filenameFormatted))
              .findFirst().orElseThrow(() -> new FileNotFoundException("file not found " + filenameFormatted)).toAbsolutePath().toString();
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileNotFoundException(e);
    }
  }

  @Override
  public String get(String filePath) throws CouldNotReadFileException {
    Path filepath = Path.of(filePath);
    try {
      if (Files.isRegularFile(filepath)) {
        return Files.readString(filepath);
      }
      throw new CouldNotReadFileException("Not a file");
    } catch (IOException e) {
      e.printStackTrace();
      throw new CouldNotReadFileException(e);
    }
  }

  @Override
  public void save(String filename, String fileContent) throws CouldNotSaveFileException {
    if (filename.startsWith("/")) {
      filename = filename.substring(1);
    }
    try {
      Path created = defaultPath.resolve(filename);
      if (!created.getParent().equals(defaultPath)) {
        Files.createDirectory(created.getParent());
      }
      Files.writeString(created, fileContent);
    } catch (IOException e) {
      e.printStackTrace();
      throw new CouldNotSaveFileException(e);
    }
  }
}
