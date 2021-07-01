package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.general.common.TestUtil;
import com.devonfw.application.mtsj.general.common.api.constants.Roles;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;

/**
 * Unit Tests for {@link Bookingmanagement#cancelBooking}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class cancelBookingTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  @Inject
  private Ordermanagement ordermanagement;

  @Inject
  private BookingRepository bookingDAO;

  private OrderEto order;

  private BookingEto booking;

  /**
   * Deletes the booking after each test.
   */
  @AfterEach
  public void cleanup() {

    if (this.booking != null) {
      try {
        this.bookingDAO.deleteById(this.booking.getId());
      } catch (Exception e) {
      }
    }
  }

  /**
   * Tests that an order can be canceled by a waiter and the booking, the order and the invited guests are deleted.
   */
  @Test
  public void cancelBookingValid() {

    // creating a booking in 24 hours
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(24, ChronoUnit.HOURS));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testEmail@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    // adding two invited guests
    List<InvitedGuestEto> invitedGuests = new ArrayList<>();

    InvitedGuestEto invitedGuest1 = new InvitedGuestEto();
    InvitedGuestEto invitedGuest2 = new InvitedGuestEto();

    invitedGuest1.setEmail("invite1@mail.com");
    invitedGuest2.setEmail("invite2@mail.com");

    invitedGuests.add(invitedGuest1);
    invitedGuests.add(invitedGuest2);

    bookingCto.setInvitedGuests(invitedGuests);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // getting the invited guests from the booking
    List<InvitedGuestEto> guests = this.bookingmanagement.findInvitedGuestByBooking(this.booking.getId());
    InvitedGuestEto guest1 = guests.get(0);
    InvitedGuestEto guest2 = guests.get(1);

    // saving an order to the created booking
    OrderCto orderCto = buildOrderCto(this.booking.getBookingToken());
    this.order = this.ordermanagement.saveOrder(orderCto);

    // authorized true, meaning that the method is made by an authorized user, e.g. a waiter
    boolean canceled = this.bookingmanagement.cancelBooking(this.booking.getId(), true);

    BookingCto bookingFromDB = this.bookingmanagement.findBookingByToken(this.booking.getBookingToken());

    // checking that the booking was canceled and thus the booking,the order and all invited guests to it have been
    // deleted.
    assertThat(canceled).isTrue();
    assertThat(bookingFromDB).isNull();
    assertThatThrownBy(() -> {
      this.ordermanagement.findOrder(this.order.getId());
    }).isInstanceOf(EmptyResultDataAccessException.class);
    assertThatThrownBy(() -> {
      this.bookingmanagement.findInvitedGuest(guest1.getId());
    }).isInstanceOf(EmptyResultDataAccessException.class);
    assertThatThrownBy(() -> {
      this.bookingmanagement.findInvitedGuest(guest2.getId());
    }).isInstanceOf(EmptyResultDataAccessException.class);

  }

  /**
   * Tests that a user can cancel his own order.
   */
  @Test
  public void cancelBookingByUserValid() {

    // same user as in the booking name
    TestUtil.login("testUser", Roles.CUSTOMER);

    // creating a booking in 24 hours
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(24, ChronoUnit.HOURS));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testUser@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // saving an order to the created booking
    OrderCto orderCto = buildOrderCto(this.booking.getBookingToken());
    this.order = this.ordermanagement.saveOrder(orderCto);

    // authorized false, meaning that the method is made by a user
    boolean canceled = this.bookingmanagement.cancelBooking(this.booking.getId(), false);

    BookingCto bookingFromDB = this.bookingmanagement.findBookingByToken(this.booking.getBookingToken());

    // checking that the booking was canceled and thus the booking and the order to it have been deleted.
    assertThat(canceled).isTrue();
    assertThat(bookingFromDB).isNull();
    assertThatThrownBy(() -> {
      this.ordermanagement.findOrder(this.order.getId());
    }).isInstanceOf(EmptyResultDataAccessException.class);

    TestUtil.logout();

  }

  /**
   * Tests that a user cannot cancel orders of other users.
   */
  @Test
  public void cancelBookingByUserInvalid() {

    // same user as in the booking name
    TestUtil.login("anotherUser", Roles.CUSTOMER);

    // creating a booking in 24 hours
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(24, ChronoUnit.HOURS));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testUser@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // saving an order to the created booking
    OrderCto orderCto = buildOrderCto(this.booking.getBookingToken());
    this.order = this.ordermanagement.saveOrder(orderCto);

    assertThatThrownBy(() -> {
      this.bookingmanagement.cancelBooking(this.booking.getId(), false);
    }).isInstanceOf(RuntimeException.class).hasMessage("Customer cannot cancel bookings of other users.");

    TestUtil.logout();

  }

  /**
   * Tests that an order can be canceled by a waiter.
   */
  @Test
  public void cancelBookingInTheNextHourInvalid() {

    // creating a booking in 15 minutes
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(15, ChronoUnit.MINUTES));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testEmail@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // saving an order to the created booking
    OrderCto orderCto = buildOrderCto(this.booking.getBookingToken());
    this.order = this.ordermanagement.saveOrder(orderCto);

    assertThatThrownBy(() -> {
      this.bookingmanagement.cancelBooking(this.booking.getId(), true);
    }).isInstanceOf(RuntimeException.class).hasMessage("It is too late to cancel this booking.");
  }

  /**
   * Tests that an order can be canceled by a waiter.
   */
  @Test
  public void cancelBookingInTheNextHourByUserInvalid() {

    TestUtil.login("testUser", Roles.CUSTOMER);

    // creating a booking in 15 minutes
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(15, ChronoUnit.MINUTES));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testEmail@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // saving an order to the created booking
    OrderCto orderCto = buildOrderCto(this.booking.getBookingToken());
    this.order = this.ordermanagement.saveOrder(orderCto);

    assertThatThrownBy(() -> {
      this.bookingmanagement.cancelBooking(this.booking.getId(), false);
    }).isInstanceOf(RuntimeException.class).hasMessage("It is too late to cancel this booking.");

    TestUtil.logout();
  }

  /**
   * Builds an OrderCto with 1x THAI GREEN CHICKEN CURRY with Extras: Tofu and Curry and 2x Beer.
   */
  private OrderCto buildOrderCto(String bookingToken) {

    OrderCto orderCto = new OrderCto();

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken(bookingToken);
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

    return orderCto;
  }
}
