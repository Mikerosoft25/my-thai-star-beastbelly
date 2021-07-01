package com.devonfw.application.mtsj.bookingmanagement.logic.api.to;

import java.time.Instant;

import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * used to find {@link com.devonfw.application.mtsj.bookingmanagement.common.api.InvitedGuest}s.
 */
public class InvitedGuestSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Long bookingId;

  private String guestToken;

  private String email;

  private Boolean accepted;

  private Instant modificationDate;

  private StringSearchConfigTo guestTokenOption;

  private StringSearchConfigTo emailOption;

  /**
   * The constructor.
   */
  public InvitedGuestSearchCriteriaTo() {

    super();
  }

  /**
   * @return bookingId
   */
  public Long getBookingId() {

    return this.bookingId;
  }

  /**
   * @param bookingId new value of {@link #getBookingId}.
   */
  public void setBookingId(Long bookingId) {

    this.bookingId = bookingId;
  }

  /**
   * @return guestToken
   */
  public String getGuestToken() {

    return this.guestToken;
  }

  /**
   * @param guestToken new value of {@link #getGuestToken}.
   */
  public void setGuestToken(String guestToken) {

    this.guestToken = guestToken;
  }

  /**
   * @return email
   */
  public String getEmail() {

    return this.email;
  }

  /**
   * @param email new value of {@link #getEmail}.
   */
  public void setEmail(String email) {

    this.email = email;
  }

  /**
   * @return accepted
   */
  public Boolean getAccepted() {

    return this.accepted;
  }

  /**
   * @param accepted new value of {@link #getAccepted}.
   */
  public void setAccepted(Boolean accepted) {

    this.accepted = accepted;
  }

  /**
   * @return modificationDate
   */
  public Instant getModificationDate() {

    return this.modificationDate;
  }

  /**
   * @param modificationDate new value of {@link #getModificationDate}.
   */
  public void setModificationDate(Instant modificationDate) {

    this.modificationDate = modificationDate;
  }

  /**
   * @return guestTokenOption
   */
  public StringSearchConfigTo getGuestTokenOption() {

    return this.guestTokenOption;
  }

  /**
   * @param guestTokenOption new value of {@link #getGuestTokenOption}.
   */
  public void setGuestTokenOption(StringSearchConfigTo guestTokenOption) {

    this.guestTokenOption = guestTokenOption;
  }

  /**
   * @return emailOption
   */
  public StringSearchConfigTo getEmailOption() {

    return this.emailOption;
  }

  /**
   * @param emailOption new value of {@link #getEmailOption}.
   */
  public void setEmailOption(StringSearchConfigTo emailOption) {

    this.emailOption = emailOption;
  }

}
