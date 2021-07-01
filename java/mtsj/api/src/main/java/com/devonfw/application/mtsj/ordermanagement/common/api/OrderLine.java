package com.devonfw.application.mtsj.ordermanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for OrderLines
 *
 */
public interface OrderLine extends ApplicationEntity {

  /**
   * @return the orderId
   */
  public Long getOrderId();

  /**
   * Set the orderId
   *
   * @param orderId the orderId
   */
  public void setOrderId(Long orderId);

  /**
   * @return the dishId
   */
  public Long getDishId();

  /**
   * Set the dishId
   *
   * @param dishId the dishId
   */
  public void setDishId(Long dishId);

  /**
   * @return the amount
   */
  public Integer getAmount();

  /**
   * Set the amount
   *
   * @param amount the amount
   */
  public void setAmount(Integer amount);

  /**
   * @return the comment
   */
  public String getComment();

  /**
   * Set the comment
   *
   * @param comment the comment
   */
  public void setComment(String comment);

  /**
   * @return true if deleted, otherwise false
   */
  public Boolean getDeleted();

  /**
   * Set if orderLine should be deleted
   *
   * @param deleted true if orderLine should be deleted
   */
  public void setDeleted(Boolean deleted);

}
