package com.devonfw.application.mtsj.usermanagement.dataaccess.api;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.usermanagement.common.api.PasswordResetToken;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link PasswordResetToken}.
 */
@Entity
@Table(name = "PasswordResetToken")
public class PasswordResetTokenEntity extends ApplicationPersistenceEntity implements PasswordResetToken {

  private String token;

  private UserEntity user;

  private Instant expiryDate;

  private static final long serialVersionUID = 1L;

  /**
   * @return token
   */
  @Override
  public String getToken() {

    return this.token;
  }

  /**
   * @param token new value of {@link #getToken}.
   */
  @Override
  public void setToken(String token) {

    this.token = token;
  }

  /**
   * @return user
   */
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idUser")
  public UserEntity getUser() {

    return this.user;
  }

  /**
   * @param user new value of {@link #getUser}.
   */
  public void setUser(UserEntity user) {

    this.user = user;
  }

  /**
   * @return expiryDate
   */
  @Override
  public Instant getExpiryDate() {

    return this.expiryDate;
  }

  /**
   * @param expiryDate new value of {@link #getExpiryDate}.
   */
  @Override
  public void setExpiryDate(Instant expiryDate) {

    this.expiryDate = expiryDate;
  }

  @Override
  @Transient
  public Long getUserId() {

    if (this.user == null) {
      return null;
    }
    return this.user.getId();
  }

  @Override
  public void setUserId(Long userId) {

    if (userId == null) {
      this.user = null;
    } else {
      UserEntity userEntity = new UserEntity();
      userEntity.setId(userId);
      this.user = userEntity;
    }
  }

}
