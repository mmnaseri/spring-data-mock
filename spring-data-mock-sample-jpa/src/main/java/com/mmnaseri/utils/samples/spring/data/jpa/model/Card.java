package com.mmnaseri.utils.samples.spring.data.jpa.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Card {

  @Id
  @GeneratedValue
  private Integer id;

  private String blabla;

  public Integer getId() {
    return id;
  }

  public Card setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getBlabla() {
    return blabla;
  }

  public Card setBlabla(final String blabla) {
    this.blabla = blabla;
    return this;
  }
}
