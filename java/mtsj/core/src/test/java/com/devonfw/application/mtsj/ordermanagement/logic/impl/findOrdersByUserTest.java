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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.general.common.TestUtil;
import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderSearchCriteriaTo;

/**
 * Unit Tests for {@link Ordermanagement#findOrdersByUser(OrderSearchCriteriaTo)}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findOrdersByUserTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  @Inject
  private BookingRepository bookingDAO;

  private String token = "CB_20210826_abcdefgh";

  private Long orderId;

  /**
   * Creates a new booking and order before each test.
   */
  @BeforeEach
  public void startup() {

    // Saving booking:

    BookingEntity bookingEntity = new BookingEntity();

    bookingEntity.setName("testUser");
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
   * Tests that an order with an invalid token will not be stored.
   */
  @Test
  public void findOrdersByUserValid() {

    // when

    TestUtil.login("testUser", ApplicationAccessControlConfig.GROUP_CUSTOMER);

    Pageable pageable = PageRequest.of(0, 20);
    OrderSearchCriteriaTo orderSearch = new OrderSearchCriteriaTo();
    orderSearch.setPageable(pageable);

    List<OrderCto> orders = this.ordermanagement.findOrdersByUser(orderSearch).toList();

    OrderCto orderCto = orders.get(0);
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
    assertThat(orders).hasSize(1);
    assertThat(orderEto.getId()).isEqualTo(this.orderId);
    assertThat(bookingEto.getBookingToken()).isEqualTo(this.token);
    assertThat(bookingEto.getName()).isEqualTo("testUser");

    assertThat(orderLineEtoChicken.getDishId()).isEqualTo(2L);
    assertThat(orderLineEtoChicken.getAmount()).isEqualTo(1);
    assertThat(orderLineEtoChicken.getComment()).isEqualTo("test");
    assertThat(extrasChicken).hasSize(2);

    assertThat(orderLineEtoBeer.getDishId()).isEqualTo(5L);
    assertThat(orderLineEtoBeer.getAmount()).isEqualTo(2);
    assertThat(orderLineEtoBeer.getComment()).isEqualTo("beer");
    assertThat(extrasBeer).isEmpty();

    TestUtil.logout();
  }

  /**
   * Tests that an no orders are returned if the user has not made any.
   */
  @Test
  public void findOrdersByUserEmpty() {

    // when

    TestUtil.login("testUserWithoutOrders", ApplicationAccessControlConfig.GROUP_CUSTOMER);

    Pageable pageable = PageRequest.of(0, 20);
    OrderSearchCriteriaTo orderSearch = new OrderSearchCriteriaTo();
    orderSearch.setPageable(pageable);

    Page<OrderCto> orders = this.ordermanagement.findOrdersByUser(orderSearch);

    assertThat(orders).isNull();

    TestUtil.logout();
  }

  /**
   * Tests that an no orders are returned if no user is logged in.
   */
  @Test
  public void findOrdersByUserNotLoggedIn() {

    // when

    Pageable pageable = PageRequest.of(0, 20);
    OrderSearchCriteriaTo orderSearch = new OrderSearchCriteriaTo();
    orderSearch.setPageable(pageable);

    Page<OrderCto> orders = this.ordermanagement.findOrdersByUser(orderSearch);

    // then
    assertThat(orders).isNull();
  }
}
