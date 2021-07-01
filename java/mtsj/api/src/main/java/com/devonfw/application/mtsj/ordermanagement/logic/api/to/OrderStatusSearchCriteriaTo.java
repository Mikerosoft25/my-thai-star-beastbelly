package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * {@link AbstractSearchCriteriaTo} to find instances of
 * {@link com.devonfw.application.mtsj.ordermanagement.common.api.OrderStatus}s.
 */
public class OrderStatusSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String status;

  private StringSearchConfigTo statusOption;

  /**
   * @return statusId
   */
  public String getStatus() {

    return this.status;
  }

  /**
   * @param status setter for status attribute
   */
  public void setStatus(String status) {

    this.status = status;
  }

  /**
   * @return the {@link StringSearchConfigTo} used to search for {@link #getStatus() status}.
   */
  public StringSearchConfigTo getStatusOption() {

    return this.statusOption;
  }

  /**
   * @param statusOption new value of {@link #getStatusOption()}.
   */
  public void setStatusOption(StringSearchConfigTo statusOption) {

    this.statusOption = statusOption;
  }

}
