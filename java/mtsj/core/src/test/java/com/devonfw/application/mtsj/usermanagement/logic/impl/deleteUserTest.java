package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;

/**
 * Tests for {@link Usermanagement#deleteUser}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class deleteUserTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  /**
   * Tests that a user is deleted from the DB.
   */
  @Test
  public void deleteUserValid() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("valid@mail.de");
    userRegistrationEto.setUserRoleId(0L);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    boolean deleted = this.usermanagement.deleteUser(savedUser.getId());
    assertThat(deleted).isTrue();
    assertThat(this.usermanagement.findUserByName("testUser")).isNull();
  }

  /**
   * Tests that a user is deleted from the DB.
   */
  @Test
  public void deleteUserInvalidUserId() {

    assertThatThrownBy(() -> {
      this.usermanagement.deleteUser(-1L);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that null as a parameter causes an exception.
   */
  @Test
  public void deleteUserUserIdNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.deleteUser(null);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }
}