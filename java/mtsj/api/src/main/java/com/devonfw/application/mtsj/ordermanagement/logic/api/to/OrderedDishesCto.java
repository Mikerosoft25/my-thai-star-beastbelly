package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishEto;
import com.devonfw.module.basic.common.api.to.AbstractCto;

/**
 * Composite transport object of OrderedDishes
 */
public class OrderedDishesCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private DishEto dish;

  private OrderedDishesEto orderedDishes;

  /**
   * @return the {@link OrderedDishesEto}.
   */
  public OrderedDishesEto getOrderedDishes() {

    return this.orderedDishes;
  }

  /**
   * @param orderedDishes the {@link OrderedDishesEto} to set.
   */
  public void setOrderedDishes(OrderedDishesEto orderedDishes) {

    this.orderedDishes = orderedDishes;
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
}
