package com.devonfw.application.mtsj.usermanagement.logic.api.to;

import java.time.Instant;

import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * {@link AbstractSearchCriteriaTo} to find instances of
 * {@link com.devonfw.application.mtsj.usermanagement.common.api.PasswordResetToken}s.
 */
public class PasswordResetTokenSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String token;

  private Long userId;

  private Instant expiryDate;

  private StringSearchConfigTo tokenOption;

  /**
   * @return tokenId
   */

  public String getToken() {

    return this.token;
  }

  /**
   * @param token setter for token attribute
   */

  public void setToken(String token) {

    this.token = token;
  }

  /**
   * getter for userId attribute
   *
   * @return userId
   */

  public Long getUserId() {

    return this.userId;
  }

  /**
   * @param userId setter for userId attribute
   */

  public void setUserId(Long userId) {

    this.userId = userId;
  }

  /**
   * @return expiryDateId
   */

  public Instant getExpiryDate() {

    return this.expiryDate;
  }

  /**
   * @param expiryDate setter for expiryDate attribute
   */

  public void setExpiryDate(Instant expiryDate) {

    this.expiryDate = expiryDate;
  }

  /**
   * @return the {@link StringSearchConfigTo} used to search for {@link #getToken() token}.
   */
  public StringSearchConfigTo getTokenOption() {

    return this.tokenOption;
  }

  /**
   * @param tokenOption new value of {@link #getTokenOption()}.
   */
  public void setTokenOption(StringSearchConfigTo tokenOption) {

    this.tokenOption = tokenOption;
  }

}
