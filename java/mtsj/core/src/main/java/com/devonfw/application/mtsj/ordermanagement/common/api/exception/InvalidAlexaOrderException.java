package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * Exception thrown when the user makes an invalid Alexa order
 *
 */
public class InvalidAlexaOrderException extends NlsRuntimeException {

  /**
   * The constructor.
   *
   * @param message the error message
   */
  public InvalidAlexaOrderException(String message) {

    super(message);
  }

  /**
   * The default constructor.
   */
  public InvalidAlexaOrderException() {

    super("The Alexa order is invalid.");
  }
}
