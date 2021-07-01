package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.service.impl.rest.BookingmanagementRestServiceImpl;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Tests for {@link Bookingmanagement#saveBooking}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class saveBookingTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  @Inject
  private BookingmanagementRestServiceImpl bookingmanagementRestServiceImpl;

  private String name = "testUser";

  private String email = "testUser@mail.com";

  private String comment = "test";

  private int assistants = 3;

  // setting the time to 05.07.2021 15:00:00
  private Instant bookingDate = Instant.ofEpochMilli(1625490000);

  /**
   * Tests that a booking can be succesfully saved without invited guests.
   */
  @Test
  public void saveBookingWithoutInvitedGuestsValid() {

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(this.bookingDate);
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);
    bookingEto.setAssistants(this.assistants);

    bookingCto.setBooking(bookingEto);

    BookingEto savedBooking = this.bookingmanagement.saveBooking(bookingCto);
    BookingEto savedBookingFromDB = this.bookingmanagement.findBooking(savedBooking.getId()).getBooking();

    assertThat(savedBooking.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(savedBooking.getName()).isEqualTo(this.name);
    assertThat(savedBooking.getEmail()).isEqualTo(this.email);
    assertThat(savedBooking.getComment()).isEqualTo(this.comment);
    assertThat(savedBooking.getAssistants()).isEqualTo(this.assistants);
    assertThat(savedBooking.getBookingType()).isEqualTo(BookingType.COMMON);

    assertThat(savedBookingFromDB.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(savedBookingFromDB.getName()).isEqualTo(this.name);
    assertThat(savedBookingFromDB.getEmail()).isEqualTo(this.email);
    assertThat(savedBookingFromDB.getComment()).isEqualTo(this.comment);
    assertThat(savedBookingFromDB.getAssistants()).isEqualTo(this.assistants);
    assertThat(savedBookingFromDB.getBookingType()).isEqualTo(BookingType.COMMON);
  }

  /**
   * Tests that a booking can be succesfully saved with invited guests.
   */
  @Test
  public void saveBookingWithnvitedGuestsValid() {

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(this.bookingDate);
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);

    bookingCto.setBooking(bookingEto);

    List<InvitedGuestEto> invitedGuests = new ArrayList<>();

    InvitedGuestEto invitedGuest1 = new InvitedGuestEto();
    InvitedGuestEto invitedGuest2 = new InvitedGuestEto();

    invitedGuest1.setEmail("guest1@mail.com");
    invitedGuest2.setEmail("guest2@mail.com");

    invitedGuests.add(invitedGuest1);
    invitedGuests.add(invitedGuest2);

    bookingCto.setInvitedGuests(invitedGuests);

    BookingEto savedBooking = this.bookingmanagement.saveBooking(bookingCto);
    BookingCto savedBookingCtoFromDB = this.bookingmanagement.findBooking(savedBooking.getId());
    BookingEto savedBookingEtoFromDB = savedBookingCtoFromDB.getBooking();

    assertThat(savedBooking.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(savedBooking.getName()).isEqualTo(this.name);
    assertThat(savedBooking.getEmail()).isEqualTo(this.email);
    assertThat(savedBooking.getComment()).isEqualTo(this.comment);
    assertThat(savedBooking.getBookingType()).isEqualTo(BookingType.INVITED);
    assertThat(savedBooking.getAssistants()).isEqualTo(3);

    assertThat(savedBookingEtoFromDB.getBookingToken()).isEqualTo(savedBooking.getBookingToken());
    assertThat(savedBookingEtoFromDB.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(savedBookingEtoFromDB.getName()).isEqualTo(this.name);
    assertThat(savedBookingEtoFromDB.getEmail()).isEqualTo(this.email);
    assertThat(savedBookingEtoFromDB.getComment()).isEqualTo(this.comment);
    assertThat(savedBookingEtoFromDB.getBookingType()).isEqualTo(BookingType.INVITED);
    assertThat(savedBooking.getAssistants()).isEqualTo(3);

    assertThat(savedBookingCtoFromDB.getInvitedGuests()).hasSize(2);
    assertThat(savedBookingCtoFromDB.getInvitedGuests().get(0).getEmail()).isEqualTo("guest1@mail.com");
    assertThat(savedBookingCtoFromDB.getInvitedGuests().get(1).getEmail()).isEqualTo("guest2@mail.com");
  }

  /**
   * Tests that if multiple invited guests have the same email, they are only saved once.
   */
  @Test
  public void saveBookingWithMultipleSameInvitedGuests() {

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(this.bookingDate);
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);

    bookingCto.setBooking(bookingEto);

    List<InvitedGuestEto> invitedGuests = new ArrayList<>();

    InvitedGuestEto invitedGuest1 = new InvitedGuestEto();
    InvitedGuestEto invitedGuest2 = new InvitedGuestEto();
    InvitedGuestEto invitedGuest3 = new InvitedGuestEto();
    InvitedGuestEto invitedGuest4 = new InvitedGuestEto();

    invitedGuest1.setEmail("guest1@mail.com");
    invitedGuest2.setEmail("guest2@mail.com");
    invitedGuest3.setEmail("guest2@mail.com");
    invitedGuest4.setEmail("guest2@mail.com");

    invitedGuests.add(invitedGuest1);
    invitedGuests.add(invitedGuest2);

    bookingCto.setInvitedGuests(invitedGuests);

    BookingEto savedBooking = this.bookingmanagement.saveBooking(bookingCto);
    BookingCto savedBookingCtoFromDB = this.bookingmanagement.findBooking(savedBooking.getId());
    BookingEto savedBookingEtoFromDB = savedBookingCtoFromDB.getBooking();

    assertThat(savedBooking.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(savedBooking.getName()).isEqualTo(this.name);
    assertThat(savedBooking.getEmail()).isEqualTo(this.email);
    assertThat(savedBooking.getComment()).isEqualTo(this.comment);
    assertThat(savedBooking.getBookingType()).isEqualTo(BookingType.INVITED);

    assertThat(savedBookingEtoFromDB.getBookingToken()).isEqualTo(savedBooking.getBookingToken());
    assertThat(savedBookingEtoFromDB.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(savedBookingEtoFromDB.getName()).isEqualTo(this.name);
    assertThat(savedBookingEtoFromDB.getEmail()).isEqualTo(this.email);
    assertThat(savedBookingEtoFromDB.getComment()).isEqualTo(this.comment);
    assertThat(savedBookingEtoFromDB.getBookingType()).isEqualTo(BookingType.INVITED);

    assertThat(savedBookingCtoFromDB.getInvitedGuests()).hasSize(2);
    assertThat(savedBookingCtoFromDB.getInvitedGuests().get(0).getEmail()).isEqualTo("guest1@mail.com");
    assertThat(savedBookingCtoFromDB.getInvitedGuests().get(1).getEmail()).isEqualTo("guest2@mail.com");
  }

  /**
   * Tests that an exception is thrown if the only invited guest is the host himself.
   */
  @Test
  public void saveBookingWithOnlyInvitedGuestHost() {

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(this.bookingDate);
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);

    bookingCto.setBooking(bookingEto);

    List<InvitedGuestEto> invitedGuests = new ArrayList<>();

    InvitedGuestEto invitedGuest1 = new InvitedGuestEto();

    invitedGuest1.setEmail(this.email);

    invitedGuests.add(invitedGuest1);

    bookingCto.setInvitedGuests(invitedGuests);

    assertThatThrownBy(() -> {
      this.bookingmanagement.saveBooking(bookingCto);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an exception is thrown if the bookingDate is in the past.
   */
  @Test
  public void saveBookingInThePast() {

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().minus(24, ChronoUnit.HOURS));
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);

    bookingCto.setBooking(bookingEto);

    assertThatThrownBy(() -> {
      this.bookingmanagementRestServiceImpl.saveBooking(bookingCto);
    }).isInstanceOf(ConstraintViolationException.class);
  }

  /**
   * Tests that an exception is thrown if the email format is not a valid email-format.
   */
  @Test
  public void saveBookingInvalidEmailFormat() {

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().minus(24, ChronoUnit.HOURS));
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);

    bookingCto.setBooking(bookingEto);

    assertThatThrownBy(() -> {
      this.bookingmanagementRestServiceImpl.saveBooking(bookingCto);
    }).isInstanceOf(ConstraintViolationException.class);
  }

}
