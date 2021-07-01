package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Exception thrown when changing the orderStatus is not allowed
 *
 */
public class ChangingOrderStatusNotAllowedException extends NlsRuntimeException {

  /**
   * The constructor.
   *
   * @param message the error message
   */
  public ChangingOrderStatusNotAllowedException(String message) {

    super(message);
  }

  /**
   * The default constructor.
   */
  public ChangingOrderStatusNotAllowedException() {

    super("Changing the orderPayStatus of this order is not allowed.");
  }
}
