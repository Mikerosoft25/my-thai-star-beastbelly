package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import java.util.List;

import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.module.basic.common.api.to.AbstractCto;

/**
 * Composite transport object of OrderLine
 */
public class OrderLineCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private OrderLineEto orderLine;

  private OrderEto order;

  private DishEto dish;

  private List<IngredientEto> extras;

  /**
   * @return the {@link OrderLineEto}.
   */
  public OrderLineEto getOrderLine() {

    return this.orderLine;
  }

  /**
   * @param orderLine the {@link OrderLineEto} to set.
   */
  public void setOrderLine(OrderLineEto orderLine) {

    this.orderLine = orderLine;
  }

  /**
   * @return the {@link OrderEto}.
   */
  public OrderEto getOrder() {

    return this.order;
  }

  /**
   * @param order the {@link OrderEto} to set.
   */
  public void setOrder(OrderEto order) {

    this.order = order;
  }

  /**
   * @return the {@link DishEto}.
   */
  public DishEto getDish() {

    return this.dish;
  }

  /**
   * @param dish the {@link DishEto} to set.
   */
  public void setDish(DishEto dish) {

    this.dish = dish;
  }

  /**
   * @return the {@link List} of {@link IngredientEto}.
   */
  public List<IngredientEto> getExtras() {

    return this.extras;
  }

  /**
   * @param extras the {@link List} of {@link IngredientEto} to set.
   */
  public void setExtras(List<IngredientEto> extras) {

    this.extras = extras;
  }

}
