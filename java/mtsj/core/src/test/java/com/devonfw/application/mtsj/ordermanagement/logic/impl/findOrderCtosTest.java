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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderSearchCriteriaTo;

/**
 * Tests for {@link Ordermanagement#findOrderCtos(OrderSearchCriteriaTo)}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findOrderCtosTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  @Inject
  private Bookingmanagement bookingmanagement;

  @Inject
  private BookingRepository bookingDAO;

  private BookingEntity booking;

  private OrderEto order;

  /**
   * Creates a booking and order before all tests.
   */
  @BeforeAll
  public void startup() {

    // Saving booking:

    BookingEntity bookingEntity = new BookingEntity();

    bookingEntity.setName("filterTest");
    bookingEntity.setBookingToken("CB_20210826_abcdefgh");
    bookingEntity.setComment("Test Cases Booking");
    bookingEntity.setEmail("filterTestMail@mail.com");
    bookingEntity.setBookingDate(Instant.now().plus(24, ChronoUnit.DAYS));
    bookingEntity.setExpirationDate(Instant.now().plus(23, ChronoUnit.HOURS));
    bookingEntity.setCreationDate(Instant.now());
    bookingEntity.setCanceled(false);
    bookingEntity.setBookingType(BookingType.COMMON);
    bookingEntity.setTableId(3L);
    bookingEntity.setOrderId(null);
    bookingEntity.setAssistants(4);

    this.booking = this.bookingDAO.save(bookingEntity);

    // Saving an an OrderCto with 1x THAI GREEN CHICKEN CURRY with Extras: Tofu and Curry and 2x Beer
    OrderCto orderCto = new OrderCto();

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken(this.booking.getBookingToken());
    orderCto.setBooking(bookingEto);

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

    this.order = this.ordermanagement.saveOrder(orderCto);

  }

  /**
   * Deletes the booking after all tests are finished.
   */
  @AfterAll
  public void cleanup() {

    this.bookingmanagement.cancelBooking(this.booking.getId(), true);
  }

  /**
   * Tests that an order can be found by filtering by bookingId.
   */
  @Test
  public void findOrderCtosByBookingId() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setBookingId(this.booking.getId());

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that the only result is the above created order
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getOrder().getId()).isEqualTo(this.order.getId());
  }

  /**
   * Tests that an order can be found by filtering by email.
   */
  @Test
  public void findOrderCtosByBookingEmail() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setEmail(this.booking.getEmail());

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that the only result is the above created order
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getOrder().getId()).isEqualTo(this.order.getId());
  }

  /**
   * Tests that an order can be found by filtering by name.
   */
  @Test
  public void findOrderCtosByBookingName() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setName(this.booking.getName());

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that the only result is the above created order
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getOrder().getId()).isEqualTo(this.order.getId());
  }

  /**
   * Tests that an order can be found by filtering by booking-token.
   */
  @Test
  public void findOrderCtosByBookingToken() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setBookingToken(this.booking.getBookingToken());

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that the only result is the above created order
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getOrder().getId()).isEqualTo(this.order.getId());
  }

  /**
   * Tests that an order can be found by filtering by tableId.
   */
  @Test
  public void findOrderCtosByBookingTable() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setTableId(this.booking.getTableId());

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that all orders have the same tableId as the above created booking
    for (OrderCto resultOrder : resultList) {
      assertThat(resultOrder.getBooking().getTableId()).isEqualTo(this.booking.getTableId());
    }
  }

  /**
   * Tests that an order can be found by filtering by booking date.
   */
  @Test
  public void findOrderCtosByBookingDate() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setBookingDate(this.booking.getBookingDate());

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that the only result is the above created order
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getOrder().getId()).isEqualTo(this.order.getId());
  }

  /**
   * Tests that an order can be found by filtering by a single order status.
   */
  @Test
  public void findOrderCtosBySingleOrderStatus() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setOrderStatusId(0L);

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that all returned orders have the order status to "0 (Received)"
    for (OrderCto resultOrder : resultList) {
      assertThat(resultOrder.getOrderStatus().getId()).isEqualTo(0L);
    }
  }

  /**
   * Tests that an order can be found by filtering by a multiple order statuses.
   */
  @Test
  public void findOrderCtosByMultipleOrderStatuses() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    Long[] orderStatusIds = { 0L, 1L };

    criteria.setOrderStatusIds(orderStatusIds);

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that all returned orders have the order status to "0 (Received)" or "1 (Pending)"
    for (OrderCto resultOrder : resultList) {
      assertThat(resultOrder.getOrderStatus().getId()).isBetween(0L, 1L);
    }
  }

  /**
   * Tests that an order can be found by filtering by a single order pay status.
   */
  @Test
  public void findOrderCtosBySingleOrderPayStatus() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setOrderPayStatusId(0L);

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that all returned orders have the order pay status to "0 (Pending)"
    for (OrderCto resultOrder : resultList) {
      assertThat(resultOrder.getOrderStatus().getId()).isEqualTo(0L);
    }
  }

  /**
   * Tests that an order can be found by filtering wether it is an inHouse order or not
   */
  @Test
  public void findOrderCtosByInHouse() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setInHouse(true);

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that all returned orders have the order pay status to "0 (Pending)"
    for (OrderCto resultOrder : resultList) {
      assertThat(resultOrder.getBooking().getBookingType()).isIn(BookingType.COMMON, BookingType.INVITED);
    }
  }

  /**
   * Tests that an order can be found by filtering wether it has been paid or not
   */
  @Test
  public void findOrderCtosByPaid() {

    OrderSearchCriteriaTo criteria = new OrderSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setPaid(false);

    Page<OrderCto> result = this.ordermanagement.findOrderCtos(criteria);

    assertThat(result).isNotNull();

    List<OrderCto> resultList = result.toList();

    // checking that all returned orders have the order pay status to "0 (Pending)"
    for (OrderCto resultOrder : resultList) {
      assertThat(resultOrder.getOrderStatus().getId()).isEqualTo(0L);
    }
  }
}
