package com.devonfw.application.mtsj.usermanagement.logic.api.to;

import java.time.Instant;

import com.devonfw.application.mtsj.usermanagement.common.api.PasswordResetToken;
import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * Entity transport object of PasswordResetToken
 */
public class PasswordResetTokenEto extends AbstractEto implements PasswordResetToken {

  private static final long serialVersionUID = 1L;

  private String token;

  private Long userId;

  private Instant expiryDate;

  @Override
  public String getToken() {

    return token;
  }

  @Override
  public void setToken(String token) {

    this.token = token;
  }

  @Override
  public Long getUserId() {

    return userId;
  }

  @Override
  public void setUserId(Long userId) {

    this.userId = userId;
  }

  @Override
  public Instant getExpiryDate() {

    return expiryDate;
  }

  @Override
  public void setExpiryDate(Instant expiryDate) {

    this.expiryDate = expiryDate;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.token == null) ? 0 : this.token.hashCode());

    result = prime * result + ((this.userId == null) ? 0 : this.userId.hashCode());
    result = prime * result + ((this.expiryDate == null) ? 0 : this.expiryDate.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    // class check will be done by super type EntityTo!
    if (!super.equals(obj)) {
      return false;
    }
    PasswordResetTokenEto other = (PasswordResetTokenEto) obj;
    if (this.token == null) {
      if (other.token != null) {
        return false;
      }
    } else if (!this.token.equals(other.token)) {
      return false;
    }

    if (this.userId == null) {
      if (other.userId != null) {
        return false;
      }
    } else if (!this.userId.equals(other.userId)) {
      return false;
    }
    if (this.expiryDate == null) {
      if (other.expiryDate != null) {
        return false;
      }
    } else if (!this.expiryDate.equals(other.expiryDate)) {
      return false;
    }
    return true;
  }
}
