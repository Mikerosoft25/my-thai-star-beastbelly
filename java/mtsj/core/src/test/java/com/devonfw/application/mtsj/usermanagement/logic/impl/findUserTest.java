package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
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
 * Tests for {@link Usermanagement#findUser}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findUserTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

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
   * Tests that finding a user by its Id is possible.
   */
  @Test
  public void findUserValid() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("valid@mail.de");
    userRegistrationEto.setUserRoleId(0L);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());

    assertThat(savedUserFromDB).isNotNull();
    assertThat(savedUserFromDB.getUsername()).isEqualTo("testUser");
    assertThat(savedUserFromDB.getEmail()).isEqualTo("valid@mail.de");
    assertThat(savedUserFromDB.getUserRoleId()).isEqualTo(0L);
  }

  /**
   * Tests that an exception is thrown if a non-existent userId is used.
   */
  @Test
  public void findUserInvalidUserId() {

    assertThatThrownBy(() -> {
      this.usermanagement.findUser(-1L);
    }).isInstanceOf(EmptyResultDataAccessException.class);
  }

  /**
   * Tests that an exception is thrown if userId null is used.
   */
  @Test
  public void findUserUserIdNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.findUser(null);
    }).isInstanceOf(InvalidDataAccessApiUsageException.class);
  }
}