package com.devonfw.application.mtsj.ordermanagement.dataaccess.api;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.ordermanagement.common.api.OrderStatus;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link OrderStatus}.
 */
@Entity
@Table(name = "OrderStatus")
public class OrderStatusEntity extends ApplicationPersistenceEntity implements OrderStatus {

  private String status;

  private static final long serialVersionUID = 1L;

  /**
   * @return status
   */
  @Override
  public String getStatus() {

    return this.status;
  }

  /**
   * @param status new value of {@link #getStatus}.
   */
  @Override
  public void setStatus(String status) {

    this.status = status;
  }

}