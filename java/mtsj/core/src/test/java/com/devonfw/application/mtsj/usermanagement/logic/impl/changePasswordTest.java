package com.devonfw.application.mtsj.usermanagement.logic.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.PasswordResetTokenEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.PasswordResetTokenRepository;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRepository;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.PasswordResetTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;

/**
 * Tests for {@link Usermanagement#changePassword}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class changePasswordTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  @Inject
  private UserRepository userDao;

  @Inject
  private PasswordResetTokenRepository passwordResetTokenDao;

  /**
   * Deleting the User after each test from the DB.
   */
  @AfterEach
  public void cleanUp() {

    UserEto savedUserFromDb = this.usermanagement.findUserByName("testUser");

    if (savedUserFromDb != null) {
      this.usermanagement.deleteUser(savedUserFromDb.getId());
    }
  }

  /**
   * Tests that changing the password after resetting is possible.
   */
  @Test
  public void changePasswordValid() {

    // saving a new user to the database
    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("validReset@mail.de");
    userRegistrationEto.setUserRoleId(0L);
    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    // requesting a password reset for the user und getting the reset token
    this.usermanagement.resetPassword(savedUser);
    List<PasswordResetTokenEntity> resetTokens = this.passwordResetTokenDao.findByUser(savedUser.getId());
    PasswordResetTokenEntity resetToken = resetTokens.get(0);

    // User before password was changed
    UserEntity userBeforePasswordChanged = this.userDao.find(savedUser.getId());

    PasswordResetTo passwordResetTo = new PasswordResetTo();
    passwordResetTo.setToken(resetToken.getToken());
    passwordResetTo.setPassword("newPassword");
    this.usermanagement.changePassword(passwordResetTo);

    // User after password was changed
    UserEntity userAfterPasswordChanged = this.userDao.find(savedUser.getId());

    // checking that the password has changed
    assertThat(userBeforePasswordChanged.getPassword()).isNotEqualTo(userAfterPasswordChanged);

    // checking that the resetToken is deleted from the DB
    resetTokens = this.passwordResetTokenDao.findByUser(savedUser.getId());
    assertThat(resetTokens.size()).isEqualTo(0);
  }

  /**
   * Tests that changing the password is not possible once the resetToken expired.
   */
  @Test
  public void changePasswordExpiredResetToken() {

    // saving a new user to the database
    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("validReset@mail.de");
    userRegistrationEto.setUserRoleId(0L);
    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    // manually creating an expired passwordResetToken associated witht the user and saving it to the database
    PasswordResetTokenEntity passwordResetToken = new PasswordResetTokenEntity();
    passwordResetToken.setModificationCounter(0);
    String token = UUID.randomUUID().toString();
    passwordResetToken.setToken(token);
    passwordResetToken.setExpiryDate(Instant.now().minus(1, ChronoUnit.HOURS));
    passwordResetToken.setUserId(savedUser.getId());
    this.passwordResetTokenDao.save(passwordResetToken);

    // checking that the resetToken was saved
    PasswordResetTokenEntity savedToken = this.passwordResetTokenDao.findByToken(token);
    assertThat(savedToken).isNotNull();

    // PasswordResetTo with the expired token
    PasswordResetTo passwordResetTo = new PasswordResetTo();
    passwordResetTo.setToken(token);
    passwordResetTo.setPassword("newPassword");

    assertThatThrownBy(() -> {
      this.usermanagement.changePassword(passwordResetTo);
    }).isInstanceOf(RuntimeException.class);

  }

  /**
   * Tests that changing the password with an invalid token is not possible.
   */
  @Test
  public void changePasswordInvalidResetToken() {

    PasswordResetTo passwordResetTo = new PasswordResetTo();
    passwordResetTo.setToken("nonExistingToken");
    passwordResetTo.setPassword("newPassword");

    assertThatThrownBy(() -> {
      this.usermanagement.changePassword(passwordResetTo);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that changing the password with an null as token is not possible.
   */
  @Test
  public void changePasswordResetTokenNull() {

    PasswordResetTo passwordResetTo = new PasswordResetTo();
    passwordResetTo.setToken(null);
    passwordResetTo.setPassword("newPassword");

    assertThatThrownBy(() -> {
      this.usermanagement.changePassword(null);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that changing the password to a new password with less than 6 characters is not possible.
   */
  @Test
  public void changePasswordPasswordLessThan6Characters() {

    // saving a new user to the database
    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("validReset@mail.de");
    userRegistrationEto.setUserRoleId(0L);
    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    // requesting a password reset for the user und getting the reset token
    this.usermanagement.resetPassword(savedUser);
    List<PasswordResetTokenEntity> resetTokens = this.passwordResetTokenDao.findByUser(savedUser.getId());
    PasswordResetTokenEntity resetToken = resetTokens.get(0);

    PasswordResetTo passwordResetTo = new PasswordResetTo();
    passwordResetTo.setToken(resetToken.getToken());
    passwordResetTo.setPassword("12345");

    assertThatThrownBy(() -> {
      this.usermanagement.changePassword(passwordResetTo);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that changing the password to null is not possible.
   */
  @Test
  public void changePasswordPasswordNull() {

    // saving a new user to the database
    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("validReset@mail.de");
    userRegistrationEto.setUserRoleId(0L);
    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    // requesting a password reset for the user und getting the reset token
    this.usermanagement.resetPassword(savedUser);
    List<PasswordResetTokenEntity> resetTokens = this.passwordResetTokenDao.findByUser(savedUser.getId());
    PasswordResetTokenEntity resetToken = resetTokens.get(0);

    PasswordResetTo passwordResetTo = new PasswordResetTo();
    passwordResetTo.setToken(resetToken.getToken());
    passwordResetTo.setPassword(null);

    assertThatThrownBy(() -> {
      this.usermanagement.changePassword(passwordResetTo);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that changing the password with null as parameter is not possible.
   */
  @Test
  public void changePasswordNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.changePassword(null);
    }).isInstanceOf(NullPointerException.class);
  }
}