package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Card;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.CardRepository;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class DefaultCardServiceTest {

  private CardRepository repository;
  private DefaultCardService service;

  @BeforeMethod
  public void setUp() {
    repository = builder().mock(CardRepository.class);
    service = new DefaultCardService(repository);
  }

  @Test
  public void loadAllCardsWhenEmpty() {
    List<Card> list = service.load();

    assertThat(list, is(Matchers.<Card>empty()));
  }

  @Test
  public void loadAllCards() {
    Card a = new Card().setBlabla("a");
    Card b = new Card().setBlabla("b");
    Card c = new Card().setBlabla("c");
    repository.save(b);
    repository.save(c);
    repository.save(a);

    List<Card> list = service.load();

    assertThat(list, Matchers.<Card>hasSize(3));
    assertThat(list, contains(a, b, c));
  }
}