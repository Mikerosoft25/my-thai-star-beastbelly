package com.devonfw.application.mtsj.usermanagement.service.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.PasswordResetTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserQrCodeTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;

/**
 * The service interface for REST calls in order to execute the logic of component {@link Usermanagement}.
 */
@Path("/usermanagement/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UsermanagementRestService {

  /**
   * Delegates to {@link Usermanagement#findUser}.
   *
   * @param id the ID of the {@link UserEto}.
   * @return the {@link UserEto}.
   */
  @GET
  @Path("/user/{id}/")
  public UserEto getUser(@PathParam("id") long id);

  /**
   * Delegates to {@link Usermanagement#findUserByAuthorization}.
   *
   * @return the {@link UserEto}.
   */
  @GET
  @Path("/userdetails")
  public UserEto getUserByAuthorization();

  /**
   * Delegates to {@link Usermanagement#findUserByPasswordResetToken}.
   *
   * @param resetToken the password-reset-token of the user.
   *
   * @return the {@link UserEto} linked to the password-reset-token.
   */
  @GET
  @Path("/user/resettoken/{resetToken}/")
  public UserEto getUserByPasswordResetToken(@PathParam("resetToken") String resetToken);

  /**
   * Delegates to {@link Usermanagement#generateUserQrCode}.
   *
   * @param username the username of the {@link UserQrCodeTo}.
   * @return the {@link UserQrCodeTo}.
   */
  @GET
  @Path("/user/pairing/{username}/")
  public UserQrCodeTo getUserQrCode(@PathParam("username") String username);

  /**
   * Delegates to {@link Usermanagement#saveUser}.
   *
   * @param user the {@link UserEto} to be saved.
   * @return the created {@link UserEto}.
   */
  @POST
  @Path("/user/register")
  public UserEto saveUser(UserRegistrationEto user);

  /**
   * Delegates to {@link Usermanagement#saveUserByAdmin}.
   *
   * @param user the {@link UserEto} to be saved.
   * @return the created {@link UserEto}.
   */
  @POST
  @Path("/user/registerbyadmin")
  public UserEto saveUserByAdmin(UserRegistrationEto user);

  /**
   * Delegates to {@link Usermanagement#changeUser}.
   *
   * @param user {@link UserRegistrationEto} with the changed user details.
   *
   * @return the changed {@link UserEto} that has been updated in the database.
   */
  @POST
  @Path("/user/changeuser")
  public UserEto changeUser(UserRegistrationEto user);

  /**
   * Delegates to {@link Usermanagement#resetPassword}.
   *
   * @param user the {@link UserEto} of the user whose password will be reset.
   */
  @POST
  @Path("/user/resetPassword")
  public void resetPassword(UserEto user);

  /**
   * Delegates to {@link Usermanagement#changePassword}.
   *
   * @param passwordResetTo user the {@link PasswordResetTo} containing the password-reset-token and the new password.
   */
  @POST
  @Path("/user/changePassword")
  public void changePassword(PasswordResetTo passwordResetTo);

  /**
   * Delegates to {@link Usermanagement#changeUserUserRole}.
   *
   * @param user the {@link UserEto} containing the users information and new user role.
   *
   * @return the changed {@link UserEto} that has been updated in the database.
   */
  @POST
  @Path("/user/changerole")
  public UserEto changeUserUserRole(UserEto user);

  /**
   * Delegates to {@link Usermanagement#getUserStatus}.
   *
   * @param username the {@link UserEto} to be saved.
   * @return the recently created {@link UserEto}.
   */
  @GET
  @Path("/user/twofactor/{username}")
  public UserEto getUserStatus(@PathParam("username") String username);

  /**
   * Delegates to {@link Usermanagement#saveUserTwoFactor}.
   *
   * @param user the {@link UserEto} to be saved
   * @return the recently created {@link UserEto}
   */
  @POST
  @Path("/user/twofactor")
  public UserEto saveUserTwoFactor(UserEto user);

  /**
   * Delegates to {@link Usermanagement#deleteUser}.
   *
   * @param id ID of the {@link UserEto} to be deleted
   */
  @DELETE
  @Path("/user/{id}/")
  public void deleteUser(@PathParam("id") long id);

  /**
   * Delegates to {@link Usermanagement#findUserEtos}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding users.
   * @return the {@link Page} of matching {@link UserEto}s.
   */
  @Path("/user/search")
  @POST
  public Page<UserEto> findUsersByPost(UserSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Usermanagement#findUserRole}.
   *
   * @param id the ID of the {@link UserRoleEto}
   * @return the {@link UserRoleEto}
   */
  @GET
  @Path("/userrole/{id}/")
  public UserRoleEto getUserRole(@PathParam("id") long id);

  /**
   * Delegates to {@link Usermanagement#findUserRoleEtos}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding userroles.
   * @return the {@link Page} of matching {@link UserRoleEto}s.
   */
  @Path("/userrole/search")
  @POST
  public Page<UserRoleEto> findUserRolesByPost(UserRoleSearchCriteriaTo searchCriteriaTo);

}
