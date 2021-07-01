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
 * Tests for {@link Usermanagement#saveUser}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SaveUserTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  private String username = "testUser";

  private String password = "testPassword";

  private String email = "valid@mail.de";

  private Long userRoleId = 0L;

  /**
   * Deleting the created user after each test.
   */
  @AfterEach
  public void cleanUp() {

    UserEto user = this.usermanagement.findUserByName(this.username);
    if (user != null) {
      this.usermanagement.deleteUser(user.getId());
    }
  }

  /**
   * Tests that a user is succesfully saved in the DB.
   */
  @Test
  public void saveUserValid() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    assertThat(savedUser.getUsername()).isEqualTo(this.username);
    assertThat(savedUser.getEmail()).isEqualTo(this.email);
    assertThat(savedUser.getUserRoleId()).isEqualTo(this.userRoleId);

    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());

    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
    assertThat(savedUserFromDB.getUserRoleId()).isEqualTo(this.userRoleId);

  }

  /**
   * Tests that null as a parameter causes an exception.
   */
  @Test
  public void saveUserNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(null);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that saving a user with a username that is already taken by another user causes an exception.
   */
  @Test
  public void saveUserUsernameAlreadyTaken() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // same Username, different mail + password
    userRegistrationEto2.setUsername(this.username);
    userRegistrationEto2.setPassword("anotherTestPassword");
    userRegistrationEto2.setEmail("another@mail.de");
    userRegistrationEto2.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    assertThat(savedUser).isNotNull();

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto2);
    }).isInstanceOf(RuntimeException.class);
  }

  /**
   * Tests that saving a user with a username with less than 4 characters causes an exception.
   */
  @Test
  public void saveUserUsernameLessThan4Characters() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("abc");
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user with a username with more than 20 characters causes an exception.
   */
  @Test
  public void saveUserUsernameMoreThan20Characters() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("thisUsernameIsLongerThan20Characters");
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user with a username with more than 20 characters causes an exception.
   */
  @Test
  public void saveUserUsernameContainsSpace() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("User Name");
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user with null as username causes an exception.
   */
  @Test
  public void saveUserUsernameNull() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(null);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user with a password with less than 6 characters causes an exception.
   */
  @Test
  public void saveUserPasswordLessThan6Characters() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword("123");
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user with null as a password causes an exception.
   */
  @Test
  public void saveUserPasswordNull() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(null);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user with an email that is not in email-form will cause an exception.
   */
  @Test
  public void saveUserInvalidEmail() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail("noValidEmailForm");
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user with null as email will cause an exception.
   */
  @Test
  public void saveUserEmailNull() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(null);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user with null as email will cause an exception.
   */
  @Test
  public void saveUserEmailAlreadyTaken() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // different username, but same email
    userRegistrationEto2.setUsername("testUser2");
    userRegistrationEto2.setPassword("anotherTestPassword");
    userRegistrationEto2.setEmail(this.email);
    userRegistrationEto2.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    assertThat(savedUser).isNotNull();

    // error when saving a user with the same email
    assertThatThrownBy(() -> {
      this.usermanagement.saveUser(userRegistrationEto2);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName("testUser2")).isNull();
  }

  /**
   * Tests that when saving a user with a role other than Customer the role is still set to Customer.
   */
  @Test
  public void saveUserUserRoleIdNotZero() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(2L);

    assertThat(userRegistrationEto.getUserRoleId()).isEqualTo(2);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    assertThat(savedUser.getUserRoleId()).isEqualTo(0);
  }
}