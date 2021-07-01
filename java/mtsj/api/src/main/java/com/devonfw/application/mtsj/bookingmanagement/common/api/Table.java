package com.devonfw.application.mtsj.bookingmanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for Tables
 */
public interface Table extends ApplicationEntity {

  /**
   * @return seatsNumber.
   */
  public Integer getSeatsNumber();

  /**
   * @param seatsNumber new seatsNumber to set.
   */
  public void setSeatsNumber(Integer seatsNumber);

}
