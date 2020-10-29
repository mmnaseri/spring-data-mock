/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.sample.models.PersonSerializable;
import org.springframework.data.repository.Repository;

/**
 * @author blackleg
 */
public interface SimplePersonSerializableRepository extends Repository<PersonSerializable, String> {

}
