package com.devonfw.application.mtsj.usermanagement.service.impl;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.PasswordResetTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserQrCodeTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.service.api.rest.UsermanagementRestService;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Usermanagement}.
 */
@Named("UsermanagementRestService")
public class UsermanagementRestServiceImpl implements UsermanagementRestService {

  @Inject
  private Usermanagement usermanagement;

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_USER)
  public UserEto getUser(long id) {

    return this.usermanagement.findUser(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_USER_BY_AUTHORIZATION)
  public UserEto getUserByAuthorization() {

    return this.usermanagement.findUserByAuthorization();
  }

  @Override
  @PermitAll
  public UserEto getUserByPasswordResetToken(String resetToken) {

    return this.usermanagement.findUserByPasswordResetToken(resetToken);
  }

  @Override
  @PermitAll
  public UserQrCodeTo getUserQrCode(String username) {

    return this.usermanagement.generateUserQrCode(username);
  }

  @Override
  @PermitAll
  public UserEto saveUser(UserRegistrationEto user) {

    return this.usermanagement.saveUser(user);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CHANGE_USER)
  public UserEto changeUser(UserRegistrationEto user) {

    return this.usermanagement.changeUser(user);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_USER_BY_ADMIN)
  public UserEto saveUserByAdmin(UserRegistrationEto user) {

    return this.usermanagement.saveUserByAdmin(user);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_RESET_PASSWORD)
  public void resetPassword(UserEto user) {

    this.usermanagement.resetPassword(user);
  }

  @Override
  @PermitAll
  public void changePassword(PasswordResetTo passwordResetTo) {

    this.usermanagement.changePassword(passwordResetTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CHANGE_USER_USER_ROLE)
  public UserEto changeUserUserRole(UserEto user) {

    return this.usermanagement.changeUserUserRole(user);
  }

  @Override
  @PermitAll
  public UserEto getUserStatus(String username) {

    return this.usermanagement.getUserStatus(username);
  }

  @Override
  @PermitAll
  public UserEto saveUserTwoFactor(UserEto user) {

    return this.usermanagement.saveUserTwoFactor(user);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_USER)
  public void deleteUser(long id) {

    this.usermanagement.deleteUser(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_USERS)
  public Page<UserEto> findUsersByPost(UserSearchCriteriaTo searchCriteriaTo) {

    return this.usermanagement.findUserEtos(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_USER_ROLE)
  public UserRoleEto getUserRole(long id) {

    return this.usermanagement.findUserRole(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_USER_ROLES)
  public Page<UserRoleEto> findUserRolesByPost(UserRoleSearchCriteriaTo searchCriteriaTo) {

    return this.usermanagement.findUserRoleEtos(searchCriteriaTo);
  }

}
