package com.devonfw.application.mtsj.usermanagement.logic.api.to;

import com.devonfw.module.basic.common.api.to.AbstractTo;

/**
 * Transfer Object for the password reset containing the password-reset-token and new password of a user
 *
 */
public class PasswordResetTo extends AbstractTo {

  private String token;

  private String password;

  /**
   * @return token
   */
  public String getToken() {

    return this.token;
  }

  /**
   * @param token new value of {@link #getToken}.
   */
  public void setToken(String token) {

    this.token = token;
  }

  /**
   * @return password
   */
  public String getPassword() {

    return this.password;
  }

  /**
   * @param password new value of {@link #getPassword}.
   */
  public void setPassword(String password) {

    this.password = password;
  }

}
