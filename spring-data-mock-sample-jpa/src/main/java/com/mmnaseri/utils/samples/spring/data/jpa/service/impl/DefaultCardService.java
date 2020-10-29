package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Card;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.CardRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.service.CardService;

import java.util.List;

public class DefaultCardService implements CardService {

  private final CardRepository cardRepository;

  public DefaultCardService(final CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @Override
  public List<Card> load() {
    return cardRepository.findAllByOrderByBlablaAsc();
  }

}
