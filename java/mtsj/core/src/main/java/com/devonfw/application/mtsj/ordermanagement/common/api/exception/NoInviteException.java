package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * This exception is thrown if the guest token of an
 * {@link com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto} has no
 * {@link com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity} related.
 *
 */
public class NoInviteException extends NlsRuntimeException {

  /**
   * The default constructor.
   */
  public NoInviteException() {

    super("The invitation does not exist");
  }
}
