package com.devonfw.application.mtsj.bookingmanagement.logic.api.to;

import com.devonfw.module.basic.common.api.to.AbstractCto;

/**
 * Composite transport object of InvitedGuest
 */
public class InvitedGuestCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private InvitedGuestEto invitedGuest;

  private BookingEto booking;

  /**
   * @return the {@link InvitedGuestEto}.
   */
  public InvitedGuestEto getInvitedGuest() {

    return this.invitedGuest;
  }

  /**
   * @param invitedGuest {@link InvitedGuestEto} to set.
   */
  public void setInvitedGuest(InvitedGuestEto invitedGuest) {

    this.invitedGuest = invitedGuest;
  }

  /**
   * @return the {@link BookingEto}.
   */
  public BookingEto getBooking() {

    return this.booking;
  }

  /**
   * @param booking {@link BookingEto} to set.
   */
  public void setBooking(BookingEto booking) {

    this.booking = booking;
  }
}
