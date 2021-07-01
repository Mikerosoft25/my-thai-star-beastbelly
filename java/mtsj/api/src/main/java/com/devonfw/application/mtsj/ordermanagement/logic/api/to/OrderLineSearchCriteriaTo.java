package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * used to find {@link com.devonfw.application.mtsj.ordermanagement.common.api.OrderLine}s.
 *
 */
public class OrderLineSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Long orderId;

  private Long dishId;

  private Integer amount;

  private String comment;

  private StringSearchConfigTo commentOption;

  /**
   * The constructor.
   */
  public OrderLineSearchCriteriaTo() {

    super();
  }

  /**
   * @return commentOption
   */
  public StringSearchConfigTo getCommentOption() {

    return this.commentOption;
  }

  /**
   * @param commentOption new value of {@link #getCommentOption}.
   */
  public void setCommentOption(StringSearchConfigTo commentOption) {

    this.commentOption = commentOption;
  }

  /**
   * @return orderId
   */
  public Long getOrderId() {

    return this.orderId;
  }

  /**
   * @param orderId new value of {@link #getOrderId}.
   */
  public void setOrderId(Long orderId) {

    this.orderId = orderId;
  }

  /**
   * @return dishId
   */
  public Long getDishId() {

    return this.dishId;
  }

  /**
   * @param dishId new value of {@link #getDishId}.
   */
  public void setDishId(Long dishId) {

    this.dishId = dishId;
  }

  /**
   * @return amount
   */
  public Integer getAmount() {

    return this.amount;
  }

  /**
   * @param amount new value of {@link #getAmount}.
   */
  public void setAmount(Integer amount) {

    this.amount = amount;
  }

  /**
   * @return comment
   */
  public String getComment() {

    return this.comment;
  }

  /**
   * @param comment new value of {@link #getComment}.
   */
  public void setComment(String comment) {

    this.comment = comment;
  }

}
