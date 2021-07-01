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

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.OrderAlreadyExistException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.WrongTokenException;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;

/**
 * Unit Tests for {@link Ordermanagement#saveOrder(OrderCto)}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class saveOrderTest extends ApplicationComponentTest {

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

    booking.setName("Test Case");
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
   * Tests that a correct order is saved without errors.
   */
  @Test
  public void saveOrderValid() {

    // given

    OrderCto orderCto = buildOrderCto(this.token);

    // when
    OrderEto order = this.ordermanagement.saveOrder(orderCto);

    // then
    assertThat(order).isNotNull();

  }

  /**
   * Tests that saving an order to a booking is possible if a previous order to the same booking has been caneled.
   */
  @Test
  public void saveOrderValidAfterPreviousOrderCanceled() {

    // given
    OrderCto orderCto = buildOrderCto(this.token);

    // when
    OrderEto order = this.ordermanagement.saveOrder(orderCto);
    this.ordermanagement.cancelOrder(order.getId(), true);

    OrderEto anotherOrder = this.ordermanagement.saveOrder(orderCto);

    // then
    assertThat(anotherOrder).isNotNull();
  }

  /**
   * Tests that an order with an invalid token will not be stored.
   */
  @Test
  public void saveOrderOrderAlreadyExists() {

    // given

    OrderCto orderCto = buildOrderCto(this.token);

    this.ordermanagement.saveOrder(orderCto);

    // second order

    OrderCto orderCto2 = buildOrderCto(this.token);

    // then

    assertThatThrownBy(() -> {
      this.ordermanagement.saveOrder(orderCto2);
    }).isInstanceOf(OrderAlreadyExistException.class);

  }

  /**
   * Tests that an order cannot be saved if an order to a booking already exists.
   */
  @Test
  public void saveOrderWithInvalidToken() {

    // given

    OrderCto orderCto = buildOrderCto("invalidToken");

    // then
    assertThatThrownBy(() -> {
      this.ordermanagement.saveOrder(orderCto);
    }).isInstanceOf(WrongTokenException.class);

  }

  /**
   * Tests that an error occurs when giving null as a parameter.
   */
  @Test
  public void saveOrderWithNull() {

    assertThatThrownBy(() -> {
      this.ordermanagement.saveOrder(null);
    }).isInstanceOf(NullPointerException.class);

  }

  /**
   * Builds an OrderCto with 1x THAI GREEN CHICKEN CURRY with Extras: Tofu and Curry and 2x Beer
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
