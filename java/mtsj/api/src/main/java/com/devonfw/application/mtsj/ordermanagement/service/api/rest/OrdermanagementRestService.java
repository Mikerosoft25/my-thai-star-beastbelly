package com.devonfw.application.mtsj.ordermanagement.service.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;

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

/**
 * The service interface for REST calls in order to execute the logic of component {@link Ordermanagement}.
 */
@Path("/ordermanagement/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrdermanagementRestService {

  /**
   * Delegates to {@link Ordermanagement#findOrder}.
   *
   * @param id the ID of the {@link OrderEto}.
   *
   * @return the {@link OrderCto}.
   */
  @GET
  @Path("/order/{id}/")
  public OrderCto getOrder(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#changeOrderStatus}.
   *
   * @param id the ID of the {@link OrderEto} where to change the order-status.
   * @param orderStatus optional {@link OrderStatusEto} with an order-status to which the order will be changed.
   *
   * @return the {@link OrderEto} with the applied changes.
   */
  @POST
  @Path("/order/changestatus/{id}/")
  public OrderEto changeOrderStatus(@PathParam("id") Long id, OrderStatusEto orderStatus);

  /**
   * Delegates to {@link Ordermanagement#changeOrderPayStatus}.
   *
   * @param id the ID of the {@link OrderEto} where to change the order-pay-status.
   * @param orderPayStatus optional {@link OrderPayStatusEto} with an order-pay-status to which the order will be.
   *        changed.
   *
   * @return the {@link OrderEto} with the applied changes.
   */
  @POST
  @Path("/order/changepaystatus/{id}/")
  public OrderEto changeOrderPayStatus(@PathParam("id") Long id, OrderPayStatusEto orderPayStatus);

  /**
   * Delegates to {@link Ordermanagement#saveOrder}.
   *
   * @param order the {@link OrderCto} to be saved.
   *
   * @return the recently created {@link OrderEto}.
   */
  @POST
  @Path("/order/")
  public OrderEto saveOrder(OrderCto order);

  /**
   * Handles Alexa orders from home to eat at the restaurant. Delegates to {@link Ordermanagement#saveAlexaOrder} with
   * booleans "inHouse" and "deliveryOrder" set to false.
   *
   * @param order the {@link OrderCto} to be saved.
   *
   * @return the recently created {@link OrderEto}.
   */
  @POST
  @Path("/alexaorderfromhome")
  public OrderCto saveAlexaOrderFromHome(OrderCto order);

  /**
   * Handles Alexa orders from home that want the food delivered. Delegates to {@link Ordermanagement#saveAlexaOrder}
   * with boolean "inHouse" set to false and boolean "deliveryOrder" set to true.
   *
   * @param order the {@link OrderCto} to be saved.
   *
   * @return the recently created {@link OrderEto}.
   */
  @POST
  @Path("/alexadeliveryorderfromhome")
  public OrderCto saveAlexaDeliveryOrderFromHome(OrderCto order);

  /**
   * Handles Alexa orders in the restaurant. Delegates to {@link Ordermanagement#saveAlexaOrder} with boolean "inHouse"
   * set to true and boolean "deliveryOrder" set to false.
   *
   * @param order the {@link OrderCto} to be saved.
   *
   * @return the recently created {@link OrderEto}.
   */
  @POST
  @Path("/alexaorderinhouse")
  public OrderCto saveAlexaOrderInHouse(OrderCto order);

  /**
   * Delegates to {@link Ordermanagement#changeOrder}.
   *
   * @param order the {@link OrderCto} containing the changed order.
   *
   * @return the changed {@link OrderCto}.
   */
  @POST
  @Path("/order/changeorder")
  public OrderCto changeOrder(OrderCto order);

  /**
   * Delegates to {@link Ordermanagement#deleteOrder}.
   *
   * @param id ID of the {@link OrderEto} to be deleted.
   *
   * @return true if deleting was successful, otherwise false.
   */
  @DELETE
  @Path("/order/{id}/")
  public boolean deleteOrder(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#cancelOrder} with "authorized" set to true.
   *
   * @param id the ID of the {@link OrderEto} to cancel.
   *
   * @return the canceled {@link OrderEto}.
   */
  @POST
  @Path("/order/cancel/{id}/")
  public OrderEto cancelOrder(@PathParam("id") Long id);

  /**
   * Delegates to {@link Ordermanagement#cancelOrder} with "authorized" set to false.
   *
   * @param id the ID of the {@link OrderEto} to cancel.
   *
   * @return the canceled {@link OrderEto}.
   */
  @POST
  @Path("/order/cancelbyuser/{id}/")
  public OrderEto cancelOrderByUser(@PathParam("id") Long id);

  /**
   * Delegates to {@link Ordermanagement#findOrdersByUser}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding orders.
   * @return the {@link Page list} of matching {@link OrderCto}s.
   */
  @Path("/order/searchbyuser")
  @POST
  public Page<OrderCto> findOrdersByUser(OrderSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Ordermanagement#findOrdersByPost}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding orders.
   * @return the {@link Page list} of matching {@link OrderCto}s.
   */
  @Path("/order/search")
  @POST
  public Page<OrderCto> findOrdersByPost(OrderSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Ordermanagement#findOrderLine}.
   *
   * @param id the ID of the {@link OrderLineEto}.
   * @return the {@link OrderLineEto}.
   */
  @GET
  @Path("/orderline/{id}/")
  public OrderLineEto getOrderLine(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#saveOrderLine}.
   *
   * @param orderline the {@link OrderLineEto} to be saved.
   * @return the recently created {@link OrderLineEto}.
   */
  @POST
  @Path("/orderline/")
  public OrderLineEto saveOrderLine(OrderLineEto orderline);

  /**
   * Delegates to {@link Ordermanagement#deleteOrderLine}.
   *
   * @param id ID of the {@link OrderLineEto} to be deleted.
   */
  @DELETE
  @Path("/orderline/{id}/")
  public void deleteOrderLine(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#findOrderLineCtos}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding orderlines.
   * @return the {@link Page list} of matching {@link OrderLineCto}s.
   */
  @Path("/orderline/search")
  @POST
  public Page<OrderLineCto> findOrderLinesByPost(OrderLineSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Ordermanagement#findOrderedDishes}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding orderDishes.
   * @return the {@link Page list} of matching {@link OrderedDishesCto}s.
   */
  @Path("/ordereddishes/history")
  @POST
  public Page<OrderedDishesCto> findOrderedDishes(OrderedDishesSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Ordermanagement#findOrderStatus}.
   *
   * @param id the ID of the {@link OrderStatusEto}.
   * @return the {@link OrderStatusEto}.
   */
  @GET
  @Path("/orderstatus/{id}/")
  public OrderStatusEto getOrderStatus(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#findOrderStatus}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding orderStatuses.
   * @return the {@link Page list} of matching {@link OrderStatusEto}s.
   */
  @POST
  @Path("/orderstatus/search")
  public Page<OrderStatusEto> findOrderStatus(OrderStatusSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Ordermanagement#findOrderPayStatus}.
   *
   * @param id the ID of the {@link OrderPayStatusEto}.
   * @return the {@link OrderPayStatusEto}.
   */
  @GET
  @Path("/orderpaystatus/{id}/")
  public OrderPayStatusEto getOrderPayStatus(@PathParam("id") long id);

  /**
   * Delegates to {@link Ordermanagement#findOrderPayStatus}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding orderPayStatuses.
   * @return the {@link Page list} of matching {@link OrderPayStatusEto}s.
   */
  @Path("/orderpaystatus/search")
  @POST
  public Page<OrderPayStatusEto> findOrderPayStatuss(OrderPayStatusSearchCriteriaTo searchCriteriaTo);

}
