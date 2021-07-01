package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.general.common.TestUtil;
import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;

/**
 * Unit Tests for {@link Bookingmanagement#findBookingsByUser}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findBookingsByUserTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  /**
   * Tests that all 3 bookings of the mock user "Lena123" are found.
   */
  @Test
  public void findBookingsByUserValid() {

    TestUtil.login("Lena123", ApplicationAccessControlConfig.GROUP_CUSTOMER);

    Pageable pageable = PageRequest.of(0, 20);
    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    criteria.setPageable(pageable);

    List<BookingCto> bookingsByUser = this.bookingmanagement.findBookingsByUser(criteria).toList();

    assertThat(bookingsByUser).hasSize(3);

    BookingEto booking1 = bookingsByUser.get(0).getBooking();
    BookingEto booking2 = bookingsByUser.get(1).getBooking();
    BookingEto booking3 = bookingsByUser.get(2).getBooking();

    assertThat(booking1.getBookingToken()).isEqualTo("CB_20170509_123502552Z");
    assertThat(booking1.getName()).isEqualTo("Lena123");
    assertThat(booking1.getEmail()).isEqualTo("Lena.Weber@mail.com");
    assertThat(booking1.getBookingType()).isEqualTo(BookingType.COMMON);
    assertThat(booking1.getAssistants()).isEqualTo(5);

    assertThat(booking2.getBookingToken()).isEqualTo("CB_20170509_123502555Z");
    assertThat(booking2.getName()).isEqualTo("Lena123");
    assertThat(booking2.getEmail()).isEqualTo("Lena.Weber@mail.com");
    assertThat(booking2.getBookingType()).isEqualTo(BookingType.COMMON);
    assertThat(booking2.getAssistants()).isEqualTo(2);

    assertThat(booking3.getBookingToken()).isEqualTo("CB_20170509_123502557Z");
    assertThat(booking3.getName()).isEqualTo("Lena123");
    assertThat(booking3.getEmail()).isEqualTo("Lena.Weber@mail.com");
    assertThat(booking3.getBookingType()).isEqualTo(BookingType.COMMON);
    assertThat(booking3.getAssistants()).isEqualTo(5);

    TestUtil.logout();
  }

  /**
   * Tests that no bookings are found if the user has not made any bookings.
   */
  @Test
  public void findBookingsByUserWithUserWithoutBookings() {

    TestUtil.login("userWithoutBookings", ApplicationAccessControlConfig.GROUP_CUSTOMER);

    BookingSearchCriteriaTo criteria = new BookingSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    Page<BookingCto> bookingsByUser = this.bookingmanagement.findBookingsByUser(criteria);

    assertThat(bookingsByUser).isNull();

    TestUtil.logout();
  }

}
