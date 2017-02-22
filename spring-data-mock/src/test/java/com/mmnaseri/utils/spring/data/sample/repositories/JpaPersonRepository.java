/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author blackleg
 */
public interface JpaPersonRepository extends JpaRepository<Person, String> {
    
}
