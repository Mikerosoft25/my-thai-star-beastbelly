package com.devonfw.application.mtsj.ordermanagement.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderedDishesCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderedDishesSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.service.api.rest.OrdermanagementRestService;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Ordermanagement}.
 */
@Named("OrdermanagementRestService")
public class OrdermanagementRestServiceImpl implements OrdermanagementRestService {

  @Inject
  private Ordermanagement ordermanagement;

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_ORDER)
  public OrderCto getOrder(long id) {

    return this.ordermanagement.findOrder(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CHANGE_ORDER_STATUS)
  public OrderEto changeOrderStatus(Long id, OrderStatusEto orderStatus) {

    return this.ordermanagement.changeOrderStatus(id, orderStatus);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CHANGE_ORDER_PAY_STATUS)
  public OrderEto changeOrderPayStatus(Long id, OrderPayStatusEto orderPayStatus) {

    return this.ordermanagement.changeOrderPayStatus(id, orderPayStatus);
  }

  @Override
  @PermitAll
  public OrderEto saveOrder(OrderCto order) {

    return this.ordermanagement.saveOrder(order);
  }

  @Override
  @PermitAll
  public OrderCto saveAlexaOrderFromHome(OrderCto order) {

    return this.ordermanagement.saveAlexaOrder(order, false, false);
  }

  @Override
  @PermitAll
  public OrderCto saveAlexaDeliveryOrderFromHome(OrderCto order) {

    return this.ordermanagement.saveAlexaOrder(order, false, true);
  }

  @Override
  @PermitAll
  public OrderCto saveAlexaOrderInHouse(OrderCto order) {

    return this.ordermanagement.saveAlexaOrder(order, true, false);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CHANGE_ORDER)
  public OrderCto changeOrder(OrderCto order) {

    return this.ordermanagement.changeOrder(order);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_ORDER)
  public boolean deleteOrder(long id) {

    return this.ordermanagement.deleteOrder(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CANCEL_ORDER)
  public OrderEto cancelOrder(Long id) {

    return this.ordermanagement.cancelOrder(id, true);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CANCEL_ORDER_BY_USER)
  public OrderEto cancelOrderByUser(Long id) {

    return this.ordermanagement.cancelOrder(id, false);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_ORDERS_BY_USER)
  public Page<OrderCto> findOrdersByUser(OrderSearchCriteriaTo searchCriteriaTo) {

    return this.ordermanagement.findOrdersByUser(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_ORDERS)
  public Page<OrderCto> findOrdersByPost(OrderSearchCriteriaTo searchCriteriaTo) {

    return this.ordermanagement.findOrdersByPost(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_ORDERLINE)
  public OrderLineEto getOrderLine(long id) {

    return this.ordermanagement.findOrderLine(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_ORDERLINE)
  public OrderLineEto saveOrderLine(OrderLineEto orderline) {

    return this.ordermanagement.saveOrderLine(orderline);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_ORDERLINE)
  public void deleteOrderLine(long id) {

    this.ordermanagement.deleteOrderLine(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_ORDERLINE)
  public Page<OrderLineCto> findOrderLinesByPost(OrderLineSearchCriteriaTo searchCriteriaTo) {

    return this.ordermanagement.findOrderLineCtos(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_ORDERED_DISHES)
  public Page<OrderedDishesCto> findOrderedDishes(OrderedDishesSearchCriteriaTo searchCriteriaTo) {

    return this.ordermanagement.findOrderedDishes(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_ORDER_STATUS)
  public OrderStatusEto getOrderStatus(long id) {

    return this.ordermanagement.findOrderStatusById(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_ORDER_STATUS)
  public Page<OrderStatusEto> findOrderStatus(OrderStatusSearchCriteriaTo searchCriteriaTo) {

    return this.ordermanagement.findOrderStatus(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_ORDER_PAY_STATUS)
  public OrderPayStatusEto getOrderPayStatus(long id) {

    return this.ordermanagement.findOrderPayStatusById(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_ORDER_PAY_STATUS)
  public Page<OrderPayStatusEto> findOrderPayStatuss(OrderPayStatusSearchCriteriaTo searchCriteriaTo) {

    return this.ordermanagement.findOrderPayStatus(searchCriteriaTo);
  }

}
