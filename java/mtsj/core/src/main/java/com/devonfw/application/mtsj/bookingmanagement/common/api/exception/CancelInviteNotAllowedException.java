package com.devonfw.application.mtsj.bookingmanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Exception thrown when cancelling an invite is not allowed.
 *
 */
public class CancelInviteNotAllowedException extends NlsRuntimeException {

  /**
   * The constructor.
   */
  public CancelInviteNotAllowedException() {

    super("The booking can not be cancelled.");
  }
}
