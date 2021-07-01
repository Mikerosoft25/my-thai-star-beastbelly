package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Unit Tests for {@link Bookingmanagement#findBookingByGuestToken}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findBookingByGuestTokenTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingmanagement;

  private String name = "testUser";

  private String email = "testUser@mail.com";

  private String comment = "test";

  // setting the time to 05.07.2021 15:00:00
  private Instant bookingDate = Instant.ofEpochMilli(1625490000);

  /**
   * Tests that a booking can be succesfully found by using a guest token.
   */
  @Test
  public void findBookingByGuestTokenValid() {

    // Saving a booking and the invited guest

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

    // Getting the guestToken

    BookingEto savedBooking = this.bookingmanagement.saveBooking(bookingCto);

    BookingCto savedBookingFromDb = this.bookingmanagement.findBooking(savedBooking.getId());

    String guestToken = savedBookingFromDb.getInvitedGuests().get(0).getGuestToken();

    // Finding the booking by Guesttoken
    BookingEto bookingByGuestTokenEto = this.bookingmanagement.findBookingByGuestToken(guestToken);

    assertThat(bookingByGuestTokenEto.getBookingToken()).isEqualTo(savedBooking.getBookingToken());
    assertThat(bookingByGuestTokenEto.getBookingDate()).isEqualTo(this.bookingDate);
    assertThat(bookingByGuestTokenEto.getName()).isEqualTo(this.name);
    assertThat(bookingByGuestTokenEto.getEmail()).isEqualTo(this.email);
    assertThat(bookingByGuestTokenEto.getComment()).isEqualTo(this.comment);
    assertThat(bookingByGuestTokenEto.getBookingType()).isEqualTo(BookingType.INVITED);
    assertThat(bookingByGuestTokenEto.getAssistants()).isEqualTo(3);
  }

  /**
   * Tests that an exception is thrown if an invalid guestToken is used.
   */
  @Test
  public void findBookingByGuestTokenInvalid() {

    assertThatThrownBy(() -> {
      this.bookingmanagement.findBookingByGuestToken("invalidToken");
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that an exception is thrown if null is used as parameter.
   */
  @Test
  public void findBookingByGuestTokenNull() {

    assertThatThrownBy(() -> {
      this.bookingmanagement.findBookingByGuestToken(null);
    }).isInstanceOf(NullPointerException.class);
  }

}
