package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import com.devonfw.application.mtsj.ordermanagement.common.api.OrderPayStatus;
import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * Entity transport object of OrderPayStatus
 */
public class OrderPayStatusEto extends AbstractEto implements OrderPayStatus {

  private static final long serialVersionUID = 1L;

  private String payStatus;

  @Override
  public String getPayStatus() {

    return this.payStatus;
  }

  @Override
  public void setPayStatus(String payStatus) {

    this.payStatus = payStatus;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.payStatus == null) ? 0 : this.payStatus.hashCode());
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
    OrderPayStatusEto other = (OrderPayStatusEto) obj;
    if (this.payStatus == null) {
      if (other.payStatus != null) {
        return false;
      }
    } else if (!this.payStatus.equals(other.payStatus)) {
      return false;
    }
    return true;
  }
}
