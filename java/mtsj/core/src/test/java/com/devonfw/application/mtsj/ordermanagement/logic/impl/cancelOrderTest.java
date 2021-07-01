package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.general.common.TestUtil;
import com.devonfw.application.mtsj.general.common.api.constants.Roles;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusEto;

/**
 * Unit Tests for {@link Ordermanagement#cancelOrder}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class cancelOrderTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  @Inject
  private BookingRepository bookingDAO;

  private String token = "CB_20210826_abcdefgh";

  /**
   * Creates a new booking before each test.
   */
  @BeforeEach
  public void startup() {

    BookingEntity booking = new BookingEntity();

    booking.setName("testUser");
    booking.setBookingToken(this.token);
    booking.setComment("Test Cases Booking");
    booking.setEmail("testCaseMail@mail.com");
    booking.setBookingDate(Instant.now().plus(24, ChronoUnit.HOURS));
    booking.setExpirationDate(Instant.now().plus(23, ChronoUnit.HOURS));
    booking.setCreationDate(Instant.now());
    booking.setCanceled(false);
    booking.setBookingType(BookingType.COMMON);
    booking.setTableId(3L);
    booking.setOrderId(null);
    booking.setAssistants(4);

    this.bookingDAO.save(booking);

  }

  /**
   * Deletes all orders and bookings after each test.
   */
  @AfterEach
  public void teardown() {

    List<OrderCto> orders = this.ordermanagement.findOrdersByBookingToken(this.token);

    for (OrderCto order : orders) {
      this.ordermanagement.deleteOrder(order.getOrder().getId());
    }

    BookingEntity booking = this.bookingDAO.findBookingByToken(this.token);

    this.bookingDAO.delete(booking);

  }

  /**
   * Tests that an order with the status 'Received' can be canceled by the waiter.
   */
  @Test
  public void cancelOrderByWaiterValidWithReceivedStatus() {

    // given

    OrderCto orderCto = buildOrderCto(this.token);
    OrderEto order = this.ordermanagement.saveOrder(orderCto);

    // when
    OrderEto canceledOrder = this.ordermanagement.cancelOrder(order.getId(), true);
    OrderEto canceledOrderFromDB = this.ordermanagement.findOrder(order.getId()).getOrder();

    // then
    assertThat(canceledOrder.getOrderStatusId()).isEqualTo(4L);
    assertThat(canceledOrder.getOrderPayStatusId()).isEqualTo(2L);

    assertThat(canceledOrderFromDB.getOrderStatusId()).isEqualTo(4L);
    assertThat(canceledOrderFromDB.getOrderPayStatusId()).isEqualTo(2L);

  }

  /**
   * Tests that an order with the status 'Preparation' can be canceled by the waiter.
   */
  @Test
  public void cancelOrderByWaiterValidWithPreperationStatus() {

    // given

    OrderCto orderCto = buildOrderCto(this.token);
    OrderEto order = this.ordermanagement.saveOrder(orderCto);

    // changing the orderStatus to 'Preparation'
    OrderStatusEto orderStatus = new OrderStatusEto();
    orderStatus.setId(1L);
    this.ordermanagement.changeOrderStatus(order.getId(), orderStatus);

    // when
    OrderEto canceledOrder = this.ordermanagement.cancelOrder(order.getId(), true);
    OrderEto canceledOrderFromDB = this.ordermanagement.findOrder(order.getId()).getOrder();

    // then
    assertThat(canceledOrder.getOrderStatusId()).isEqualTo(4L);
    assertThat(canceledOrder.getOrderPayStatusId()).isEqualTo(2L);

    assertThat(canceledOrderFromDB.getOrderStatusId()).isEqualTo(4L);
    assertThat(canceledOrderFromDB.getOrderPayStatusId()).isEqualTo(2L);

  }

  /**
   * Tests that an order with the status 'Delivery' can be canceled by the waiter.
   */
  @Test
  public void cancelOrderByWaiterValidWithDeliveryStatus() {

    // given
    OrderCto orderCto = buildOrderCto(this.token);
    OrderEto order = this.ordermanagement.saveOrder(orderCto);

    // changing the orderStatus to 'Delivery'
    OrderStatusEto orderStatus = new OrderStatusEto();
    orderStatus.setId(2L);
    this.ordermanagement.changeOrderStatus(order.getId(), orderStatus);

    // when
    OrderEto canceledOrder = this.ordermanagement.cancelOrder(order.getId(), true);
    OrderEto canceledOrderFromDB = this.ordermanagement.findOrder(order.getId()).getOrder();

    // then
    assertThat(canceledOrder.getOrderStatusId()).isEqualTo(4L);
    assertThat(canceledOrder.getOrderPayStatusId()).isEqualTo(2L);

    assertThat(canceledOrderFromDB.getOrderStatusId()).isEqualTo(4L);
    assertThat(canceledOrderFromDB.getOrderPayStatusId()).isEqualTo(2L);

  }

  /**
   * Tests that a customer can cancel his own order.
   */
  @Test
  public void cancelOrderByCustomerValid() {

    // given

    // same user as in the booking name
    TestUtil.login("testUser", Roles.CUSTOMER);

    OrderCto orderCto = buildOrderCto(this.token);
    OrderEto order = this.ordermanagement.saveOrder(orderCto);

    // when
    OrderEto canceledOrder = this.ordermanagement.cancelOrder(order.getId(), false);
    OrderEto canceledOrderFromDB = this.ordermanagement.findOrder(order.getId()).getOrder();

    // then
    assertThat(canceledOrder.getOrderStatusId()).isEqualTo(4L);
    assertThat(canceledOrder.getOrderPayStatusId()).isEqualTo(2L);

    assertThat(canceledOrderFromDB.getOrderStatusId()).isEqualTo(4L);
    assertThat(canceledOrderFromDB.getOrderPayStatusId()).isEqualTo(2L);

    TestUtil.logout();

  }

  /**
   * Tests that a customer cannot cancel an order that is not received.
   */
  @Test
  public void cancelOrderByCustomerInvalidStatusIsNotReceived() {

    // same user as in the booking name
    TestUtil.login("testUser", Roles.CUSTOMER);

    OrderCto orderCto = buildOrderCto(this.token);
    OrderEto order = this.ordermanagement.saveOrder(orderCto);

    // changing the orderStatus to 'Preparation'
    OrderStatusEto orderStatus = new OrderStatusEto();
    orderStatus.setId(1L);
    this.ordermanagement.changeOrderStatus(order.getId(), orderStatus);

    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(order.getId(), false);
    }).isInstanceOf(RuntimeException.class);

    TestUtil.logout();

  }

  /**
   * Tests that a customer cannot cancel the order of other customers.
   */
  @Test
  public void cancelOrderByCustomerInvalidOrderIsFromAnotherUser() {

    // wrong user, is not the creator of the order
    TestUtil.login("notTheRightUser", Roles.CUSTOMER);

    OrderCto orderCto = buildOrderCto(this.token);
    OrderEto order = this.ordermanagement.saveOrder(orderCto);

    // then
    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(order.getId(), false);
    }).isInstanceOf(RuntimeException.class);

    TestUtil.logout();

  }

  /**
   * Tests that canceling an order with an invalid orderId causes an exception.
   */
  @Test
  public void cancelOrderByWaiterInvalidOrderId() {

    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(-1L, true);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that an exception is thrown if null is given as parameter.
   */
  @Test
  public void cancelOrderByWaiterOrderIdNull() {

    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(null, true);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }

  /**
   * Tests that canceling an order by a customer with an invalidId causes an exception.
   */
  @Test
  public void cancelOrderByCustomerInvalidOrderId() {

    TestUtil.login("testUser", Roles.CUSTOMER);

    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(-1L, false);
    }).isInstanceOf(EmptyResultDataAccessException.class);

    TestUtil.logout();
  }

  /**
   * Tests that an exception is thrown if null is given as parameter by a customer.
   */
  @Test
  public void cancelOrderByCustomerOrderIdNull() {

    TestUtil.login("testUser", Roles.CUSTOMER);

    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(null, false);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);

    TestUtil.logout();
  }

  /**
   * Tests that an exception is thrown if null is given as parameter by a customer.
   */
  @Test
  public void cancelOrderByCustomerOrderAlreadyCompleted() {

    TestUtil.login("testUser", Roles.CUSTOMER);

    OrderCto orderCto = buildOrderCto(this.token);

    OrderEto savedOrder = this.ordermanagement.saveOrder(orderCto);

    // changing the orderStatus to 'Complete'
    OrderStatusEto orderStatus = new OrderStatusEto();
    orderStatus.setId(3L);
    this.ordermanagement.changeOrderStatus(savedOrder.getId(), orderStatus);

    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(savedOrder.getId(), false);
    }).isInstanceOf(RuntimeException.class);

    TestUtil.logout();
  }

  /**
   * Tests that an exception is thrown if a waiter tries to cancel an already completed order.
   */
  @Test
  public void cancelOrderByWaiterOrderAlreadyCompleted() {

    OrderCto orderCto = buildOrderCto(this.token);

    OrderEto savedOrder = this.ordermanagement.saveOrder(orderCto);

    // changing the orderStatus to 'Complete'
    OrderStatusEto orderStatus = new OrderStatusEto();
    orderStatus.setId(3L);
    this.ordermanagement.changeOrderStatus(savedOrder.getId(), orderStatus);

    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(savedOrder.getId(), false);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an exception is thrown if a customer tries to cancel an already canceled order.
   */
  @Test
  public void cancelOrderByCustomerOrderAlreadyCanceled() {

    TestUtil.login("testUser", Roles.CUSTOMER);

    OrderCto orderCto = buildOrderCto(this.token);

    OrderEto savedOrder = this.ordermanagement.saveOrder(orderCto);

    // canceling the order
    this.ordermanagement.cancelOrder(savedOrder.getId(), true);

    // trying to cancel again
    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(savedOrder.getId(), true);
    }).isInstanceOf(RuntimeException.class);

    TestUtil.logout();
  }

  /**
   * Tests that an exception is thrown if a waiter tries to cancel an already canceled order.
   */
  @Test
  public void cancelOrderByWaiterOrderAlreadyCanceled() {

    OrderCto orderCto = buildOrderCto(this.token);

    OrderEto savedOrder = this.ordermanagement.saveOrder(orderCto);

    // canceling the order
    this.ordermanagement.cancelOrder(savedOrder.getId(), true);

    // trying to cancel again
    assertThatThrownBy(() -> {
      this.ordermanagement.cancelOrder(savedOrder.getId(), true);
    }).isInstanceOf(RuntimeException.class);

  }

  /**
   * Builds an OrderCto with 1x THAI GREEN CHICKEN CURRY with Extras: Tofu and Curry and 2x Beer.
   */
  private OrderCto buildOrderCto(String bookingToken) {

    OrderCto orderCto = new OrderCto();

    BookingEto booking = new BookingEto();
    booking.setBookingToken(bookingToken);
    orderCto.setBooking(booking);

    List<OrderLineCto> orderLines = new ArrayList<>();

    // 1x THAI GREEN CHICKEN CURRY with Extras: Tofu and Curry
    OrderLineCto orderLineCto1 = new OrderLineCto();
    OrderLineEto orderLineEto1 = new OrderLineEto();
    List<IngredientEto> extras1 = new ArrayList<>();
    IngredientEto extra1 = new IngredientEto();
    IngredientEto extra2 = new IngredientEto();

    orderLineEto1.setDishId(2L);
    orderLineEto1.setAmount(1);
    orderLineEto1.setComment("test");

    extra1.setId(0L);
    extra2.setId(1L);

    extras1.add(extra1);
    extras1.add(extra2);

    orderLineCto1.setOrderLine(orderLineEto1);
    orderLineCto1.setExtras(extras1);

    // 2x Beer
    OrderLineCto orderLineCto2 = new OrderLineCto();
    OrderLineEto orderLineEto2 = new OrderLineEto();

    orderLineEto2.setDishId(5L);
    orderLineEto2.setAmount(2);
    orderLineEto2.setComment("beer");

    orderLineCto2.setOrderLine(orderLineEto2);

    // adding both to orderLines
    orderLines.add(orderLineCto1);
    orderLines.add(orderLineCto2);

    // adding orderLines to orderCto
    orderCto.setOrderLines(orderLines);

    return orderCto;
  }
}
