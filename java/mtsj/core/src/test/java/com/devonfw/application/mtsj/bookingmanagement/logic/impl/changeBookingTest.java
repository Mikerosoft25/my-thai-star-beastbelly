package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.common.api.exception.InvalidBookingDateException;
import com.devonfw.application.mtsj.bookingmanagement.common.api.exception.InvalidBookingIdException;
import com.devonfw.application.mtsj.bookingmanagement.common.api.exception.InvalidTableIdException;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Tests for {@link Bookingmanagement#changeBooking}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class changeBookingTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  @Inject
  private BookingRepository bookingDao;

  private Long bookingId;

  /**
   * Creates a booking before each test.
   */
  @BeforeEach
  public void init() {

    BookingCto bookingCto = new BookingCto();
    BookingEto bookingEto = new BookingEto();

    bookingEto.setBookingDate(Instant.now().plus(5, ChronoUnit.HOURS));
    bookingEto.setName("testName");
    bookingEto.setEmail("testEmail");
    bookingEto.setComment("testComment");
    bookingEto.setAssistants(5);

    bookingCto.setBooking(bookingEto);

    BookingEto savedBooking = this.bookingmanagement.saveBooking(bookingCto);
    this.bookingId = savedBooking.getId();
  }

  /**
   * Deletes the created booking after each test.
   */
  @AfterEach
  public void cleanup() {

    this.bookingDao.deleteById(this.bookingId);
  }

  /**
   *
   * @return the BookingCto that was created before the test.
   */
  public BookingCto getBookingCto() {

    return this.bookingmanagement.findBooking(this.bookingId);
  }

  /**
   * Tests that using null as a parameter causes an exception.
   */
  @Test
  public void changeBookingWithNull() {

    assertThatThrownBy(() -> {
      this.bookingmanagement.changeBooking(null);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that changing booking with a new valid bookingDate in the future, valid tableId and name and email is
   * possible.
   */
  @Test
  public void changeBookingWithValidDateAndValidTableIdAndValidUserDetails() {

    BookingCto bookingCto = getBookingCto();
    BookingEto bookingEto = bookingCto.getBooking();

    Instant newDate = Instant.now().plus(24, ChronoUnit.HOURS);
    Long newTableId = 5L;
    String newName = "newName";
    String newEmail = "newEmail@mail.com";

    bookingEto.setBookingDate(newDate);
    bookingEto.setTableId(newTableId);
    bookingEto.setName(newName);
    bookingEto.setEmail(newEmail);

    BookingCto changedBookingCto = this.bookingmanagement.changeBooking(bookingCto);
    BookingEto changedBookingEto = changedBookingCto.getBooking();

    // check that unchanged values stay the same
    assertThat(changedBookingEto.getId()).isEqualTo(bookingEto.getId());
    assertThat(changedBookingEto.getBookingToken()).isEqualTo(bookingEto.getBookingToken());
    assertThat(changedBookingEto.getComment()).isEqualTo(bookingEto.getComment());

    assertThat(changedBookingEto.getCanceled()).isEqualTo(bookingEto.getCanceled());
    assertThat(changedBookingEto.getBookingType()).isEqualTo(bookingEto.getBookingType());
    assertThat(changedBookingEto.getAssistants()).isEqualTo(bookingEto.getAssistants());
    assertThat(changedBookingEto.getUserId()).isEqualTo(bookingEto.getUserId());

    // check that date was changed and expirationDate as well
    assertThat(changedBookingEto.getBookingDate()).isEqualTo(newDate);
    assertThat(changedBookingEto.getExpirationDate()).isEqualTo(newDate.minus(1, ChronoUnit.HOURS));
    // check that tableId has been changed
    assertThat(changedBookingEto.getTableId()).isEqualTo(newTableId);
    // check that name and email changed
    assertThat(changedBookingEto.getName()).isEqualTo(newName);
    assertThat(changedBookingEto.getEmail()).isEqualTo(newEmail);

  }

  /**
   * Tests that changign to an invalid date is not possible.
   */
  @Test
  public void changeBookingWithInvalidDate() {

    BookingCto bookingCto = getBookingCto();
    BookingEto bookingEto = bookingCto.getBooking();

    Instant newDate = Instant.now().minus(24, ChronoUnit.HOURS);

    bookingEto.setBookingDate(newDate);

    assertThatThrownBy(() -> {
      this.bookingmanagement.changeBooking(bookingCto);
    }).isInstanceOf(InvalidBookingDateException.class);

  }

  /**
   * Changing booking with new date null
   */
  public void changeBookingWithDateNull() {

    BookingCto bookingCto = getBookingCto();
    BookingEto bookingEto = bookingCto.getBooking();

    bookingEto.setBookingDate(null);

    assertThatThrownBy(() -> {
      this.bookingmanagement.changeBooking(bookingCto);
    }).isInstanceOf(InvalidBookingDateException.class);
  }

  /**
   * Changing booking with non existent bookingId
   */
  @Test
  public void changeBookingWithInvalidBookingId() {

    BookingCto bookingCto = getBookingCto();
    BookingEto bookingEto = bookingCto.getBooking();

    bookingEto.setId(-1L);

    assertThatThrownBy(() -> {
      this.bookingmanagement.changeBooking(bookingCto);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Changing booking with bookingId null
   */
  @Test
  public void changeBookingWithBookingIdNull() {

    BookingCto bookingCto = getBookingCto();
    BookingEto bookingEto = bookingCto.getBooking();

    bookingEto.setId(null);

    assertThatThrownBy(() -> {
      this.bookingmanagement.changeBooking(bookingCto);
    }).isInstanceOf(InvalidBookingIdException.class);
  }

  /**
   * Changing booking with non existent tableId
   */
  @Test
  public void changeBookingWithInvalidTableId() {

    BookingCto bookingCto = getBookingCto();
    BookingEto bookingEto = bookingCto.getBooking();

    bookingEto.setTableId(-1L);

    assertThatThrownBy(() -> {
      this.bookingmanagement.changeBooking(bookingCto);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Changing booking with tableId null
   */
  @Test
  public void changeBookingWithTableIdNull() {

    BookingCto bookingCto = getBookingCto();
    BookingEto bookingEto = bookingCto.getBooking();

    bookingEto.setTableId(null);

    assertThatThrownBy(() -> {
      this.bookingmanagement.changeBooking(bookingCto);
    }).isInstanceOf(InvalidTableIdException.class);
  }

  /**
   * Changing all unallowed fields on booking
   */
  @Test
  public void changeBookingWithUnallowedFields() {

    BookingCto bookingCto = getBookingCto();
    BookingEto originalbookingEto = bookingCto.getBooking();

    BookingEto changedBookingEto = new BookingEto();

    // modificationcounter is 1 by default
    changedBookingEto.setModificationCounter(1);
    // setting the allowed fields to the same as they are right now
    changedBookingEto.setName(originalbookingEto.getName());
    changedBookingEto.setId(originalbookingEto.getId());
    changedBookingEto.setBookingDate(originalbookingEto.getBookingDate());
    changedBookingEto.setTableId(originalbookingEto.getTableId());
    changedBookingEto.setEmail(originalbookingEto.getEmail());

    // changing all fields that are not allowed to change
    changedBookingEto.setBookingToken("test");
    changedBookingEto.setComment("test");
    changedBookingEto.setExpirationDate(Instant.now());
    changedBookingEto.setCreationDate(Instant.now());
    changedBookingEto.setCanceled(true);
    changedBookingEto.setBookingType(BookingType.INVITED);
    changedBookingEto.setAssistants(300);
    changedBookingEto.setUserId(2L);

    bookingCto.setBooking(changedBookingEto);

    BookingCto changedBookingCto = this.bookingmanagement.changeBooking(bookingCto);
    BookingEto savedBookingEto = changedBookingCto.getBooking();

    // checking that changing all unallowed fields had no effect
    assertThat(savedBookingEto.getId()).isEqualTo(originalbookingEto.getId());
    assertThat(savedBookingEto.getName()).isEqualTo(originalbookingEto.getName());
    assertThat(savedBookingEto.getBookingToken()).isEqualTo(originalbookingEto.getBookingToken());
    assertThat(savedBookingEto.getComment()).isEqualTo(originalbookingEto.getComment());
    assertThat(savedBookingEto.getBookingDate()).isEqualTo(originalbookingEto.getBookingDate());
    assertThat(savedBookingEto.getExpirationDate()).isEqualTo(originalbookingEto.getExpirationDate());
    assertThat(savedBookingEto.getCreationDate()).isEqualTo(originalbookingEto.getCreationDate());
    assertThat(savedBookingEto.getEmail()).isEqualTo(originalbookingEto.getEmail());
    assertThat(savedBookingEto.getCanceled()).isEqualTo(originalbookingEto.getCanceled());
    assertThat(savedBookingEto.getBookingType()).isEqualTo(originalbookingEto.getBookingType());
    assertThat(savedBookingEto.getTableId()).isEqualTo(originalbookingEto.getTableId());
    assertThat(savedBookingEto.getAssistants()).isEqualTo(originalbookingEto.getAssistants());
    assertThat(savedBookingEto.getUserId()).isEqualTo(originalbookingEto.getUserId());

  }

}
