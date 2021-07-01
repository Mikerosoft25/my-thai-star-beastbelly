package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Exception thrown when changing the orderPayStatus is not allowed
 *
 */
public class ChangingOrderPayStatusNotAllowedException extends NlsRuntimeException {

  /**
   * The constructor.
   *
   * @param message the error message.
   */
  public ChangingOrderPayStatusNotAllowedException(String message) {

    super(message);
  }

  /**
   * The default constructor.
   */
  public ChangingOrderPayStatusNotAllowedException() {

    super("Changing the orderPayStatus of this order is not allowed.");
  }
}
