package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoBookingException;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;

/**
 * Test for {@link Ordermanagement#saveAlexaOrder}
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class saveAlexaOrderTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  @Inject
  private BookingRepository bookingDAO;

  private Long orderId;

  private String token = "CB_20210426_5fc8b3abcd";

  /**
   * @return the latest created order.
   */
  public OrderCto getOrderCto() {

    return this.ordermanagement.findOrder(this.orderId);
  }

  /**
   * Creates a booking before all tests.
   */
  @BeforeEach
  public void startup() {

    // Creating a new booking at the start

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

  // ============= Tests for: saveAlexaOrder() , orders from home but eating at the restaurant =============

  private OrderCto getAlexaOrderFromHomeOrderCto(String bookingEmail, Long dishId, Integer amount, String comment,
      Long extraId) {

    OrderCto orderCto = new OrderCto();

    BookingEto bookingEto = new BookingEto();
    bookingEto.setEmail(bookingEmail);

    orderCto.setBooking(bookingEto);

    List<OrderLineCto> lineCtos = new ArrayList<>();
    OrderLineCto lineCto = new OrderLineCto();
    OrderLineEto lineEto = new OrderLineEto();

    lineEto.setDishId(dishId);
    lineEto.setAmount(amount);
    lineEto.setComment(comment);

    lineCto.setOrderLine(lineEto);

    List<IngredientEto> extras = new ArrayList<>();

    IngredientEto extra = new IngredientEto();
    extra.setId(extraId);

    extras.add(extra);

    lineCto.setExtras(extras);

    lineCtos.add(lineCto);

    orderCto.setOrderLines(lineCtos);

    return orderCto;
  }

  private OrderCto getAlexaDeliveryOrderFromHomeOrderCto(String bookingEmail, String bookingName, Long dishId,
      Integer amount, String comment, Long extraId) {

    OrderCto orderCto = new OrderCto();

    BookingEto bookingEto = new BookingEto();
    bookingEto.setEmail(bookingEmail);
    bookingEto.setName(bookingName);

    orderCto.setBooking(bookingEto);

    List<OrderLineCto> lineCtos = new ArrayList<>();
    OrderLineCto lineCto = new OrderLineCto();
    OrderLineEto lineEto = new OrderLineEto();

    lineEto.setDishId(dishId);
    lineEto.setAmount(amount);
    lineEto.setComment(comment);

    lineCto.setOrderLine(lineEto);

    List<IngredientEto> extras = new ArrayList<>();

    IngredientEto extra = new IngredientEto();
    extra.setId(extraId);

    extras.add(extra);

    lineCto.setExtras(extras);

    lineCtos.add(lineCto);

    orderCto.setOrderLines(lineCtos);

    return orderCto;
  }

  private OrderCto getAlexaInHouseOrderCto(Long tableId, Long dishId, Integer amount, String comment, Long extraId) {

    OrderCto orderCto = new OrderCto();

    BookingEto bookingEto = new BookingEto();
    bookingEto.setTableId(tableId);

    orderCto.setBooking(bookingEto);

    List<OrderLineCto> lineCtos = new ArrayList<>();
    OrderLineCto lineCto = new OrderLineCto();
    OrderLineEto lineEto = new OrderLineEto();

    lineEto.setDishId(dishId);
    lineEto.setAmount(amount);
    lineEto.setComment(comment);

    lineCto.setOrderLine(lineEto);

    List<IngredientEto> extras = new ArrayList<>();

    IngredientEto extra = new IngredientEto();
    extra.setId(extraId);

    extras.add(extra);

    lineCto.setExtras(extras);

    lineCtos.add(lineCto);

    orderCto.setOrderLines(lineCtos);

    return orderCto;
  }

  /**
   * Tests that alexa orders from home are saved in the DB.
   */
  @Test
  public void saveAlexaOrderFromHomeValid() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("testCaseMail@mail.com", 2L, 5, "test", 0L);

    OrderCto resultOrder = this.ordermanagement.saveAlexaOrder(orderCto, false, false);

    assertNotNull(resultOrder);

    OrderCto savedOrder = this.ordermanagement.findOrder(resultOrder.getOrder().getId());

    BookingEto bookingEto = orderCto.getBooking();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);
    OrderLineEto orderLineEto = orderLineCto.getOrderLine();
    IngredientEto extra = orderLineCto.getExtras().get(0);

    BookingEto savedBookingEto = savedOrder.getBooking();
    OrderLineCto savedOrderLineCto = savedOrder.getOrderLines().get(0);
    OrderLineEto savedOrderLineEto = savedOrderLineCto.getOrderLine();
    IngredientEto savedExtra = savedOrderLineCto.getExtras().get(0);

    assertThat(savedBookingEto.getEmail()).isEqualTo(bookingEto.getEmail());
    assertThat(savedBookingEto.getBookingToken()).isEqualTo("CB_20210426_5fc8b3abcd");

    assertThat(savedOrderLineEto.getDishId()).isEqualTo(orderLineEto.getDishId());
    assertThat(savedOrderLineEto.getAmount()).isEqualTo(orderLineEto.getAmount());
    assertThat(savedOrderLineEto.getComment()).isEqualTo(orderLineEto.getComment());

    assertThat(savedExtra.getId()).isEqualTo(extra.getId());

  }

  /**
   * Tests that an alexa order from home with an invalid email causes an exception.
   */
  @Test
  public void saveAlexaOrderFromHomeBookingEmailInvalid() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("invalid@mail.com", 2L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, false);
    }).isInstanceOf(NoBookingException.class);
  }

  /**
   * Tests that an alexa order from home with null as email causes an exception.
   */
  @Test
  public void saveAlexaOrderFromHomeBookingEmailNull() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto(null, 2L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, false);
    }).isInstanceOf(NoBookingException.class);
  }

  /**
   * Tests that an alexa order from home with an invalid dishId causes an exception.
   */
  @Test
  public void saveAlexaOrderFromHomeOrderLineDishIdInvalid() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("testCaseMail@mail.com", -1L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, false);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa order from home with null as dishId causes an exception.
   */
  @Test
  public void saveAlexaOrderFromHomeOrderLineDishIdNull() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("testCaseMail@mail.com", null, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, false);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa order from home with an invalid orderLine amount causes an exception.
   */
  @Test
  public void saveAlexaOrderFromHomeOrderLineAmountInvalid() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("testCaseMail@mail.com", 2L, 0, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, false);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an alexa order from home with an orderLine amount of null causes an exception.
   */
  @Test
  public void saveAlexaOrderFromHomeOrderLineAmountNull() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("testCaseMail@mail.com", 2L, null, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, false);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that an alexa order from home with an orderLine amount of null causes an exception.
   */
  @Test
  public void saveAlexaOrderFromHomeExtraIdInvalid() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("testCaseMail@mail.com", 2L, 5, "test", -1L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, false);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa order from home with an extraId of null causes an exception.
   */
  @Test
  public void saveAlexaOrderFromHomeExtraIdNull() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("testCaseMail@mail.com", 2L, 5, "test", null);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, false);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }

  // Alexa Delivery Order from Home

  /**
   * Tests that a valid alexa delivery order from home is saved to the the DB.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeValid() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("testCaseMail@mail.com", "testCase", 2L, 5, "test", 0L);

    OrderCto resultOrder = this.ordermanagement.saveAlexaOrder(orderCto, false, true);

    assertNotNull(resultOrder);

    OrderCto savedOrder = this.ordermanagement.findOrder(resultOrder.getOrder().getId());

    BookingEto bookingEto = orderCto.getBooking();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);
    OrderLineEto orderLineEto = orderLineCto.getOrderLine();
    IngredientEto extra = orderLineCto.getExtras().get(0);

    BookingEto savedBookingEto = savedOrder.getBooking();
    OrderLineCto savedOrderLineCto = savedOrder.getOrderLines().get(0);
    OrderLineEto savedOrderLineEto = savedOrderLineCto.getOrderLine();
    IngredientEto savedExtra = savedOrderLineCto.getExtras().get(0);

    assertThat(savedBookingEto.getEmail()).isEqualTo(bookingEto.getEmail());
    assertThat(savedBookingEto.getName()).isEqualTo(bookingEto.getName());
    assertThat(savedBookingEto.getBookingType()).isEqualTo(BookingType.DELIVERY);

    assertThat(savedOrderLineEto.getDishId()).isEqualTo(orderLineEto.getDishId());
    assertThat(savedOrderLineEto.getAmount()).isEqualTo(orderLineEto.getAmount());
    assertThat(savedOrderLineEto.getComment()).isEqualTo(orderLineEto.getComment());

    assertThat(savedExtra.getId()).isEqualTo(extra.getId());
  }

  /**
   * Tests that an alexa delivery order from home with bookingName null causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeBookingNameNull() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("testCaseMail@mail.com", null, 2L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa delivery order from home with an invalid email causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeBookingEmailInvalid() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("invalidMail", "testCase", 2L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an alexa delivery order from home with null as email causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeBookingEmailNull() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto(null, "testCase", 2L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an alexa delivery order from home with an invalid dishId causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeOrderLineDishIdInvalid() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("testCaseMail@mail.com", "testCase", -1L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa delivery order from home with null as dishId causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeOrderLineDishIdNull() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("testCaseMail@mail.com", "testCase", null, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa delivery order from home with an invalid amount causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeOrderLineAmountInvalid() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("testCaseMail@mail.com", "testCase", 2L, 0, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an alexa delivery order from home with an invalid amount causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeOrderLineAmountNull() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("testCaseMail@mail.com", "testCase", 2L, null, "test",
        0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that an alexa delivery order from home with an invalid extraId causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeExtraIdInvalid() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("testCaseMail@mail.com", "testCase", 2L, 5, "test", 5L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa delivery order from home with null as an extraId causes an exception.
   */
  @Test
  public void saveAlexaDeliveryOrderFromHomeExtraIdNull() {

    OrderCto orderCto = getAlexaDeliveryOrderFromHomeOrderCto("testCaseMail@mail.com", "testCase", 2L, 5, "test", null);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, false, true);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }

  // Alexa inHouse orders

  /**
   * Tests that a valid alexa in house order is saved in the DB.
   */
  @Test
  public void saveAlexaInHouseOrderValid() {

    OrderCto orderCto = getAlexaInHouseOrderCto(1L, 2L, 5, "test", 0L);

    OrderCto resultOrder = this.ordermanagement.saveAlexaOrder(orderCto, true, false);

    assertNotNull(resultOrder);

    OrderCto savedOrder = this.ordermanagement.findOrder(resultOrder.getOrder().getId());

    BookingEto bookingEto = orderCto.getBooking();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);
    OrderLineEto orderLineEto = orderLineCto.getOrderLine();
    IngredientEto extra = orderLineCto.getExtras().get(0);

    BookingEto savedBookingEto = savedOrder.getBooking();
    OrderLineCto savedOrderLineCto = savedOrder.getOrderLines().get(0);
    OrderLineEto savedOrderLineEto = savedOrderLineCto.getOrderLine();
    IngredientEto savedExtra = savedOrderLineCto.getExtras().get(0);

    assertThat(savedBookingEto.getEmail()).isEqualTo(bookingEto.getEmail());
    assertThat(savedBookingEto.getName()).isEqualTo(bookingEto.getName());
    assertThat(savedBookingEto.getBookingType()).isEqualTo(BookingType.COMMON);
    assertThat(savedBookingEto.getTableId()).isEqualTo(bookingEto.getTableId());

    assertThat(savedOrderLineEto.getDishId()).isEqualTo(orderLineEto.getDishId());
    assertThat(savedOrderLineEto.getAmount()).isEqualTo(orderLineEto.getAmount());
    assertThat(savedOrderLineEto.getComment()).isEqualTo(orderLineEto.getComment());

    assertThat(savedExtra.getId()).isEqualTo(extra.getId());
  }

  /**
   * Tests that an alexa in house order with an invalid tableId causes an exception.
   */
  @Test
  public void saveAlexaInHouseOrderTableIdInvalid() {

    OrderCto orderCto = getAlexaInHouseOrderCto(-1L, 2L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, false);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa in house order with null as tableId causes an exception.
   */
  @Test
  public void saveAlexaInHouseOrderTableIdNull() {

    OrderCto orderCto = getAlexaInHouseOrderCto(null, 2L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, false);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an alexa in house order with an invalid dishId causes an exception.
   */
  @Test
  public void saveAlexaInHouseOrderDishIdInvalid() {

    OrderCto orderCto = getAlexaInHouseOrderCto(0L, -1L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, false);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa in house order with null as dishId causes an exception.
   */
  @Test
  public void saveAlexaInHouseOrderDishIdNull() {

    OrderCto orderCto = getAlexaInHouseOrderCto(0L, null, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, false);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa in house order with an invalid amount causes an exception.
   */
  @Test
  public void saveAlexaInHouseOrderAmountInvalid() {

    OrderCto orderCto = getAlexaInHouseOrderCto(0L, 2L, 0, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, false);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an alexa in house order with null as amount causes an exception.
   */
  @Test
  public void saveAlexaInHouseOrderAmountNull() {

    OrderCto orderCto = getAlexaInHouseOrderCto(0L, 2L, null, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, false);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that an alexa in house order with an invalid extraId causes an exception.
   */
  @Test
  public void saveAlexaInHouseOrderExtraInvalid() {

    OrderCto orderCto = getAlexaInHouseOrderCto(0L, 2L, 5, "test", -1L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, false);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa in house order with null as extraId causes an exception.
   */
  @Test
  public void saveAlexaInHouseOrderExtraNull() {

    OrderCto orderCto = getAlexaInHouseOrderCto(0L, 2L, 5, "test", null);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, false);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that an alexa order that is marked as inHouse and delivery causes an exception.
   */
  @Test
  public void saveAlexaOrderInvalidParameters() {

    OrderCto orderCto = getAlexaOrderFromHomeOrderCto("testCaseMail@mail.com", 2L, 5, "test", 0L);

    assertThatThrownBy(() -> {
      this.ordermanagement.saveAlexaOrder(orderCto, true, true);
    }).isInstanceOf(RuntimeException.class);
  }
}