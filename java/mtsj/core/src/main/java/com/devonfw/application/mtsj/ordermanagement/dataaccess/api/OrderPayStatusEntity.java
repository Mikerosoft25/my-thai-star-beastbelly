package com.devonfw.application.mtsj.ordermanagement.dataaccess.api;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.ordermanagement.common.api.OrderPayStatus;

/**
 * * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link OrderPayStatus}.
 */
@Entity
@Table(name = "OrderPayStatus")
public class OrderPayStatusEntity extends ApplicationPersistenceEntity implements OrderPayStatus {

  private String payStatus;

  private static final long serialVersionUID = 1L;

  /**
   * @return status
   */
  @Override
  public String getPayStatus() {

    return this.payStatus;
  }

  /**
   * @param payStatus new value of {@link #getPayStatus}.
   */
  @Override
  public void setPayStatus(String payStatus) {

    this.payStatus = payStatus;
  }
}
