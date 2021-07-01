package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;
import net.sf.mmm.util.nls.api.NlsMessage;

import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;

/**
 * This exception is thrown a changed {@link OrderCto} is invalid.
 *
 */
public class InvalidChangedOrderException extends NlsRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param message the error {@link #getNlsMessage() message}.
   */
  public InvalidChangedOrderException(NlsMessage message) {

    super(message);
  }

  /**
   * The default constructor.
   */
  public InvalidChangedOrderException() {

    super("Changed Order is invalid");
  }

  /**
   * The constructor.
   *
   * @param message custom error message
   */
  public InvalidChangedOrderException(String message) {

    super(message);
  }

}
