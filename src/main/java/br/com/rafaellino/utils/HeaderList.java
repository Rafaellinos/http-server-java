package br.com.rafaellino.utils;

import br.com.rafaellino.Header;

import java.util.ArrayList;

public class HeaderList extends ArrayList<Header> {

  @Override
  public boolean add(Header header) {
    this.remove(header);
    return super.add(header);
  }
}
