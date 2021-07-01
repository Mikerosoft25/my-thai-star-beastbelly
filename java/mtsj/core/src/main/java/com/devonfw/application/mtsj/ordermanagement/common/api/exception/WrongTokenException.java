package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;
import net.sf.mmm.util.nls.api.NlsMessage;

/**
 * This exception is thrown if the token of an
 * {@link com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto} is not well formed (wrong prefix).
 *
 */
public class WrongTokenException extends NlsRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param message the error {@link #getNlsMessage() message}.
   */
  public WrongTokenException(NlsMessage message) {

    super(message);
  }

  /**
   * The default constructor.
   */
  public WrongTokenException() {

    super("Not a valid token");
  }

  /**
   * The constructor.
   *
   * @param message custom error message
   */
  public WrongTokenException(String message) {

    super(message);
  }

}
