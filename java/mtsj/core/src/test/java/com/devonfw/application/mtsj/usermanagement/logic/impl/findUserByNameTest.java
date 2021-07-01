package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;

/**
 * Tests for {@link Usermanagement#findUserByName}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findUserByNameTest extends ApplicationComponentTest {
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
   * Tests that finding a user out of the DB is possible.
   */
  @Test
  public void findUserValid() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("testPassword");
    userRegistrationEto.setEmail("valid@mail.de");
    userRegistrationEto.setUserRoleId(0L);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    UserEto savedUserFromDB = this.usermanagement.findUserByName("testUser");

    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getUsername()).isEqualTo("testUser");
    assertThat(savedUser.getEmail()).isEqualTo("valid@mail.de");
    assertThat(savedUser.getUserRoleId()).isEqualTo(0L);

    assertThat(savedUserFromDB).isNotNull();
    assertThat(savedUserFromDB.getUsername()).isEqualTo("testUser");
    assertThat(savedUserFromDB.getEmail()).isEqualTo("valid@mail.de");
    assertThat(savedUserFromDB.getUserRoleId()).isEqualTo(0L);
  }

  /**
   * Tests that no user is found with a non-existent username.
   */
  @Test
  public void findUserByNameInvalidUserName() {

    UserEto user = this.usermanagement.findUserByName("nameDoesNotExist");

    assertThat(user).isNull();
  }

  /**
   * Tests that no user is found when using username null.
   */
  @Test
  public void findUserByNameNameNull() {

    UserEto user = this.usermanagement.findUserByName(null);

    assertThat(user).isNull();
  }
}