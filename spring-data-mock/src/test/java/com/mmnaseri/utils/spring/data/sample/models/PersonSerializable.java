/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmnaseri.utils.spring.data.sample.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author blackleg
 */
public class PersonSerializable extends Person implements Serializable {

    @Override
    public String getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public static PersonSerializable build() {
        return new PersonSerializable();
    }
    
    public static List<PersonSerializable> getList(int count) {
        ArrayList<PersonSerializable> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(PersonSerializable.build());
        }
        return list;
    }
    
    public static List<PersonSerializable> getList() {
        return getList(3);
    }

}
