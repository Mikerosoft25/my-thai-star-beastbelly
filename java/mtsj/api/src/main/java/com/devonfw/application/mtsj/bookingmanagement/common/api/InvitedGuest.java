package com.devonfw.application.mtsj.bookingmanagement.common.api;

import java.time.Instant;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for InvitedGuests
 */
public interface InvitedGuest extends ApplicationEntity {
  /**
   * @return the bookingId.
   */
  public Long getBookingId();

  /**
   * @param bookingId new bookingId to set.
   */
  public void setBookingId(Long bookingId);

  /**
   * @return the guestToken.
   */
  public String getGuestToken();

  /**
   * @param guestToken new guestToken to set.
   */
  public void setGuestToken(String guestToken);

  /**
   * @return the email.
   */
  public String getEmail();

  /**
   * @param email new email to set.
   */
  public void setEmail(String email);

  /**
   * @return the accepted.
   */
  public Boolean getAccepted();

  /**
   * @param accepted new accepted to set.
   */
  public void setAccepted(Boolean accepted);

  /**
   * @return the modificationDate.
   */
  public Instant getModificationDate();

  /**
   * @param modificationDate new modificationDate to set.
   */
  public void setModificationDate(Instant modificationDate);

}
