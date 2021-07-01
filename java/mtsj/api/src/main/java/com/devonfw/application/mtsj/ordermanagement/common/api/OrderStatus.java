package com.devonfw.application.mtsj.ordermanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for OrderStatus
 */
public interface OrderStatus extends ApplicationEntity {

  /**
   * @return status
   */
  public String getStatus();

  /**
   * @param status setter for status attribute
   */
  public void setStatus(String status);

}
