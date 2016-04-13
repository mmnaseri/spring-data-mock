package com.mmnaseri.utils.spring.data.sample.models;

import org.joda.time.DateTime;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 5:21 PM)
 */
public class EntityWithDateTimeLastModifiedDate {

    private String id;
    @LastModifiedDate
    private DateTime lastModifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
