package com.devonfw.application.mtsj.usermanagement.logic.api.to;

import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * used to find {@link com.devonfw.application.mtsj.usermanagement.common.api.User}s.
 *
 */
public class UserSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private String username;

  private String email;

  private Long userRoleId;

  private StringSearchConfigTo usernameOption;

  private StringSearchConfigTo passwordOption;

  private StringSearchConfigTo emailOption;

  /**
   * The constructor.
   */
  public UserSearchCriteriaTo() {

    super();
  }

  /**
   * @return the username.
   */
  public String getUsername() {

    return this.username;
  }

  /**
   * @param username new value of {@link #getUsername}.
   */
  public void setUsername(String username) {

    this.username = username;
  }

  /**
   * @return the email.
   */
  public String getEmail() {

    return this.email;
  }

  /**
   * @param email new value of {@link #getEmail}.
   */
  public void setEmail(String email) {

    this.email = email;
  }

  /**
   * @return the userRoleId.
   */
  public Long getUserRoleId() {

    return this.userRoleId;
  }

  /**
   * @param userRoleId new value of {@link #getUserRoleId}.
   */
  public void setUserRoleId(Long userRoleId) {

    this.userRoleId = userRoleId;
  }

  /**
   * @return usernameOption
   */
  public StringSearchConfigTo getUsernameOption() {

    return this.usernameOption;
  }

  /**
   * @param usernameOption new value of {@link #getUsernameOption}.
   */
  public void setUsernameOption(StringSearchConfigTo usernameOption) {

    this.usernameOption = usernameOption;
  }

  /**
   * @return passwordOption
   */
  public StringSearchConfigTo getPasswordOption() {

    return this.passwordOption;
  }

  /**
   * @param passwordOption new value of {@link #getPasswordOption}.
   */
  public void setPasswordOption(StringSearchConfigTo passwordOption) {

    this.passwordOption = passwordOption;
  }

  /**
   * @return emailOption
   */
  public StringSearchConfigTo getEmailOption() {

    return this.emailOption;
  }

  /**
   * @param emailOption new value of {@link #getEmailOption}.
   */
  public void setEmailOption(StringSearchConfigTo emailOption) {

    this.emailOption = emailOption;
  }

}
