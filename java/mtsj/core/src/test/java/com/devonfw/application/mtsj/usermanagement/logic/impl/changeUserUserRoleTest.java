package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;

/**
 * Tests for {@link Usermanagement#changeUserUserRole}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class changeUserUserRoleTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  private Long userId;

  @BeforeEach
  private void initUser() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("testUser");
    // userRegistrationEto.setModificationCounter(0);
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("valid@mail.de");
    userRegistrationEto.setUserRoleId(0L);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    this.userId = savedUser.getId();
  }

  @AfterEach
  private void teardown() {

    this.usermanagement.deleteUser(this.userId);
  }

  /**
   * Tests that changing the user role of a User is possible.
   */
  @Test
  public void changeUserUserRoleValid() {

    UserEto userEto = this.usermanagement.findUser(this.userId);
    Long newUserRoleId = 1L;

    userEto.setUserRoleId(newUserRoleId);

    assertThat(userEto.getUserRoleId()).isEqualTo(newUserRoleId);

    UserEto changedUserEto = this.usermanagement.changeUserUserRole(userEto);

    assertThat(changedUserEto.getUserRoleId()).isEqualTo(newUserRoleId);

  }

  /**
   * Tests that null as a parameter causes an exception.
   */
  @Test
  public void changeUserUserRoleUserEtoNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.changeUserUserRole(null);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that changing the userRoleId to an invalid id is not possible.
   */
  @Test
  public void changeUserUserRoleInvalidUserId() {

    UserEto userEto = this.usermanagement.findUser(this.userId);
    userEto.setId(-1L);
    userEto.setUserRoleId(1L);

    assertThatThrownBy(() -> {
      this.usermanagement.changeUserUserRole(userEto);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that changing the userRoleId of userId null is not possible.
   */
  @Test
  public void changeUserUserRoleUserIdNull() {

    UserEto userEto = this.usermanagement.findUser(this.userId);
    userEto.setId(null);
    userEto.setUserRoleId(1L);

    assertThatThrownBy(() -> {
      this.usermanagement.changeUserUserRole(userEto);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }

  /**
   * Tests that changing the userRoleId to an invalid id is not possible.
   */
  @Test
  public void changeUserUserRoleInvalidUserRoleId() {

    UserEto userEto = this.usermanagement.findUser(this.userId);
    userEto.setUserRoleId(-1L);

    assertThatThrownBy(() -> {
      this.usermanagement.changeUserUserRole(userEto);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }

  /**
   * Tests that changing the userRoleId to null is not possible.
   */
  @Test
  public void changeUserUserRoleUserRoleIdNull() {

    UserEto userEto = this.usermanagement.findUser(this.userId);
    userEto.setUserRoleId(null);

    assertThatThrownBy(() -> {
      this.usermanagement.changeUserUserRole(userEto);
    }).isInstanceOf(DataIntegrityViolationException.class);
  }
}