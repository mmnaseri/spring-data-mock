package com.mmnaseri.utils.samples.spring.data.jpa.model;

import javax.persistence.MappedSuperclass;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:36 PM)
 */
@MappedSuperclass
public class SerialAwareEntity {

  private String serial;

  public String getSerial() {
    return serial;
  }

  public void setSerial(String serial) {
    this.serial = serial;
  }
}
