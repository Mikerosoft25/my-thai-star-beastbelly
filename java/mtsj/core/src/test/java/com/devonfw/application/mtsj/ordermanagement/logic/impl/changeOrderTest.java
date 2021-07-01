package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.ChangingOrderPayStatusNotAllowedException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.ChangingOrderStatusNotAllowedException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.InvalidChangedOrderException;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusEto;

/**
 * Test for {@link Ordermanagement#changeOrder}, {@link Ordermanagement#changeOrderStatus} and
 * {@link Ordermanagement#changeOrderPayStatus}
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class changeOrderTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  @Inject
  private BookingRepository bookingDAO;

  private Long orderId;

  /**
   * @return the {@link OrderCto} that has been created before each test.
   */
  private OrderCto getOrderCto() {

    return this.ordermanagement.findOrder(this.orderId);
  }

  /**
   * Creates a booking before all tests.
   */
  @BeforeAll
  public void startup() {

    // Creating a new booking at the start

    BookingEntity booking = new BookingEntity();

    booking.setName("test");
    booking.setBookingToken("CB_20210426_5fc8b3a9467");
    booking.setComment("Test cases booking");
    booking.setEmail("test@d.de");
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
   * Creates an order before each tests linked to the above created booking.
   */
  @BeforeEach
  public void init() {

    OrderCto orderCto = new OrderCto();
    BookingEto bookingEto = new BookingEto();

    List<OrderLineCto> orderLineCtos = new ArrayList<>();
    OrderLineCto orderLineCto = new OrderLineCto();
    OrderLineEto orderLineEto = new OrderLineEto();
    OrderStatusEto orderStatusEto = new OrderStatusEto();
    OrderPayStatusEto orderPayStatusEto = new OrderPayStatusEto();
    List<IngredientEto> extraEtos = new ArrayList<>();
    IngredientEto extraEto = new IngredientEto();

    bookingEto.setBookingToken("CB_20210426_5fc8b3a9467");

    orderLineEto.setDishId(2L);
    orderLineEto.setAmount(1);

    extraEto.setId(0L);
    extraEtos.add(extraEto);

    orderStatusEto.setId(0L);
    orderPayStatusEto.setId(0L);

    orderLineCto.setOrderLine(orderLineEto);
    orderLineCto.setExtras(extraEtos);

    orderLineCtos.add(orderLineCto);

    orderCto.setOrderLines(orderLineCtos);
    orderCto.setBooking(bookingEto);
    orderCto.setOrderStatus(orderStatusEto);
    orderCto.setOrderPayStatus(orderPayStatusEto);

    OrderEto savedOrderCto = this.ordermanagement.saveOrder(orderCto);
    this.orderId = savedOrderCto.getId();
  }

  /**
   * Deletes the order after each test.
   */
  @AfterEach
  public void teardown() {

    this.ordermanagement.deleteOrder(this.orderId);
  }

  // ============= Tests for: changeOrder() =============

  /**
   * Tests that changing the orderLine of an order works.
   */
  @Test
  public void changeOrderChangeOrderLine() {

    OrderCto orderCto = getOrderCto();
    List<OrderLineCto> orderLineCtos = orderCto.getOrderLines();
    OrderLineEto orderLineEto = orderLineCtos.get(0).getOrderLine();

    Long newDishId = 0L;
    Integer newAmount = 50;
    String newComment = "testComment";

    assertThat(orderLineEto.getDishId()).isNotEqualTo(newDishId);
    assertThat(orderLineEto.getAmount()).isNotEqualTo(newAmount);
    assertThat(orderLineEto.getComment()).isNotEqualTo(newComment);

    orderLineEto.setDishId(newDishId);
    orderLineEto.setAmount(newAmount);
    orderLineEto.setComment(newComment);

    // Tests for the returned object of the method
    OrderCto resultOrderCto = this.ordermanagement.changeOrder(orderCto);
    OrderLineEto resultOrderLineEto = resultOrderCto.getOrderLines().get(0).getOrderLine();

    assertThat(resultOrderLineEto.getDishId()).isEqualTo(newDishId);
    assertThat(resultOrderLineEto.getAmount()).isEqualTo(newAmount);
    assertThat(resultOrderLineEto.getComment()).isEqualTo(newComment);

    // Tests that not only the method returns the right changed order but also the DB
    OrderCto resultOrderCtoFromDB = this.ordermanagement.findOrder(resultOrderCto.getOrder().getId());
    OrderLineEto resultOrderLineEtoFromDB = resultOrderCtoFromDB.getOrderLines().get(0).getOrderLine();

    assertThat(resultOrderLineEtoFromDB.getDishId()).isEqualTo(newDishId);
    assertThat(resultOrderLineEtoFromDB.getAmount()).isEqualTo(newAmount);
    assertThat(resultOrderLineEtoFromDB.getComment()).isEqualTo(newComment);

  }

  /**
   * Tests that deleting an orderLine of an order works.
   */
  @Test
  public void changeOrderDeleteOrderLine() {

    OrderCto orderCto = getOrderCto();
    List<OrderLineCto> orderLineCtos = orderCto.getOrderLines();
    OrderLineEto orderLineEto = orderLineCtos.get(0).getOrderLine();

    assertThat(orderLineEto.getDeleted()).satisfiesAnyOf(deleted -> assertThat(deleted).isNull(),
        deleted -> assertThat(deleted).isFalse());

    orderLineEto.setDeleted(true);

    // Tests for the returned object of the method
    OrderCto resultOrderCto = this.ordermanagement.changeOrder(orderCto);
    List<OrderLineCto> resultOrderLineCtos = resultOrderCto.getOrderLines();

    assertThat(resultOrderLineCtos).isEmpty();

    // Tests that not only the method returns the right changed order but also the DB
    OrderCto resultOrderCtoFromDB = this.ordermanagement.findOrder(resultOrderCto.getOrder().getId());
    List<OrderLineCto> resultOrderLineCtosFromDB = resultOrderCtoFromDB.getOrderLines();

    assertThat(resultOrderLineCtosFromDB).isEmpty();
  }

  /**
   * Tests that adding a new OrderLine with an extra works.
   */
  @Test
  public void changeOrderAddNewOrderLineWithNewExtra() {

    OrderCto orderCto = getOrderCto();
    List<OrderLineCto> orderLineCtos = orderCto.getOrderLines();

    List<IngredientEto> newExtraEtos = new ArrayList<>();
    OrderLineCto newOrderLineCto = new OrderLineCto();
    OrderLineEto newOrderLineEto = new OrderLineEto();
    IngredientEto newExtra = new IngredientEto();

    Long newDishId = 0L;
    Long newExtraId = 0L;
    Integer newAmount = 20;
    String newComment = "test";

    newOrderLineEto.setDishId(newDishId);
    newOrderLineEto.setAmount(newAmount);
    newOrderLineEto.setComment(newComment);

    newExtra.setId(newExtraId);
    newExtraEtos.add(newExtra);

    newOrderLineCto.setOrderLine(newOrderLineEto);
    newOrderLineCto.setExtras(newExtraEtos);

    assertThat(orderLineCtos.size()).isEqualTo(1);

    orderLineCtos.add(newOrderLineCto);

    orderCto.setOrderLines(orderLineCtos);

    // Tests for the returned object of the method
    OrderCto resultOrderCto = this.ordermanagement.changeOrder(orderCto);
    List<OrderLineCto> resultOrderLineCtos = resultOrderCto.getOrderLines();
    OrderLineEto resultOrderLineEto = resultOrderLineCtos.get(1).getOrderLine();
    IngredientEto resultExtra = resultOrderLineCtos.get(1).getExtras().get(0);

    assertThat(resultOrderLineCtos.size()).isEqualTo(2);
    assertThat(resultOrderLineEto.getDishId()).isEqualTo(newDishId);
    assertThat(resultOrderLineEto.getAmount()).isEqualTo(newAmount);
    assertThat(resultOrderLineEto.getComment()).isEqualTo(newComment);

    assertThat(resultExtra.getId()).isEqualTo(newExtraId);

    // Tests that not only the method returns the right changed order but also the DB
    OrderCto resultOrderCtoFromDB = this.ordermanagement.findOrder(resultOrderCto.getOrder().getId());
    List<OrderLineCto> resultOrderLineCtosFromDB = resultOrderCtoFromDB.getOrderLines();
    OrderLineEto resultOrderLineEtoFromDB = resultOrderLineCtosFromDB.get(1).getOrderLine();
    IngredientEto resultExtraFromDB = resultOrderLineCtosFromDB.get(1).getExtras().get(0);

    assertThat(resultOrderLineCtosFromDB.size()).isEqualTo(2);
    assertThat(resultOrderLineEtoFromDB.getDishId()).isEqualTo(newDishId);
    assertThat(resultOrderLineEtoFromDB.getAmount()).isEqualTo(newAmount);
    assertThat(resultOrderLineEtoFromDB.getComment()).isEqualTo(newComment);

    assertThat(resultExtraFromDB.getId()).isEqualTo(newExtraId);

  }

  /**
   * Tests that changing the the extra of an orderLine works.
   */
  @Test
  public void changeOrderChangeExtra() {

    OrderCto orderCto = getOrderCto();
    List<OrderLineCto> orderLineCtos = orderCto.getOrderLines();
    OrderLineCto orderLineCto = orderLineCtos.get(0);
    List<IngredientEto> exrtaEtos = orderLineCto.getExtras();
    IngredientEto extraEto = exrtaEtos.get(0);

    Long newExtraId = 1L;

    assertThat(extraEto.getId()).isNotEqualTo(newExtraId);

    extraEto.setId(newExtraId);

    // Tests for the returned object of the method
    OrderCto resultOrderCto = this.ordermanagement.changeOrder(orderCto);
    List<OrderLineCto> resultOrderLineCtos = resultOrderCto.getOrderLines();
    IngredientEto resultExtra = resultOrderLineCtos.get(0).getExtras().get(0);

    assertThat(resultExtra.getId()).isEqualTo(newExtraId);

    // Tests that not only the method returns the right changed order but also the DB
    OrderCto resultOrderCtoFromDB = this.ordermanagement.findOrder(resultOrderCto.getOrder().getId());
    List<OrderLineCto> resultOrderLineCtosFromDB = resultOrderCtoFromDB.getOrderLines();
    IngredientEto resultExtraFromDB = resultOrderLineCtosFromDB.get(0).getExtras().get(0);

    assertThat(resultExtraFromDB.getId()).isEqualTo(newExtraId);

  }

  /**
   * Tests that deleting an extra of an orderLine works.
   */
  @Test
  public void changeOrderDeleteExtra() {

    OrderCto orderCto = getOrderCto();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);

    assertThat(orderLineCto.getExtras().size()).isEqualTo(1);

    orderLineCto.setExtras(new ArrayList<>());

    // Tests for the returned object of the method
    OrderCto resultOrderCto = this.ordermanagement.changeOrder(orderCto);
    List<OrderLineCto> resultOrderLineCtos = resultOrderCto.getOrderLines();
    List<IngredientEto> extraEtos = resultOrderLineCtos.get(0).getExtras();

    assertThat(extraEtos.size()).isEqualTo(0);

    // Tests that not only the method returns the right changed order but also the DB
    OrderCto resultOrderCtoFromDB = this.ordermanagement.findOrder(resultOrderCto.getOrder().getId());
    List<OrderLineCto> resultOrderLineCtosFromDB = resultOrderCtoFromDB.getOrderLines();
    List<IngredientEto> extraEtosFromDB = resultOrderLineCtosFromDB.get(0).getExtras();

    assertThat(extraEtosFromDB.size()).isEqualTo(0);
  }

  /**
   * Tests that null as a parameter throws an exception.
   */
  @Test
  public void changeOrderWithNull() {

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(null);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that changing an order with an invalid id does not work.
   */
  @Test
  public void changeOrderWithInvalidOrderId() {

    OrderCto orderCto = getOrderCto();
    OrderEto orderEto = orderCto.getOrder();

    orderEto.setId(-1L);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that changing an order with order id null does not work.
   */
  @Test
  public void changeOrderWithOrderIdNull() {

    OrderCto orderCto = getOrderCto();
    OrderEto orderEto = orderCto.getOrder();

    orderEto.setId(null);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(InvalidChangedOrderException.class);
  }

  /**
   * Tests that adding an orderLine with invalid id does not work.
   */
  @Test
  public void changeOrderWithInvalidOrderLineId() {

    OrderCto orderCto = getOrderCto();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);

    OrderLineEto orderLineEto = orderLineCto.getOrderLine();

    orderLineEto.setId(-1L);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(InvalidChangedOrderException.class);
  }

  /**
   * Tests that adding an orderLine with invalid dishId does not work.
   */
  @Test
  public void changeOrderWithInvalidDishId() {

    OrderCto orderCto = getOrderCto();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);

    OrderLineEto orderLineEto = orderLineCto.getOrderLine();

    orderLineEto.setDishId(-1L);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that adding an orderLine with dishId null does not work.
   */
  @Test
  public void changeOrderWithDishIdNull() {

    OrderCto orderCto = getOrderCto();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);
    OrderLineEto orderLineEto = orderLineCto.getOrderLine();

    orderLineEto.setDishId(null);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(InvalidChangedOrderException.class);
  }

  /**
   * Tests that adding an orderLine with invalid amount does not work.
   */
  @Test
  public void changeOrderWithInvalidAmount() {

    OrderCto orderCto = getOrderCto();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);
    OrderLineEto orderLineEto = orderLineCto.getOrderLine();

    orderLineEto.setAmount(-1);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(InvalidChangedOrderException.class);
  }

  /**
   * Tests that adding an orderLine with amount null does not work.
   */
  @Test
  public void changeOrderWithAmountNull() {

    OrderCto orderCto = getOrderCto();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);
    OrderLineEto orderLineEto = orderLineCto.getOrderLine();

    orderLineEto.setAmount(null);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(InvalidChangedOrderException.class);
  }

  /**
   * Tests that adding an orderLine with invalid extraId does not work.
   */
  @Test
  public void changeOrderWithInvalidExtraId() {

    OrderCto orderCto = getOrderCto();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);
    IngredientEto extra = orderLineCto.getExtras().get(0);

    extra.setId(-1L);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(ObjectRetrievalFailureException.class);
  }

  /**
   * Tests that adding an orderLine with extraId null does not work.
   */
  @Test
  public void changeOrderWithExtraIdNull() {

    OrderCto orderCto = getOrderCto();
    OrderLineCto orderLineCto = orderCto.getOrderLines().get(0);
    IngredientEto extra = orderLineCto.getExtras().get(0);

    extra.setId(null);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrder(orderCto);
    }).isInstanceOf(InvalidChangedOrderException.class);
  }

  // ============= Tests for: changeOrderStatus() =============

  /**
   * Tests that all advancement processes of changeOrderStatus() work properly.
   */
  @Test
  public void changeOrderStatusAdvanceValid() {

    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(0);

    // advancing status from 0 -> 1
    this.ordermanagement.changeOrderStatus(this.orderId, null);

    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(1);

    // advancing status from 1 -> 2
    this.ordermanagement.changeOrderStatus(this.orderId, null);

    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(2);

    // cannot advance to "Complete" (3) without having paid
    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderStatus(this.orderId, null);
    }).isInstanceOf(ChangingOrderStatusNotAllowedException.class);

    // setting payStatus to paid
    this.ordermanagement.changeOrderPayStatus(this.orderId, null);

    // advancing status to 2 -> 3
    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(2);
    this.ordermanagement.changeOrderStatus(this.orderId, null);
    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(3);

    // cannot further advance from Complete
    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderStatus(this.orderId, null);
    }).isInstanceOf(ChangingOrderStatusNotAllowedException.class);

  }

  /**
   * Tests that changing the orderStatus on an invalid order does not work.
   */
  @Test
  public void changeOrderStatusAdvanceInvalidOrderId() {

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderStatus(-1L, null);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that changing the orderStatus on orderId null does not work.
   */
  @Test
  public void changeOrderStatusAdvanceOrderIdNull() {

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderStatus(null, null);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }

  /**
   * Tests that changing the orderStatus without advancing is possible.
   */
  @Test
  public void changeOrderStatusCustomValid() {

    OrderStatusEto orderStatus = new OrderStatusEto();

    Long newOrderStatus = 4L;
    orderStatus.setId(newOrderStatus);

    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(0);
    this.ordermanagement.changeOrderStatus(this.orderId, orderStatus);
    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(newOrderStatus);

    newOrderStatus = 3L;
    orderStatus.setId(newOrderStatus);
    this.ordermanagement.changeOrderStatus(this.orderId, orderStatus);
    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(newOrderStatus);

    newOrderStatus = 2L;
    orderStatus.setId(newOrderStatus);
    this.ordermanagement.changeOrderStatus(this.orderId, orderStatus);
    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(newOrderStatus);

    newOrderStatus = 1L;
    orderStatus.setId(newOrderStatus);
    this.ordermanagement.changeOrderStatus(this.orderId, orderStatus);
    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(newOrderStatus);

    newOrderStatus = 0L;
    orderStatus.setId(newOrderStatus);
    this.ordermanagement.changeOrderStatus(this.orderId, orderStatus);
    assertThat(getOrderCto().getOrderStatus().getId()).isEqualTo(newOrderStatus);
  }

  /**
   * Tests that changing the orderStatus to an invalid id is not possible.
   */
  @Test
  public void changeOrderStatusCustomInvalidOrderStatusId() {

    OrderStatusEto orderStatus = new OrderStatusEto();
    orderStatus.setId(5L);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderStatus(this.orderId, orderStatus);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  // ============= Tests for: changeOrderPayStatus() =============

  /**
   * Tests that all advancement processes of changeOrderPayStatus() work properly.
   */
  @Test
  public void changeOrderPayStatusAdvanceValid() {

    assertThat(getOrderCto().getOrderPayStatus().getId()).isEqualTo(0);

    // advancing status from 0 -> 1
    this.ordermanagement.changeOrderPayStatus(this.orderId, null);

    assertThat(getOrderCto().getOrderPayStatus().getId()).isEqualTo(1);

    // cannot further advance from Paid
    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderPayStatus(this.orderId, null);
    }).isInstanceOf(ChangingOrderPayStatusNotAllowedException.class);

  }

  /**
   * Tests that advaning the orderPayStatus is not possible on an invalid orderId.
   */
  @Test
  public void changeOrderPayStatusAdvanceInvalidOrderId() {

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderPayStatus(-1L, null);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that advaning the orderPayStatus is not possible on an orderId null.
   */
  @Test
  public void changeOrderPayStatusAdvanceOrderIdNull() {

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderPayStatus(null, null);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }

  /**
   * Tests that changing the orderPayStatus without advancing is possible.
   */
  @Test
  public void changeOrderPayStatusCustomValid() {

    OrderPayStatusEto orderPayStatus = new OrderPayStatusEto();

    Long newOrderPayStatus = 1L;
    orderPayStatus.setId(newOrderPayStatus);

    assertThat(getOrderCto().getOrderPayStatus().getId()).isEqualTo(0);
    this.ordermanagement.changeOrderPayStatus(this.orderId, orderPayStatus);
    assertThat(getOrderCto().getOrderPayStatus().getId()).isEqualTo(newOrderPayStatus);

    newOrderPayStatus = 0L;
    orderPayStatus.setId(newOrderPayStatus);

    this.ordermanagement.changeOrderPayStatus(this.orderId, orderPayStatus);
    assertThat(getOrderCto().getOrderPayStatus().getId()).isEqualTo(newOrderPayStatus);

    assertThat(getOrderCto().getOrderPayStatus().getId()).isEqualTo(newOrderPayStatus);
  }

  /**
   * Tests that changing the orderPayStatus to an invalid Id is not possible.
   */
  @Test
  public void changeOrderPayStatusCustomInvalidOrderPayStatusId() {

    OrderPayStatusEto orderPayStatus = new OrderPayStatusEto();
    orderPayStatus.setId(5L);

    assertThatThrownBy(() -> {
      this.ordermanagement.changeOrderPayStatus(this.orderId, orderPayStatus);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

}
