package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.time.Instant;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Unit Tests for {@link Bookingmanagement#findBooking}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findBookingTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  private String name = "testUser";

  private String email = "testUser@mail.com";

  private String comment = "test";

  private int assistants = 3;

  // setting the time to 05.07.2021 15:00:00
  private Instant bookingDate = Instant.ofEpochMilli(1625490000);

  private Long bookingId;

  /**
   * Creates a new booking before each test.
   */
  @BeforeEach
  public void init() {

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(this.bookingDate);
    bookingEto.setName(this.name);
    bookingEto.setEmail(this.email);
    bookingEto.setComment(this.comment);
    bookingEto.setAssistants(this.assistants);

    bookingCto.setBooking(bookingEto);

    BookingEto savedBooking = this.bookingmanagement.saveBooking(bookingCto);

    this.bookingId = savedBooking.getId();
  }

  /**
   * Tests that the correct booking is found.
   */
  @Test
  public void findBookingValidId() {

    BookingEto savedBookingFromDB = this.bookingmanagement.findBooking(this.bookingId).getBooking();

    assertThat(savedBookingFromDB.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(savedBookingFromDB.getName()).isEqualTo(this.name);
    assertThat(savedBookingFromDB.getEmail()).isEqualTo(this.email);
    assertThat(savedBookingFromDB.getComment()).isEqualTo(this.comment);
    assertThat(savedBookingFromDB.getAssistants()).isEqualTo(this.assistants);
    assertThat(savedBookingFromDB.getBookingType()).isEqualTo(BookingType.COMMON);
  }

  /**
   * Tests that an exception is thrown if an invalid bookingId is used.
   */
  @Test
  public void findBookingInvalidId() {

    assertThatThrownBy(() -> {
      this.bookingmanagement.findBooking(-1L);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that an exception is thrown if null is used as a parameter.
   */
  @Test
  public void findBookingIdNull() {

    assertThatThrownBy(() -> {
      this.bookingmanagement.findBooking(null);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }
}
