package com.devonfw.application.mtsj.usermanagement.logic.api;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.general.common.api.UserProfile;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.PasswordResetTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserQrCodeTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;

/**
 * Interface for Usermanagement component.
 */
public interface Usermanagement {

  /**
   * Returns a User by its id 'id'.
   *
   * @param id The id 'id' of the User.
   * @return The {@link UserEto} with id 'id'
   */
  UserEto findUser(Long id);

  /**
   * Returns a User by its authorization bearer-token in the request.
   *
   * @return The {@link UserEto} matching the username in the bearer-token.
   */
  UserEto findUserByAuthorization();

  /**
   * Returns a User that is linked to a password-reset-token.
   *
   * @param token the password-reset-token.
   * @return The {@link UserEto} linked to the password-reset-token.
   */
  UserEto findUserByPasswordResetToken(String token);

  /**
   * Returns a User by its userName 'userName'.
   *
   * @param userName The userName 'userName' of the User.
   * @return The {@link UserEto} with userName 'userName'
   */
  UserEto findUserByName(String userName);

  /**
   * Returns a paginated list of Users matching the search criteria.
   *
   * @param criteria the {@link UserSearchCriteriaTo}.
   * @return the {@link Page} of matching {@link UserEto}s.
   */
  Page<UserEto> findUserEtos(UserSearchCriteriaTo criteria);

  /**
   * Deletes a user from the database by its id 'userId'.
   *
   * @param userId Id of the user to delete.
   * @return boolean <code>true</code> if the user can be deleted, <code>false</code> otherwise.
   */
  boolean deleteUser(Long userId);

  /**
   * Saves a user and stores it in the database.
   *
   * @param user the {@link UserRegistrationEto} to create.
   * @return the new {@link UserEto} that has been saved with ID and version.
   */
  UserEto saveUser(UserRegistrationEto user);

  /**
   * Saves a user by an admin and stores it in the database. Allows to set the user role in the
   * {@link UserRegistrationEto}.
   *
   * @param user the {@link UserRegistrationEto} to create.
   * @return the new {@link UserEto} that has been saved with ID and version.
   */
  UserEto saveUserByAdmin(UserRegistrationEto user);

  /**
   * Changes the user role of a user.
   *
   * @param user {@link UserEto} containing the users information and new user role.
   * @return the changed {@link UserEto} that has been updated in the database.
   */
  UserEto changeUserUserRole(UserEto user);

  /**
   * Pick the boolean and persist it for the user.
   *
   * @param user the {@link UserEto} to create.
   * @return the new {@link UserEto} that has been saved with ID and version.
   */
  UserEto saveUserTwoFactor(UserEto user);

  /**
   * Only returns the necessary info.
   *
   * @param username the {@link String} to create.
   * @return the new {@link UserEto} that has been saved with ID and version.
   */
  UserEto getUserStatus(String username);

  /**
   * Create an QR Code to the belonging user
   *
   * @param username the {@link UserQrCodeTo} to create.
   * @return the created {@link UserQrCodeTo}.
   */
  UserQrCodeTo generateUserQrCode(String username);

  /**
   * Returns a UserRole by its id 'id'.
   *
   * @param id The id 'id' of the UserRole.
   * @return The {@link UserRoleEto} with id 'id'
   */
  UserRoleEto findUserRole(Long id);

  /**
   * Returns a paginated list of UserRoles matching the search criteria.
   *
   * @param criteria the {@link UserRoleSearchCriteriaTo}.
   * @return the {@link Page} of matching {@link UserRoleEto}s.
   */
  Page<UserRoleEto> findUserRoleEtos(UserRoleSearchCriteriaTo criteria);

  /**
   * @param login The login of the requested user.
   * @return The {@link UserProfile} with the given <code>login</code> or {@code null} if no such object exists.
   */
  UserProfile findUserProfileByLogin(String login);

  /**
   * Initiates a password reset of a user.
   *
   * @param user the {@link UserEto} of the user whose password will be reset.
   */
  void resetPassword(UserEto user);

  /**
   * Changes the password of a user.
   *
   * @param passwordResetTo user the {@link PasswordResetTo} containing the password-reset-token and the new password.
   */
  void changePassword(PasswordResetTo passwordResetTo);

  /**
   * Changes the details of a user.
   *
   * @param user the {@link UserRegistrationEto} containing the changed user details.
   * @return the changed {@link UserEto} that has been updated in the database.
   */
  UserEto changeUser(UserRegistrationEto user);

}
