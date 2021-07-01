package com.devonfw.application.mtsj.ordermanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for OrderPayStatus
 */
public interface OrderPayStatus extends ApplicationEntity {

  /**
   * @return payStatusId
   */

  public String getPayStatus();

  /**
   * @param payStatus setter for payStatus attribute
   */
  public void setPayStatus(String payStatus);

}
