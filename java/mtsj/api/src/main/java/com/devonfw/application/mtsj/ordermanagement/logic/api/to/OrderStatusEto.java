package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import com.devonfw.application.mtsj.ordermanagement.common.api.OrderStatus;
import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * Entity transport object of OrderStatus
 */
public class OrderStatusEto extends AbstractEto implements OrderStatus {

  private static final long serialVersionUID = 1L;

  private String status;

  @Override
  public String getStatus() {

    return this.status;
  }

  @Override
  public void setStatus(String status) {

    this.status = status;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    // class check will be done by super type EntityTo!
    if (!super.equals(obj)) {
      return false;
    }
    OrderStatusEto other = (OrderStatusEto) obj;
    if (this.status == null) {
      if (other.status != null) {
        return false;
      }
    } else if (!this.status.equals(other.status)) {
      return false;
    }
    return true;
  }

}
