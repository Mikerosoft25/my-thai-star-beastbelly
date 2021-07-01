package com.devonfw.application.mtsj.bookingmanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Exception thrown when a bookingId is invalid.
 *
 */
public class InvalidBookingIdException extends NlsRuntimeException {
  /**
   * The constructor.
   */
  public InvalidBookingIdException() {

    super("The BookingId is invalid.");
  }

  /**
   * The constructor with custom message.
   *
   * @param message message shown in the exception.
   */
  public InvalidBookingIdException(String message) {

    super(message);
  }
}
