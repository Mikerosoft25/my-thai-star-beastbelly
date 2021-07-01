package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoBookingException;

/**
 * Unit Tests for {@link Bookingmanagement#findAssociatedBooking(Instant, String)}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findAssociatedBookingTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  @Inject
  private BookingRepository bookingDao;

  private String name = "associatedTestUser";

  private String email = "associatedTestUser@mail.com";

  private String comment = "test";

  private int assistants = 3;

  private BookingEto booking;

  /**
   * Deletes the booking after each test.
   */
  @AfterEach
  public void cleanUp() {

    if (this.booking != null) {
      this.bookingDao.deleteById(this.booking.getId());
      this.booking = null;
    }
  }

  /**
   * Tests that all 3 bookings of the mock user "Lena123" are found.
   */
  @Test
  public void findAssociatedBookingValidInTheFuture() {

    // creating a booking in 5 hours
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(5, ChronoUnit.HOURS));
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);
    bookingEto.setAssistants(this.assistants);

    bookingCto.setBooking(bookingEto);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    BookingCto associatedBooking = this.bookingmanagement.findAssociatedBooking(Instant.now(), this.email);

    assertThat(associatedBooking.getBooking().getId()).isEqualTo(this.booking.getId());
  }

  /**
   * Tests that an associated booking is found if it is still in the past hour
   */
  @Test
  public void findAssociatedBookingValid20MinutesAgo() {

    // creating a booking that was 20 minutes ago
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().minus(20, ChronoUnit.MINUTES));
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);
    bookingEto.setAssistants(this.assistants);

    bookingCto.setBooking(bookingEto);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    BookingCto associatedBooking = this.bookingmanagement.findAssociatedBooking(Instant.now(), this.email);

    assertThat(associatedBooking.getBooking().getId()).isEqualTo(this.booking.getId());
  }

  /**
   * Tests that an associated booking is not found if the booking was more than one hour ago
   */
  @Test
  public void findAssociatedBookingInvalidBookingOverAnHourAgo() {

    // creating a booking that was 1 hour and 5 minutes ago
    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().minus(1, ChronoUnit.HOURS).minus(5, ChronoUnit.MINUTES));
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);
    bookingEto.setAssistants(this.assistants);

    bookingCto.setBooking(bookingEto);

    this.booking = this.bookingmanagement.saveBooking(bookingCto);

    assertThatThrownBy(() -> {
      this.bookingmanagement.findAssociatedBooking(Instant.now(), this.email);
    }).isInstanceOf(NoBookingException.class);
  }

  /**
   * Tests that no associated booking is found if no bookings with the given email exist
   */
  @Test
  public void findAssociatedBookingInvalidNoBookingWithThatEmail() {

    assertThatThrownBy(() -> {
      this.bookingmanagement.findAssociatedBooking(Instant.now(), "nonExistentMail@mail.com");
    }).isInstanceOf(NoBookingException.class);
  }

}
