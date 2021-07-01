package com.devonfw.application.mtsj.bookingmanagement.common.api;

import java.time.Instant;

import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for Bookings
 */
public interface Booking extends ApplicationEntity {

  /**
   * @return name.
   */
  public String getName();

  /**
   * @param name new name to set.
   */
  public void setName(String name);

  /**
   * @return bookingToken.
   */
  public String getBookingToken();

  /**
   * @param bookingToken new bookingToken to set.
   */
  public void setBookingToken(String bookingToken);

  /**
   * @return comment.
   */
  public String getComment();

  /**
   * @param comment new comment to set.
   */
  public void setComment(String comment);

  /**
   * @return bookingDate.
   */
  public Instant getBookingDate();

  /**
   * @param bookingDate new bookingDate to set.
   */
  public void setBookingDate(Instant bookingDate);

  /**
   * @return expirationDate.
   */
  public Instant getExpirationDate();

  /**
   * @param expirationDate new expirationDate to set.
   */
  public void setExpirationDate(Instant expirationDate);

  /**
   * @return creationDate.
   */
  public Instant getCreationDate();

  /**
   * @param creationDate new creationDate to set.
   */
  public void setCreationDate(Instant creationDate);

  /**
   * @return creationDate.
   */
  public String getEmail();

  /**
   * @param email new email to set.
   */
  public void setEmail(String email);

  /**
   * @return canceled.
   */
  public Boolean getCanceled();

  /**
   * @param canceled new canceled to set.
   */
  public void setCanceled(Boolean canceled);

  /**
   * @return bookingType.
   */
  public BookingType getBookingType();

  /**
   * @param bookingType new bookingType to set.
   */
  public void setBookingType(BookingType bookingType);

  /**
   * @return tableId.
   */
  public Long getTableId();

  /**
   * @param tableId new tableId to set.
   */
  public void setTableId(Long tableId);

  /**
   * @return orderId.
   */
  public Long getOrderId();

  /**
   * @param orderId new orderId to set.
   */
  public void setOrderId(Long orderId);

  /**
   * @return assistants.
   */
  public Integer getAssistants();

  /**
   * @param assistants new assistants to set.
   */
  public void setAssistants(Integer assistants);

  /**
   * @return userId.
   */
  public Long getUserId();

  /**
   * @param userId new userId to set.
   */
  public void setUserId(Long userId);

}
