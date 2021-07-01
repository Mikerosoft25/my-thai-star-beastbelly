package com.devonfw.application.mtsj.usermanagement.logic.api.to;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * Transport object of UserQrCode
 *
 */
public class UserQrCodeTo extends AbstractTo {

  private static final long serialVersionUID = 1L;

  private String base64QrCode;

  /*
   * TODO: Encrypted object during transfer must be enforced with HTTPS or similar encryption
   */
  private String secret;

  /**
   * The constructor.
   *
   * @param base64QrCode the base64QrCode.
   * @param secret the secret.
   */
  public UserQrCodeTo(String base64QrCode, String secret) {

    this.base64QrCode = base64QrCode;
    this.secret = secret;
  }

  /**
   * @return the base64QrCode;
   */
  public String getBase64QrCode() {

    return this.base64QrCode;
  }

  /**
   * @param base64QrCode the new base64QrCode to set.
   */
  public void setBase64QrCode(String base64QrCode) {

    this.base64QrCode = base64QrCode;
  }

  /**
   * @return the secret.
   */
  public String getSecret() {

    return this.secret;
  }

  /**
   * @param secret the new secret to set.
   */
  public void setSecret(String secret) {

    this.secret = secret;
  }
}
