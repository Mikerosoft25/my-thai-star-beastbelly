package com.devonfw.application.mtsj.bookingmanagement.logic.api.to;

import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;

/**
 * used to find {@link com.devonfw.application.mtsj.bookingmanagement.common.api.Table}s.
 */
public class TableSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Integer seatsNumber;

  /**
   * The constructor.
   */
  public TableSearchCriteriaTo() {

    super();
  }

  /**
   * @return seatsNumber
   */
  public Integer getSeatsNumber() {

    return this.seatsNumber;
  }

  /**
   * @param seatsNumber new value of {@link #getSeatsNumber}.
   */
  public void setSeatsNumber(Integer seatsNumber) {

    this.seatsNumber = seatsNumber;
  }

}
