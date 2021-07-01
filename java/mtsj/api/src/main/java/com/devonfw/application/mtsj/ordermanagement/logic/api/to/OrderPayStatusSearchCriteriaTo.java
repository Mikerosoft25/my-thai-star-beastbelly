package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * {@link AbstractSearchCriteriaTo} to find instances of
 * {@link com.devonfw.application.mtsj.ordermanagement.common.api.OrderPayStatus}s.
 */
public class OrderPayStatusSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String payStatus;

  private StringSearchConfigTo payStatusOption;

  /**
   * @return payStatusId
   */

  public String getPayStatus() {

    return this.payStatus;
  }

  /**
   * @param payStatus setter for payStatus attribute
   */

  public void setPayStatus(String payStatus) {

    this.payStatus = payStatus;
  }

  /**
   * @return the {@link StringSearchConfigTo} used to search for {@link #getPayStatus() payStatus}.
   */
  public StringSearchConfigTo getPayStatusOption() {

    return this.payStatusOption;
  }

  /**
   * @param payStatusOption new value of {@link #getPayStatusOption()}.
   */
  public void setPayStatusOption(StringSearchConfigTo payStatusOption) {

    this.payStatusOption = payStatusOption;
  }

}
