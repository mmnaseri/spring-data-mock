package com.mmnaseri.utils.spring.data.sample.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Balthasar Biedermann
 */
@Embeddable
public class EmbeddableId implements Serializable {
    private String str;
    private Integer integer;
}
