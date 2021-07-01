package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;

/**
 * Tests for {@link Usermanagement#saveUserByAdmin}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SaveUserByAdminTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  private String username = "testUser";

  private String password = "testPassword";

  private String email = "valid@mail.de";

  private Long userRoleId = 1L;

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
   * Tests that saving a user by an admin with a userRole is possible.
   */
  @Test
  public void saveUserValidWithCustomRole() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUserByAdmin(userRegistrationEto);

    assertThat(savedUser.getUsername()).isEqualTo(this.username);
    assertThat(savedUser.getEmail()).isEqualTo(this.email);
    assertThat(savedUser.getUserRoleId()).isEqualTo(this.userRoleId);

    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());

    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
    assertThat(savedUserFromDB.getUserRoleId()).isEqualTo(this.userRoleId);
  }

  /**
   * Tests that saving a user by an admin with the userRole null saves a customer.
   */
  @Test
  public void saveUserValidWithRoleNull() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(null);

    UserEto savedUser = this.usermanagement.saveUserByAdmin(userRegistrationEto);

    assertThat(savedUser.getUsername()).isEqualTo(this.username);
    assertThat(savedUser.getEmail()).isEqualTo(this.email);
    assertThat(savedUser.getUserRoleId()).isEqualTo(0L);

    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());

    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
    assertThat(savedUserFromDB.getUserRoleId()).isEqualTo(0L);
  }

  /**
   * Tests that using null results in an exception.
   */
  @Test
  public void saveUserNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.saveUserByAdmin(null);
    }).isInstanceOf(NullPointerException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user by an admin with an already taken username will result in an exception.
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
    userRegistrationEto.setUsername(this.username);
    userRegistrationEto2.setPassword("anotherTestPassword");
    userRegistrationEto2.setEmail("another@mail.de");
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUserByAdmin(userRegistrationEto);

    assertThat(savedUser).isNotNull();

    // error when saving a user with the same username
    assertThatThrownBy(() -> {
      this.usermanagement.saveUserByAdmin(userRegistrationEto2);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that saving a user by an admin with an already taken username will result in an exception and no user will be
   * saved.
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
      this.usermanagement.saveUserByAdmin(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user by an admin with a username with more than 20 characters will result in an exception and
   * no user will be saved.
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
      this.usermanagement.saveUserByAdmin(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that saving a user by an admin with a username containing a space-characters will result in an exception and
   * no user will be saved.
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
      this.usermanagement.saveUserByAdmin(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that using null as a parameter will result in an exception and no user will be saved.
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
      this.usermanagement.saveUserByAdmin(userRegistrationEto);
    }).isInstanceOf(NullPointerException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that using null as a parameter will result in an exception and no user will be saved.
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
      this.usermanagement.saveUserByAdmin(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that an exception is thrown if the password is null.
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
      this.usermanagement.saveUserByAdmin(userRegistrationEto);
    }).isInstanceOf(RuntimeException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that an exception is thrown if the email of the user to save is not in an email-form.
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
      this.usermanagement.saveUserByAdmin(userRegistrationEto);
    }).isInstanceOf(TransactionSystemException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that an exception is thrown if the email of the user to save is null.
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
      this.usermanagement.saveUserByAdmin(userRegistrationEto);
    }).isInstanceOf(DataIntegrityViolationException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNull();
  }

  /**
   * Tests that an exception is thrown if the email of the user to save is alrady in use by another user.
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

    UserEto savedUser = this.usermanagement.saveUserByAdmin(userRegistrationEto);

    assertThat(savedUser).isNotNull();

    // error when saving a user with the same email
    assertThatThrownBy(() -> {
      this.usermanagement.saveUserByAdmin(userRegistrationEto2);
    }).isInstanceOf(DataIntegrityViolationException.class);

    assertThat(this.usermanagement.findUserByName(this.username)).isNotNull();
    assertThat(this.usermanagement.findUserByName("testUser2")).isNull();
  }
}