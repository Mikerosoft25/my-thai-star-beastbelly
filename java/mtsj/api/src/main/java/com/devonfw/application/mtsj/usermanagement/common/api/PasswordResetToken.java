package com.devonfw.application.mtsj.usermanagement.common.api;

import java.time.Instant;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for PasswordResetToken
 *
 */
public interface PasswordResetToken extends ApplicationEntity {

  /**
   * @return token
   */

  public String getToken();

  /**
   * @param token setter for token attribute
   */

  public void setToken(String token);

  /**
   * getter for userId attribute
   *
   * @return userId
   */

  public Long getUserId();

  /**
   * @param userId setter for userId attribute
   */

  public void setUserId(Long userId);

  /**
   * @return expiryDateId
   */

  public Instant getExpiryDate();

  /**
   * @param expiryDate setter for expiryDate attribute
   */

  public void setExpiryDate(Instant expiryDate);

}
