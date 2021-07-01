package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

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
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Tests for {@link Bookingmanagement#findBookingCtos(BookingSearchCriteriaTo)}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findBookingCtosTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  @Inject
  private BookingRepository bookingDAO;

  private BookingEntity booking;

  /**
   * Creates a booking and order before all tests.
   */
  @BeforeAll
  public void startup() {

    // Saving booking:

    BookingEntity bookingEntity = new BookingEntity();

    bookingEntity.setName("filterTest");
    bookingEntity.setBookingToken("CB_20210826_abcdefgh");
    bookingEntity.setComment("This is a test comment!");
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
  }

  /**
   * Deletes the booking after all tests are finished.
   */
  @AfterAll
  public void cleanup() {

    this.bookingmanagement.cancelBooking(this.booking.getId(), true);
  }

  /**
   * Tests that a booking can be found by filtering by id.
   */
  @Test
  public void findBookingCtosById() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    ArrayList<Long> ids = new ArrayList<>();
    ids.add(this.booking.getId());

    criteria.setIds(ids);

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    // checking that the only result is the above created booking
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getBooking().getId()).isEqualTo(this.booking.getId());
  }

  /**
   * Tests that a booking can be found by filtering by name.
   */
  @Test
  public void findBookingCtosByName() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setName(this.booking.getName());

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    // checking that the only result is the above created booking
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getBooking().getId()).isEqualTo(this.booking.getId());
  }

  /**
   * Tests that a booking can be found by filtering by bookingToken.
   */
  @Test
  public void findBookingCtosByBookingToken() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setBookingToken(this.booking.getBookingToken());

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    // checking that the only result is the above created booking
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getBooking().getId()).isEqualTo(this.booking.getId());
  }

  /**
   * Tests that a booking can be filtered by Comment.
   */
  @Test
  public void findBookingCtosByComment() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setComment(this.booking.getComment());

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    // checking that the only result is the above created booking
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getBooking().getId()).isEqualTo(this.booking.getId());
  }

  /**
   * Tests that a booking can be filtered by bookingDate.
   */
  @Test
  public void findBookingCtosByBookingDate() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setBookingDate(this.booking.getBookingDate());

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    // checking that the only result is the above created booking
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getBooking().getId()).isEqualTo(this.booking.getId());
  }

  /**
   * Tests that a booking can be filtered by email.
   */
  @Test
  public void findBookingCtosByEmail() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setEmail(this.booking.getEmail());

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    // checking that the only result is the above created booking
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getBooking().getId()).isEqualTo(this.booking.getId());
  }

  /**
   * Tests that a booking can be filtered by whether it is canceled or not.
   */
  @Test
  public void findBookingCtosByCanceled() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setCanceled(false);

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    for (BookingCto resultBooking : resultList) {
      assertThat(resultBooking.getBooking().getCanceled()).isFalse();
    }
  }

  /**
   * Tests that a booking can be filtered by bookingType.
   */
  @Test
  public void findBookingCtosByType() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setBookingType(BookingType.INVITED);

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    for (BookingCto resultBooking : resultList) {
      assertThat(resultBooking.getBooking().getBookingType()).isEqualTo(BookingType.INVITED);
    }
  }

  /**
   * Tests that a booking can be filtered by tableId.
   */
  @Test
  public void findBookingCtosByTableId() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setTableId(this.booking.getTableId());

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();

    List<BookingCto> resultList = result.toList();

    for (BookingCto resultBooking : resultList) {
      assertThat(resultBooking.getBooking().getTableId()).isEqualTo(this.booking.getTableId());
    }
  }

  /**
   * Tests that filtering a booking by showAll will show all bookings.
   */
  @Test
  public void findBookingCtosByTableShowAll() {

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setShowAll(true);

    Page<BookingCto> result = this.bookingmanagement.findBookingCtos(criteria);

    assertThat(result).isNotNull();
    assertThat(result).hasSize(22);
  }
}
