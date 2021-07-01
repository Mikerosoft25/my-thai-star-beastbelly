package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * This exception is thrown if the token of an
 * {@link com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto} has no
 * {@link com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity} related.
 *
 */
public class NoBookingException extends NlsRuntimeException {

  /**
   * The constructor.
   */
  public NoBookingException() {

    super("The booking does not exist");
  }

}
