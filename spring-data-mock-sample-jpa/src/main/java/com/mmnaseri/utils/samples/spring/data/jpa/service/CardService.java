package com.mmnaseri.utils.samples.spring.data.jpa.service;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Card;

import java.util.List;

public interface CardService {

  List<Card> load();
}
