package com.devonfw.application.mtsj.usermanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.jboss.aerogear.security.otp.api.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.general.common.api.UserProfile;
import com.devonfw.application.mtsj.general.common.api.datatype.Role;
import com.devonfw.application.mtsj.general.logic.api.to.UserDetailsClientTo;
import com.devonfw.application.mtsj.general.logic.base.AbstractComponentFacade;
import com.devonfw.application.mtsj.mailservice.logic.api.Mail;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.PasswordResetTokenEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserRoleEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.PasswordResetTokenRepository;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRepository;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRoleRepository;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.PasswordResetTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserQrCodeTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.logic.base.QrCodeService;

/**
 * Implementation of component interface of usermanagement
 */
@Named
@Transactional
public class UsermanagementImpl extends AbstractComponentFacade implements Usermanagement {

  private static final Logger LOG = LoggerFactory.getLogger(UsermanagementImpl.class);

  @Inject
  private Bookingmanagement bookingmanagement;

  @Inject
  private UserRepository userDao;

  @Inject
  private UserRoleRepository userRoleDao;

  @Inject
  private PasswordResetTokenRepository passwordResetTokenDao;

  @Inject
  private PasswordEncoder passwordEncoder;

  @Inject
  private Mail mailService;

  @Value("${client.port}")
  private int clientPort;

  /**
   * The constructor.
   */
  public UsermanagementImpl() {

    super();
  }

  @Override
  // @RolesAllowed(Roles.ADMIN)
  public UserEto findUser(Long id) {

    LOG.debug("Get User with id {} from database.", id);

    UserEto user = getBeanMapper().map(getUserDao().find(id), UserEto.class);
    user.setUserRoleName(getUserRoleDao().find(user.getUserRoleId()).getName());

    return user;
  }

  @Override
  public UserEto findUserByAuthorization() {

    String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    System.out.println(username);

    return findUserByName(username);
  }

  @Override
  public UserEto findUserByPasswordResetToken(String token) {

    PasswordResetTokenEntity tokenEntity = getPasswordResetTokenDAO().findByToken(token);
    UserEntity user = tokenEntity.getUser();

    return getBeanMapper().map(user, UserEto.class);
  }

  @Override
  public UserQrCodeTo generateUserQrCode(String username) {

    LOG.debug("Get User with username {} from database.", username);
    UserEntity user = getBeanMapper().map(getUserDao().findByUsername(username), UserEntity.class);
    initializeSecret(user);
    if (user != null && user.getTwoFactorStatus()) {
      return QrCodeService.generateQrCode(user);
    } else {
      return null;
    }
  }

  @Override
  public UserEto getUserStatus(String username) {

    LOG.debug("Get User with username {} from database.", username);
    return getBeanMapper().map(getUserDao().findByUsername(username), UserEto.class);
  }

  @Override
  public UserEto findUserByName(String userName) {

    UserEntity userEntity = getUserDao().findByUsername(userName);
    UserEto userEto = getBeanMapper().map(userEntity, UserEto.class);
    if (userEto != null) {
      userEto.setUserRoleName(getUserRoleDao().find(userEto.getUserRoleId()).getName());
    }

    return userEto;
  }

  @Override
  public Page<UserEto> findUserEtos(UserSearchCriteriaTo criteria) {

    Page<UserEntity> users = getUserDao().findUsers(criteria);
    Page<UserEto> userEtos = mapPaginatedEntityList(users, UserEto.class);

    for (UserEto user : userEtos) {
      user.setUserRoleName(getUserRoleDao().find(user.getUserRoleId()).getName());
    }

    return userEtos;
  }

  @Override
  public boolean deleteUser(Long userId) {

    UserEntity user = getUserDao().find(userId);
    getUserDao().delete(user);
    LOG.debug("The user with id '{}' has been deleted.", userId);
    return true;
  }

  @Override
  public UserEto saveUser(UserRegistrationEto user) {

    Objects.requireNonNull(user, "user");

    // setting default role to 0 which is user, only verified admins later on can change the userRole
    user.setUserRoleId(0L);

    UserEntity userEntity = getBeanMapper().map(user, UserEntity.class);

    validateUserDetails(userEntity);

    // encoding password with bcrypt
    userEntity.setPassword(encodePassword(userEntity.getPassword()));

    UserEntity resultEntity = getUserDao().save(userEntity);
    LOG.debug("User with id '{}' has been created.", resultEntity.getId());
    return getBeanMapper().map(resultEntity, UserEto.class);

  }

  @Override
  public UserEto saveUserByAdmin(UserRegistrationEto user) {

    Objects.requireNonNull(user, "user");

    UserEntity userEntity = getBeanMapper().map(user, UserEntity.class);

    validateUserDetails(userEntity);

    if (userEntity.getUserRoleId() == null) {
      userEntity.setUserRoleId(0L);
    }
    // encoding password with bcrypt
    userEntity.setPassword(encodePassword(userEntity.getPassword()));

    UserEntity resultEntity = getUserDao().save(userEntity);
    LOG.debug("User with id '{}' has been created.", resultEntity.getId());
    return getBeanMapper().map(resultEntity, UserEto.class);

  }

  @Override
  public UserEto changeUser(UserRegistrationEto user) {

    Objects.requireNonNull(user, "user");

    UserEntity currentUser = getUserDao().find(user.getId());

    // getting all the bookings of the user
    Pageable pageableBookings = PageRequest.of(0, 1000);
    BookingSearchCriteriaTo bookingCriteria = new BookingSearchCriteriaTo();
    bookingCriteria.setPageable(pageableBookings);
    bookingCriteria.setName(currentUser.getUsername());
    Page<BookingCto> bookings = this.bookingmanagement.findBookingCtos(bookingCriteria);

    if (user.getUsername() != null) {
      currentUser.setUsername(user.getUsername());
    }
    if (user.getEmail() != null) {
      currentUser.setEmail(user.getEmail());
    }
    // encoding password with bcrypt
    if (user.getPassword() != null) {
      if (user.getPassword().length() < 6) {
        throw new RuntimeException("Password must be at least 6 characters long");
      }
      currentUser.setPassword(encodePassword(user.getPassword()));
    }

    validateUserDetails(currentUser);

    // changing the userDetails of the bookings to the new user details
    if (bookings != null) {
      List<BookingCto> bookingsList = bookings.toList();

      for (BookingCto bookingCto : bookingsList) {
        bookingCto.getBooking().setName(currentUser.getUsername());
        bookingCto.getBooking().setEmail(currentUser.getEmail());

        this.bookingmanagement.changeBooking(bookingCto);
      }
    }

    UserEntity resultEntity = getUserDao().save(currentUser);
    LOG.debug("User with id '{}' has been created.", resultEntity.getId());
    return getBeanMapper().map(resultEntity, UserEto.class);

  }

  @Override
  public void resetPassword(UserEto user) {

    UserEntity userEntity = null;

    if (user.getId() != null) {
      try {
        userEntity = getUserDao().find(user.getId());
      } catch (EmptyResultDataAccessException e) {
        throw new RuntimeException("No User exists with the given ID");
      }
    } else {
      throw new RuntimeException("No ID specified in request");
    }

    String token = UUID.randomUUID().toString();

    Instant expiryDate = Instant.now().plus(1, ChronoUnit.HOURS);

    PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
    passwordResetTokenEntity.setToken(token);
    passwordResetTokenEntity.setUser(userEntity);
    passwordResetTokenEntity.setExpiryDate(expiryDate);

    getPasswordResetTokenDAO().save(passwordResetTokenEntity);
    sendPasswordResetEmail(userEntity, token);

  }

  @Override
  public void changePassword(PasswordResetTo passwordResetTo) {

    String newPassword = passwordResetTo.getPassword();
    String token = passwordResetTo.getToken();

    if (!validPasswordResetToken(token)) {
      throw new RuntimeException("Reset-Token is invalid or has expired");
    } else if (newPassword.length() < 6) {
      throw new RuntimeException("Password is too short");
    }

    PasswordResetTokenEntity passwordResetTokenEntity = getPasswordResetTokenDAO().findByToken(token);
    UserEntity user = passwordResetTokenEntity.getUser();
    user.setPassword(encodePassword(newPassword));

    getUserDao().save(user);
    getPasswordResetTokenDAO().delete(passwordResetTokenEntity);

  }

  @Override
  public UserEto changeUserUserRole(UserEto user) {

    Objects.requireNonNull(user, "user");

    UserEntity userEntity = getUserDao().find(user.getId());

    userEntity.setUserRoleId(user.getUserRoleId());

    UserEntity resultEntity = getUserDao().save(userEntity);
    return getBeanMapper().map(resultEntity, UserEto.class);
  }

  @Override
  public UserEto saveUserTwoFactor(UserEto user) {

    Objects.requireNonNull(user, "user");
    UserEntity userEntity = getBeanMapper().map(getUserDao().findByUsername(user.getUsername()), UserEntity.class);

    // initialize, validate userEntity here if necessary
    userEntity.setTwoFactorStatus(user.getTwoFactorStatus());
    UserEntity resultEntity = getUserDao().save(userEntity);
    LOG.debug("User with id '{}' has been modified.", resultEntity.getId());
    return getBeanMapper().map(resultEntity, UserEto.class);
  }

  /**
   * Returns the field 'userDao'.
   *
   * @return the {@link UserRepository} instance.
   */
  public UserRepository getUserDao() {

    return this.userDao;
  }

  @Override
  public UserRoleEto findUserRole(Long id) {

    LOG.debug("Get UserRole with id {} from database.", id);
    return getBeanMapper().map(getUserRoleDao().find(id), UserRoleEto.class);
  }

  @Override
  public Page<UserRoleEto> findUserRoleEtos(UserRoleSearchCriteriaTo criteria) {

    Page<UserRoleEntity> userroles = getUserRoleDao().findUserRoles(criteria);
    return mapPaginatedEntityList(userroles, UserRoleEto.class);
  }

  /**
   * Returns the field 'userRoleDao'.
   *
   * @return the {@link UserRoleRepository} instance.
   */
  public UserRoleRepository getUserRoleDao() {

    return this.userRoleDao;
  }

  /**
   * Returns the field 'passwordResetTokenDao'.
   *
   * @return the {@link UserRoleRepository} instance.
   */
  public PasswordResetTokenRepository getPasswordResetTokenDAO() {

    return this.passwordResetTokenDao;
  }

  @Override
  public UserProfile findUserProfileByLogin(String login) {

    UserEto userEto = findUserByName(login);
    UserDetailsClientTo profile = new UserDetailsClientTo();
    profile.setId(userEto.getId());
    profile.setRole(Role.getRoleById(userEto.getUserRoleId()));
    return profile;
  }

  /**
   * Validates if fields of the {@link UserEntity} are valid.
   *
   * @param userEntity The {@link UserEntity} to validate.
   *
   * @throws RuntimeException when:
   *
   *         <ul>
   *         <li>Username has less than 4 characters
   *         <li>Username has more than 20 characters
   *         <li>Password has less than 6 characters
   *         <li>Password is null
   *         </ul>
   *
   */
  private void validateUserDetails(UserEntity userEntity) {

    if (userEntity.getUsername().length() < 4 || userEntity.getUsername().length() > 20) {
      throw new RuntimeException("Username must be between 4 and 20 characters long");
    } else if (userEntity.getUsername().contains(" ")) {
      throw new RuntimeException("Username cannot contain a space-character");
    } else if (userEntity.getPassword() == null) {
      throw new RuntimeException("Password cannot be null");
    } else if (userEntity.getPassword().length() < 6) {
      throw new RuntimeException("Password must be at least 6 characters long");
    }
  }

  /**
   * Encodes a password.
   *
   * @param password The password to encode.
   * @return the encoded password.
   */
  private String encodePassword(String password) {

    return this.passwordEncoder.encode(password);
  }

  /**
   * Assigns a randomly generated secret for an specific user
   *
   * @param user
   */
  private void initializeSecret(UserEntity user) {

    if (user.getSecret() == null) {
      user.setSecret(Base32.random());
      UserEntity resultEntity = getUserDao().save(user);
      LOG.debug("User with id '{}' has been modified.", resultEntity.getId());
    }
  }

  /**
   * Validates a password-reset-token.
   *
   * @param token the token to validate.
   * @return true if the token is valid, false otherwise.
   */
  private boolean validPasswordResetToken(String token) {

    PasswordResetTokenEntity passwordResetTokenEntity = getPasswordResetTokenDAO().findByToken(token);

    // no such token exists
    if (passwordResetTokenEntity == null) {
      return false;
    } else {
      // token expired
      if (Instant.now().compareTo(passwordResetTokenEntity.getExpiryDate()) > 0) {
        return false;
      }

      return true;
    }

  }

  /**
   * Sends a password-reset-email to a user.
   *
   * @param user the user who will receive the password-reset-email.
   * @param resetToken the password-reset-token.
   */
  private void sendPasswordResetEmail(UserEntity user, String resetToken) {

    Objects.requireNonNull(user, "user");

    String resetURL = getClientUrl() + "/password-reset?token=" + resetToken;

    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getUsername());
    templateModel.put("resetURL", resetURL);

    this.mailService.sendPasswordResetMail(user.getEmail(), templateModel);
  }

  private String getClientUrl() {

    HttpServletRequest request = null;
    String clientUrl = null;

    if (RequestContextHolder.getRequestAttributes() != null) {
      request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    if (request != null) {
      clientUrl = request.getHeader("origin");
    }
    if (clientUrl == null) {
      return "http://localhost:" + this.clientPort;
    }
    return clientUrl;
  }
}