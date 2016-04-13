package com.mmnaseri.utils.spring.data.sample.models;

import com.mmnaseri.utils.spring.data.sample.models.Address;
import com.mmnaseri.utils.spring.data.sample.models.Zip;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 5:18 PM)
 */
public class NoAccessorPerson {

    private String id;
    private String firstName;
    private String lastName;
    private Zip addressZip;
    private Address address;

}
