package com.devonfw.application.mtsj.usermanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for User
 */
public interface User extends ApplicationEntity {

  /**
   * @return the username.
   */
  public String getUsername();

  /**
   * @param username the new username.
   */
  public void setUsername(String username);

  /**
   * @return the email.
   */
  public String getEmail();

  /**
   * @param email the new email.
   */
  public void setEmail(String email);

  /**
   * @return the userRoleId.
   */
  public Long getUserRoleId();

  /**
   * @param userRoleId the new userRoleId.
   */
  public void setUserRoleId(Long userRoleId);

  /**
   * @return the twoFactorStatus.
   */
  public boolean getTwoFactorStatus();

  /**
   * @param twoFactorStatus the new twoFactorStatus.
   */
  public void setTwoFactorStatus(boolean twoFactorStatus);

}
