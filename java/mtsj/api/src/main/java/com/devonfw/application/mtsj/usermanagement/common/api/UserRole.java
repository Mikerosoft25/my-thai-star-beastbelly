package com.devonfw.application.mtsj.usermanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for UserRole
 *
 */
public interface UserRole extends ApplicationEntity {

  /**
   * @return the name of the role.
   */
  public String getName();

  /**
   * @param name the new name of the role.
   */
  public void setName(String name);

  /**
   * @return true if the role is active, otherwise false.
   */
  public Boolean getActive();

  /**
   * @param active the new active-status of the role.
   */
  public void setActive(Boolean active);

}
