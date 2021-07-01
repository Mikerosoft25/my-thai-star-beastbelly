package com.devonfw.application.mtsj.usermanagement.logic.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.PasswordResetTokenEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.PasswordResetTokenRepository;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;

/**
 * Tests for {@link Usermanagement#findUserByPasswordResetToken}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findUserByPasswordResetTokenTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  @Inject
  private PasswordResetTokenRepository passwordResetTokenDao;

  /**
   * Deletes the user from the DB after each test.
   */
  @AfterEach
  public void cleanUp() {

    UserEto savedUserFromDb = this.usermanagement.findUserByName("testUser");

    if (savedUserFromDb != null) {
      this.usermanagement.deleteUser(savedUserFromDb.getId());
    }
  }

  /**
   * Tests that finding a user by password-reset-token is possible.
   */
  @Test
  public void findUserByPasswordResetTokenValid() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("validReset@mail.de");
    userRegistrationEto.setUserRoleId(0L);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);
    this.usermanagement.resetPassword(savedUser);
    List<PasswordResetTokenEntity> resetTokens = this.passwordResetTokenDao.findByUser(savedUser.getId());
    PasswordResetTokenEntity resetToken = resetTokens.get(0);

    UserEto foundUser = this.usermanagement.findUserByPasswordResetToken(resetToken.getToken());

    assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
    assertThat(foundUser.getUsername()).isEqualTo(savedUser.getUsername());
    assertThat(foundUser.getEmail()).isEqualTo(savedUser.getEmail());
    assertThat(foundUser.getUserRoleId()).isEqualTo(savedUser.getUserRoleId());
  }

  /**
   * Tests that an exception is thrown if an invalid token is used.
   */
  @Test
  public void findUserByPasswordResetTokenInvalid() {

    assertThatThrownBy(() -> {
      this.usermanagement.findUserByPasswordResetToken("notValidToken");
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that an exception is thrown if null as a token is used.
   */
  @Test
  public void findUserByPasswordResetTokenNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.findUserByPasswordResetToken(null);
    }).isInstanceOf(NullPointerException.class);
  }
}