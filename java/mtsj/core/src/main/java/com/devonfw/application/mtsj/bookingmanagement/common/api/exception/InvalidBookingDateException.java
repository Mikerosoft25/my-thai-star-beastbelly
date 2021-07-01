package com.devonfw.application.mtsj.bookingmanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Exception thrown when a booking date is invalid.
 *
 */
public class InvalidBookingDateException extends NlsRuntimeException {

  /**
   * The constructor with default message.
   */
  public InvalidBookingDateException() {

    super("The BookingDate is invalid");
  }

  /**
   * The constructor with custom message.
   *
   * @param message message shown in the exception.
   */
  public InvalidBookingDateException(String message) {

    super(message);
  }
}
