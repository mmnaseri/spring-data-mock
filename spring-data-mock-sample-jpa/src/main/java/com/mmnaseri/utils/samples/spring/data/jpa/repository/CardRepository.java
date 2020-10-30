package com.mmnaseri.utils.samples.spring.data.jpa.repository;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {

  List<Card> findAllByOrderByBlablaAsc();
}
