package br.com.rafaellino.utils;

import br.com.rafaellino.exception.CouldNotReadFileException;
import br.com.rafaellino.exception.CouldNotSaveFileException;
import br.com.rafaellino.exception.FileNotFoundException;

public interface FileHandler {

  String findByName(final String filename) throws FileNotFoundException;

  void save(final String filename, final String fileContent) throws CouldNotSaveFileException;

  String get(String filePath) throws CouldNotReadFileException;
}
