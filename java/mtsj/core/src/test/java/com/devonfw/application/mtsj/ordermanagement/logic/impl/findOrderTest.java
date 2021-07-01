package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;

/**
 * Unit Tests for {@link Ordermanagement#findOrder(Long)}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findOrderTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  @Inject
  private BookingRepository bookingDAO;

  private String token = "CB_20210826_abcdefgh";

  private Long orderId;

  /**
   * Creates a new booking and order before all test.
   */
  @BeforeAll
  public void startup() {

    // Saving booking:

    BookingEntity bookingEntity = new BookingEntity();

    bookingEntity.setName("Test Case");
    bookingEntity.setBookingToken(this.token);
    bookingEntity.setComment("Test Cases Booking");
    bookingEntity.setEmail("testCaseMail@mail.com");
    bookingEntity.setBookingDate(Instant.now().plus(24, ChronoUnit.HOURS));
    bookingEntity.setExpirationDate(Instant.now().plus(23, ChronoUnit.HOURS));
    bookingEntity.setCreationDate(Instant.now());
    bookingEntity.setCanceled(false);
    bookingEntity.setBookingType(BookingType.COMMON);
    bookingEntity.setTableId(3L);
    bookingEntity.setOrderId(null);
    bookingEntity.setAssistants(4);

    this.bookingDAO.save(bookingEntity);

    // Saving order:

    OrderCto orderCto = new OrderCto();

    BookingEto booking = new BookingEto();
    booking.setBookingToken(this.token);
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

    OrderEto savedOrder = this.ordermanagement.saveOrder(orderCto);
    this.orderId = savedOrder.getId();

  }

  /**
   * Deletes all orders and bookings after all test.
   */
  @AfterAll
  public void teardown() {

    List<OrderCto> orders = this.ordermanagement.findOrdersByBookingToken(this.token);

    for (OrderCto order : orders) {
      this.ordermanagement.deleteOrder(order.getOrder().getId());
    }

    BookingEntity booking = this.bookingDAO.findBookingByToken(this.token);

    this.bookingDAO.delete(booking);

  }

  /**
   * Tests that an order with an invalid token will not be stored.
   */
  @Test
  public void findOrderValid() {

    // when
    OrderCto orderCto = this.ordermanagement.findOrder(this.orderId);

    OrderEto orderEto = orderCto.getOrder();
    BookingEto bookingEto = orderCto.getBooking();
    List<OrderLineCto> orderLines = orderCto.getOrderLines();

    OrderLineCto orderLineCtoChicken = orderLines.get(0);
    OrderLineEto orderLineEtoChicken = orderLineCtoChicken.getOrderLine();
    List<IngredientEto> extrasChicken = orderLineCtoChicken.getExtras();

    OrderLineCto orderLineCtoBeer = orderLines.get(1);
    OrderLineEto orderLineEtoBeer = orderLineCtoBeer.getOrderLine();
    List<IngredientEto> extrasBeer = orderLineCtoBeer.getExtras();

    // then

    assertThat(orderEto.getId()).isEqualTo(this.orderId);
    assertThat(bookingEto.getBookingToken()).isEqualTo(this.token);

    assertThat(orderLineEtoChicken.getDishId()).isEqualTo(2L);
    assertThat(orderLineEtoChicken.getAmount()).isEqualTo(1);
    assertThat(orderLineEtoChicken.getComment()).isEqualTo("test");
    assertThat(extrasChicken).hasSize(2);

    assertThat(orderLineEtoBeer.getDishId()).isEqualTo(5L);
    assertThat(orderLineEtoBeer.getAmount()).isEqualTo(2);
    assertThat(orderLineEtoBeer.getComment()).isEqualTo("beer");
    assertThat(extrasBeer).isEmpty();
  }

  /**
   * Tests that an order cannot be saved if an order to a booking already exists.
   */
  @Test
  public void findOrderWithInvalidId() {

    assertThatThrownBy(() -> {
      this.ordermanagement.findOrder(-1L);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that an error occurs when giving null as a parameter.
   */
  @Test
  public void findOrderWithIdNull() {

    assertThatThrownBy(() -> {
      this.ordermanagement.saveOrder(null);
    }).isInstanceOf(NullPointerException.class);

  }
}
