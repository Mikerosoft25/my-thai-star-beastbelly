package com.devonfw.application.mtsj.usermanagement.logic.api.to;

import java.util.List;

import com.devonfw.module.basic.common.api.to.AbstractCto;

/**
 * Composite transport object of UserRole
 */
public class UserRoleCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private UserRoleEto userRole;

  private List<UserEto> users;

  /**
   * @return the {@link UserRoleEto}.
   */
  public UserRoleEto getUserRole() {

    return this.userRole;
  }

  /**
   * @param userRole the {@link UserRoleEto} to set.
   */
  public void setUserRole(UserRoleEto userRole) {

    this.userRole = userRole;
  }

  /**
   * @return the {@link List} of {@link UserEto}s.
   */
  public List<UserEto> getUsers() {

    return this.users;
  }

  /**
   * @param users the {@link List} of {@link UserEto}s to set.
   */
  public void setUsers(List<UserEto> users) {

    this.users = users;
  }

}
