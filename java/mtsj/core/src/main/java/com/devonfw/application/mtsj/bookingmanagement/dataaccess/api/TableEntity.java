package com.devonfw.application.mtsj.bookingmanagement.dataaccess.api;

import javax.persistence.Entity;

import com.devonfw.application.mtsj.bookingmanagement.common.api.Table;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link Table}.
 */
@Entity
@javax.persistence.Table(name = "\"Table\"")
public class TableEntity extends ApplicationPersistenceEntity implements Table {

  private Integer seatsNumber;

  private static final long serialVersionUID = 1L;

  /**
   * @return seatsNumber
   */
  @Override
  public Integer getSeatsNumber() {

    return this.seatsNumber;
  }

  /**
   * @param seatsNumber new value of {@link #getSeatsNumber}.
   */
  @Override
  public void setSeatsNumber(Integer seatsNumber) {

    this.seatsNumber = seatsNumber;
  }

}
