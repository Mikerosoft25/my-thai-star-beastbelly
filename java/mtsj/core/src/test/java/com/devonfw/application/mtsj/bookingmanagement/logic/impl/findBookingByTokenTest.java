package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.time.Instant;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Unit Tests for {@link Bookingmanagement#findBookingByToken}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findBookingByTokenTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  private String name = "bookingTestUser";

  private String email = "bookingTestUser@mail.com";

  private String comment = "test";

  private int assistants = 3;

  // setting the time to 05.07.2021 15:00:00
  private Instant bookingDate = Instant.ofEpochMilli(1625490000);

  /**
   * Tests that a booking can be succesfully found by using a guest token.
   */
  @Test
  public void findBookingByTokenValid() {

    // Saving a booking

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(this.bookingDate);
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);
    bookingEto.setAssistants(this.assistants);

    bookingCto.setBooking(bookingEto);

    BookingEto savedBooking = this.bookingmanagement.saveBooking(bookingCto);

    // Finding the booking by Token
    BookingCto bookingByTokenCto = this.bookingmanagement.findBookingByToken(savedBooking.getBookingToken());
    BookingEto bookingByTokenEto = bookingByTokenCto.getBooking();

    assertThat(bookingByTokenEto.getBookingToken()).isEqualTo(savedBooking.getBookingToken());
    assertThat(bookingByTokenEto.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(bookingByTokenEto.getName()).isEqualTo(this.name);
    assertThat(bookingByTokenEto.getEmail()).isEqualTo(this.email);
    assertThat(bookingByTokenEto.getComment()).isEqualTo(this.comment);
    assertThat(bookingByTokenEto.getBookingType()).isEqualTo(BookingType.COMMON);
    assertThat(bookingByTokenEto.getAssistants()).isEqualTo(3);
  }

  /**
   * Tests that an exception is thrown if an invalid token is used.
   */
  @Test
  public void findBookingByTokenInvalidToken() {

    BookingCto booking = this.bookingmanagement.findBookingByToken("invalidToken");

    assertThat(booking).isNull();
  }

  /**
   * Tests that an exception is thrown if null is used as parameter.
   */
  @Test
  public void findBookingByTokenWithNull() {

    BookingCto booking = this.bookingmanagement.findBookingByToken(null);

    assertThat(booking).isNull();
  }

}
