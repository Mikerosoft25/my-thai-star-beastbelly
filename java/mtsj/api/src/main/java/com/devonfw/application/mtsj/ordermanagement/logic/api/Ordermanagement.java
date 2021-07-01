package com.devonfw.application.mtsj.ordermanagement.logic.api;

import java.util.List;

import org.springframework.data.domain.Page;

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
 * Interface for Ordermanagement component.
 */
public interface Ordermanagement {

  /**
   * Returns a Order by its id 'id'.
   *
   * @param id The id 'id' of the Order.
   * @return The {@link OrderCto} with id 'id'.
   */
  OrderCto findOrder(Long id);

  /**
   * Changes the order-status of an order with the given id
   *
   * @param id the ID of the {@link OrderEto} where to change the order-status.
   * @param orderStatus optional {@link OrderStatusEto} with an order-status to which the order will be changed.
   *
   *
   * @return the {@link OrderEto} with the applied changes.
   */
  OrderEto changeOrderStatus(Long id, OrderStatusEto orderStatus);

  /**
   * Changes the order-pay-status of an order with the given id
   *
   * @param id the ID of the {@link OrderEto} where to change the order-pay-status.
   * @param orderPayStatus optional {@link OrderPayStatusEto} with an order-pay-status to which the order will be
   *        changed.
   *
   * @return the {@link OrderEto} with the applied changes.
   */
  OrderEto changeOrderPayStatus(Long id, OrderPayStatusEto orderPayStatus);

  /**
   * Returns a paginated list of Orders matching the search criteria. Shows only orders related to the user defined in
   * the bearer token in the header of the http request.
   *
   *
   * @param criteria the {@link OrderSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderCto}s.
   */
  public Page<OrderCto> findOrdersByUser(OrderSearchCriteriaTo criteria);

  /**
   * Returns a paginated list of Orders matching the search criteria. Needs Authorization.
   *
   * @param criteria the {@link OrderSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderCto}s.
   */
  Page<OrderCto> findOrdersByPost(OrderSearchCriteriaTo criteria);

  /**
   * Returns a paginated list of Orders matching the search criteria.
   *
   * @param criteria the {@link OrderSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderCto}s.
   */
  Page<OrderCto> findOrderCtos(OrderSearchCriteriaTo criteria);

  /**
   * Returns the list of OrderCto by invitedGuestId
   *
   * @param invitedGuestId Id of the invited guest
   * @return the list of {@link OrderCto}s
   */
  List<OrderCto> findOrdersByInvitedGuest(Long invitedGuestId);

  /**
   * Returns the list of OrderCtos by bookingToken.
   *
   * @param bookingToken The bookingToken.
   * @return the list of {@link OrderCto}s.
   */
  List<OrderCto> findOrdersByBookingToken(String bookingToken);

  /**
   * Returns the list of OrderCtos by bookingId.
   *
   * @param idBooking ID of the booking.
   * @return the list of {@link OrderCto}s.
   */
  List<OrderCto> findOrders(Long idBooking);

  /**
   * Deletes an order from the database by its id 'orderId'.
   *
   * @param orderId Id of the order to delete.
   * @return boolean <code>true</code> if the order can be deleted, <code>false</code> otherwise.
   */
  boolean deleteOrder(Long orderId);

  /**
   * Cancels an order
   *
   * @param id Id of the order to cancel.
   * @param authorized Whether the cancellation is done by a waiter or a higher role or not.
   *
   * @return the modified {@link OrderEto}.
   */
  OrderEto cancelOrder(Long id, boolean authorized);

  /**
   * Saves an order and stores it in the database.
   *
   * @param order the {@link OrderEto} to create.
   * @return the new {@link OrderEto} that has been saved with ID and version.
   */
  OrderEto saveOrder(OrderCto order);

  /**
   * Saves an Alexa order and stores it in the database.
   *
   * @param order the {@link OrderCto} to create.
   * @param inHouse Whether the order is made inside the restaurant at a table.
   * @param deliveryOrder Whether the order is a a delivery.
   *
   * @return the new {@link OrderCto} that has been saved with ID and version.
   */
  OrderCto saveAlexaOrder(OrderCto order, boolean inHouse, boolean deliveryOrder);

  /**
   * Changes an existing order and stores the changes in the database.
   *
   * @param order the {@link OrderCto} containing the changed order.
   *
   * @return the changed {@link OrderCto}.
   */
  OrderCto changeOrder(OrderCto order);

  /**
   * Returns an OrderLine by its id 'id'.
   *
   * @param id The id 'id' of the OrderLine.
   * @return The {@link OrderLineEto} with id 'id'.
   */
  OrderLineEto findOrderLine(Long id);

  /**
   * Returns a paginated list of OrderLines matching the search criteria.
   *
   * @param criteria the {@link OrderLineSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderLineEto}s.
   */
  Page<OrderLineCto> findOrderLineCtos(OrderLineSearchCriteriaTo criteria);

  /**
   * Deletes an OrderLine from the database by its id 'orderLineId'.
   *
   * @param orderLineId Id of the orderLine to delete.
   * @return boolean <code>true</code> if the orderLine can be deleted, <code>false</code> otherwise.
   */
  boolean deleteOrderLine(Long orderLineId);

  /**
   * Saves an OrderLine and store it in the database.
   *
   * @param orderLine the {@link OrderLineEto} to create.
   * @return the new {@link OrderLineEto} that has been saved with ID and version.
   */
  OrderLineEto saveOrderLine(OrderLineEto orderLine);

  /**
   * Returns a paginated list of OrderDishes matching the search criteria.
   *
   * @param criteria the pagination and search criteria to be used for finding orderDishes.
   * @return the {@link Page list} of matching {@link OrderedDishesCto}s.
   */
  Page<OrderedDishesCto> findOrderedDishes(OrderedDishesSearchCriteriaTo criteria);

  /**
   * Returns a OrderStatus by its id 'id'.
   *
   * @param id The id 'id' of the OrderStatus.
   * @return The {@link OrderStatusEto} with id 'id'
   */
  OrderStatusEto findOrderStatusById(long id);

  /**
   * Returns a paginated list of OrderStatuss matching the search criteria.
   *
   * @param criteria the {@link OrderStatusSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderStatusEto}s.
   */
  Page<OrderStatusEto> findOrderStatus(OrderStatusSearchCriteriaTo criteria);

  /**
   * Returns a OrderPayStatus by its id 'id'.
   *
   * @param id The id 'id' of the OrderPayStatus.
   * @return The {@link OrderPayStatusEto} with id 'id'
   */
  OrderPayStatusEto findOrderPayStatusById(long id);

  /**
   * Returns a paginated list of OrderPayStatuss matching the search criteria.
   *
   * @param criteria the {@link OrderPayStatusSearchCriteriaTo}.
   * @return the {@link List} of matching {@link OrderPayStatusEto}s.
   */
  Page<OrderPayStatusEto> findOrderPayStatus(OrderPayStatusSearchCriteriaTo criteria);

}
