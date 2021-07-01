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
 * Tests for {@link Usermanagement#resetPassword}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class resetPasswordTest extends ApplicationComponentTest {
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
   * Tests that the password-reset is initiated succesfully.
   */
  @Test
  public void resetPasswordValid() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("validReset@mail.de");
    userRegistrationEto.setUserRoleId(0L);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    assertThat(this.usermanagement.findUser(savedUser.getId())).isNotNull();

    this.usermanagement.resetPassword(savedUser);

    List<PasswordResetTokenEntity> resetTokens = this.passwordResetTokenDao.findByUser(savedUser.getId());

    assertThat(resetTokens.size()).isEqualTo(1);

    PasswordResetTokenEntity resetToken = resetTokens.get(0);

    assertThat(resetToken.getUserId()).isEqualTo(savedUser.getId());

  }

  /**
   * Tests that an exception is thrown if the password-reset is initiated with an invalid userId.
   */
  @Test
  public void resetPasswordInvalidUserId() {

    UserEto userRegistrationEto = new UserEto();

    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setEmail("validReset@mail.de");
    userRegistrationEto.setUserRoleId(0L);
    userRegistrationEto.setId(-1L);

    assertThatThrownBy(() -> {
      this.usermanagement.resetPassword(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an exception is thrown if the password-reset is initiated with an invalid userId.
   */
  @Test
  public void resetPasswordInvalidUserIdNull() {

    UserEto userRegistrationEto = new UserEto();

    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setEmail("validReset@mail.de");
    userRegistrationEto.setUserRoleId(0L);
    userRegistrationEto.setId(null);

    assertThatThrownBy(() -> {
      this.usermanagement.resetPassword(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that an exception is thrown if the user is null.
   */
  @Test
  public void resetPasswordUserNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.resetPassword(null);
    }).isInstanceOf(NullPointerException.class);
  }
}