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

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Unit Tests for {@link Bookingmanagement#acceptInvite}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class acceptInviteTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  @Inject
  private BookingRepository bookingDAO;

  private BookingEto booking;

  /**
   * Deleted the created booking after each test.
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
   * Tests that a valid invite can be accepted.
   */
  @Test
  public void acceptInviteValid() {

    // creating a booking in 24 hours
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(24, ChronoUnit.HOURS));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testEmail@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    // adding one invited guest
    List<InvitedGuestEto> invitedGuests = new ArrayList<>();

    InvitedGuestEto invitedGuest1 = new InvitedGuestEto();

    invitedGuest1.setEmail("invite1@mail.com");

    invitedGuests.add(invitedGuest1);

    bookingCto.setInvitedGuests(invitedGuests);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // getting the invited guests from the booking
    List<InvitedGuestEto> guests = this.bookingmanagement.findInvitedGuestByBooking(this.booking.getId());
    InvitedGuestEto guest1 = guests.get(0);

    // accepting the invite
    invitedGuest1 = this.bookingmanagement.acceptInvite(guest1.getGuestToken());

    // loading the booking from the DB
    BookingCto bookingFromDB = this.bookingmanagement.findBookingByToken(this.booking.getBookingToken());
    InvitedGuestEto invitedGuestFromDB = bookingFromDB.getInvitedGuests().get(0);

    // checking that the invite has been accepted
    assertThat(invitedGuest1.getAccepted()).isTrue();
    assertThat(invitedGuestFromDB.getAccepted()).isTrue();

  }

  /**
   * Tests that an invite cannot be accepted for a booking in the next hour.
   */
  @Test
  public void acceptInviteInvalidBookingInNextHour() {

    // creating a booking in 30 minutes
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(30, ChronoUnit.MINUTES));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testEmail@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    // adding one invited guest
    List<InvitedGuestEto> invitedGuests = new ArrayList<>();

    InvitedGuestEto invitedGuest1 = new InvitedGuestEto();

    invitedGuest1.setEmail("invite1@mail.com");

    invitedGuests.add(invitedGuest1);

    bookingCto.setInvitedGuests(invitedGuests);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // getting the invited guests from the booking
    List<InvitedGuestEto> guests = this.bookingmanagement.findInvitedGuestByBooking(this.booking.getId());
    InvitedGuestEto guest1 = guests.get(0);

    assertThatThrownBy(() -> {
      this.bookingmanagement.acceptInvite(guest1.getGuestToken());
    }).isInstanceOf(RuntimeException.class);

  }

  /**
   * Tests that an invite cannot be accepted for a booking in the past.
   */
  @Test
  public void acceptInviteInvalidBookingInPast() {

    // creating a booking in the past
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().minus(5, ChronoUnit.HOURS));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testEmail@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    // adding one invited guest
    List<InvitedGuestEto> invitedGuests = new ArrayList<>();

    InvitedGuestEto invitedGuest1 = new InvitedGuestEto();

    invitedGuest1.setEmail("invite1@mail.com");

    invitedGuests.add(invitedGuest1);

    bookingCto.setInvitedGuests(invitedGuests);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // getting the invited guests from the booking
    List<InvitedGuestEto> guests = this.bookingmanagement.findInvitedGuestByBooking(this.booking.getId());
    InvitedGuestEto guest1 = guests.get(0);

    assertThatThrownBy(() -> {
      this.bookingmanagement.acceptInvite(guest1.getGuestToken());
    }).isInstanceOf(RuntimeException.class);

  }

  /**
   * Tests that an invite cannot be accepted for a booking in the past.
   */
  @Test
  public void acceptInviteInvalidInviteHasAlreadyBeenAccepted() {

    // creating a booking in 24 hours
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(24, ChronoUnit.HOURS));
    bookingEto.setName("testUser");
    bookingEto.setEmail("testEmail@email.com");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    // adding one invited guest
    List<InvitedGuestEto> invitedGuests = new ArrayList<>();

    InvitedGuestEto invitedGuest1 = new InvitedGuestEto();

    invitedGuest1.setEmail("invite2@mail.com");

    invitedGuests.add(invitedGuest1);

    bookingCto.setInvitedGuests(invitedGuests);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    // getting the invited guests from the booking
    List<InvitedGuestEto> guests = this.bookingmanagement.findInvitedGuestByBooking(this.booking.getId());
    InvitedGuestEto guest1 = guests.get(0);

    // accepting the invite
    invitedGuest1 = this.bookingmanagement.acceptInvite(guest1.getGuestToken());

    // trying to accept the invite again
    assertThatThrownBy(() -> {
      this.bookingmanagement.acceptInvite(guest1.getGuestToken());
    }).isInstanceOf(RuntimeException.class);

  }

  /**
   * Tests that an exception is thrown if an invalid booking token is used.
   */
  @Test
  public void acceptInviteInvalidBookingToken() {

    assertThatThrownBy(() -> {
      this.bookingmanagement.acceptInvite("invalidToken");
    }).isInstanceOf(NullPointerException.class);

  }

  /**
   * Tests that null as a parameter causes an exception.
   */
  @Test
  public void acceptInviteInvalidBookingTokenIsNull() {

    assertThatThrownBy(() -> {
      this.bookingmanagement.acceptInvite(null);
    }).isInstanceOf(NullPointerException.class);

  }

}
