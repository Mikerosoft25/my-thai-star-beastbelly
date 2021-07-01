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
 * Tests for {@link Usermanagement#changeUser}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class changeUserTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  private String username = "testUser";

  private String username2 = "anotherTestUser";

  private String changedUsername = "changedUsername";

  private String password = "testPassword";

  private String password2 = "anotherTestPassword";

  private String changedPassword = "testPasswordChanged";

  private String email = "valid@mail.de";

  private String email2 = "valid2@mail.de";

  private String changedEmail = "anotherValid@mail.com";

  private Long userRoleId = 0L;

  /**
   * Deleting the created user after each test
   */
  @AfterEach
  public void cleanUp() {

    UserEto user = this.usermanagement.findUserByName(this.username);
    if (user != null) {
      this.usermanagement.deleteUser(user.getId());
    }
    UserEto user2 = this.usermanagement.findUserByName(this.username2);
    if (user2 != null) {
      this.usermanagement.deleteUser(user2.getId());
    }
  }

  /**
   * Tests that changing the details of a User is possible.
   */
  @Test
  public void changeUserValid() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    UserRegistrationEto changedUserRegistrationEto = new UserRegistrationEto();

    changedUserRegistrationEto.setId(savedUser.getId());
    changedUserRegistrationEto.setUsername(this.changedUsername);
    changedUserRegistrationEto.setPassword(this.changedPassword);
    changedUserRegistrationEto.setEmail(this.changedEmail);
    changedUserRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto changedUser = this.usermanagement.changeUser(changedUserRegistrationEto);

    assertThat(changedUser.getUsername()).isEqualTo(this.changedUsername);
    assertThat(changedUser.getEmail()).isEqualTo(this.changedEmail);
    assertThat(changedUser.getId()).isEqualTo(savedUser.getId());

    UserEto changedUserFromDB = this.usermanagement.findUser(savedUser.getId());

    assertThat(changedUserFromDB.getUsername()).isEqualTo(this.changedUsername);
    assertThat(changedUserFromDB.getEmail()).isEqualTo(this.changedEmail);
    assertThat(changedUserFromDB.getId()).isEqualTo(savedUser.getId());
  }

  /**
   * Tests that no changes happen if changeUser is called with no changes.
   */
  @Test
  public void changeUserValidWithNoChange() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    UserRegistrationEto changedUserRegistrationEto = new UserRegistrationEto();

    changedUserRegistrationEto.setId(savedUser.getId());
    changedUserRegistrationEto.setUsername(null);
    changedUserRegistrationEto.setPassword(null);
    changedUserRegistrationEto.setEmail(null);
    changedUserRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto changedUser = this.usermanagement.changeUser(changedUserRegistrationEto);

    assertThat(changedUser.getUsername()).isEqualTo(this.username);
    assertThat(changedUser.getEmail()).isEqualTo(this.email);
    assertThat(changedUser.getId()).isEqualTo(savedUser.getId());

    UserEto changedUserFromDB = this.usermanagement.findUser(savedUser.getId());

    assertThat(changedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(changedUserFromDB.getEmail()).isEqualTo(this.email);
    assertThat(changedUserFromDB.getId()).isEqualTo(savedUser.getId());
  }

  /**
   * Tests that calling changeUser() with null is not possible.
   */
  @Test
  public void changeUserNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.changeUser(null);
    }).isInstanceOf(NullPointerException.class);
  }

  /**
   * Tests that changing the username to an already taken username is not possible.
   */
  @Test
  public void changeUserUsernameAlreadyTaken() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // second user
    userRegistrationEto2.setUsername(this.username2);
    userRegistrationEto2.setPassword(this.password2);
    userRegistrationEto2.setEmail(this.email2);
    userRegistrationEto2.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);
    this.usermanagement.saveUser(userRegistrationEto2);

    UserRegistrationEto userRegistrationEto3 = new UserRegistrationEto();
    userRegistrationEto3.setId(savedUser.getId());
    userRegistrationEto3.setUsername(this.username2);

    assertThatThrownBy(() -> {
      this.usermanagement.changeUser(userRegistrationEto3);
    }).isInstanceOf(DataIntegrityViolationException.class);

    // checking that no changes were made
    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());
    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
  }

  /**
   * Tests that changing the username to a name with less than 4 characters is not possible.
   */
  @Test
  public void changeUserUsernameLessThan4Characters() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    userRegistrationEto2.setId(savedUser.getId());
    userRegistrationEto2.setUsername("abc");

    assertThatThrownBy(() -> {
      this.usermanagement.changeUser(userRegistrationEto2);
    }).isInstanceOf(RuntimeException.class);

    // checking that no changes were made
    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());
    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
  }

  /**
   * Tests that changing the username to a name with more than 20 characters is not possible.
   */
  @Test
  public void changeUserUsernameMoreThan20Characters() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    userRegistrationEto2.setId(savedUser.getId());
    userRegistrationEto2.setUsername("thisUserNameIsMoreThan20CharactersLong");

    assertThatThrownBy(() -> {
      this.usermanagement.changeUser(userRegistrationEto2);
    }).isInstanceOf(RuntimeException.class);

    // checking that no changes were made
    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());
    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
  }

  /**
   * Tests that changing the username to a name that contains a space-character is not possible.
   */
  @Test
  public void changeUserUsernameContainsSpace() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    userRegistrationEto2.setId(savedUser.getId());
    userRegistrationEto2.setUsername("User Name");

    assertThatThrownBy(() -> {
      this.usermanagement.changeUser(userRegistrationEto2);
    }).isInstanceOf(RuntimeException.class);

    // checking that no changes were made
    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());
    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
  }

  /**
   * Tests that changing the username to a password that contains less than 6 characters is not possible.
   */
  @Test
  public void changeUserPasswordLessThan6Characters() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    userRegistrationEto2.setId(savedUser.getId());
    userRegistrationEto2.setPassword("123");

    assertThatThrownBy(() -> {
      this.usermanagement.changeUser(userRegistrationEto2);
    }).isInstanceOf(RuntimeException.class);

    // checking that no changes were made
    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());
    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
  }

  /**
   * Tests that changing the email to a non-email form is not possible.
   */
  @Test
  public void changeUserInvalidEmail() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);

    userRegistrationEto2.setId(savedUser.getId());
    userRegistrationEto2.setEmail("invalidEmail");

    assertThatThrownBy(() -> {
      this.usermanagement.changeUser(userRegistrationEto2);
    }).isInstanceOf(TransactionSystemException.class);

    // checking that no changes were made
    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());
    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
  }

  /**
   * Tests that changing the email to an already taken email is not possible.
   */
  @Test
  public void changeUserEmailAlreadyTaken() {

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();
    UserRegistrationEto userRegistrationEto2 = new UserRegistrationEto();

    userRegistrationEto.setUsername(this.username);
    userRegistrationEto.setPassword(this.password);
    userRegistrationEto.setEmail(this.email);
    userRegistrationEto.setUserRoleId(this.userRoleId);

    // second user
    userRegistrationEto2.setUsername("anotherUser");
    userRegistrationEto2.setPassword("anotherTestPassword");
    userRegistrationEto2.setEmail("another@mail.de");
    userRegistrationEto2.setUserRoleId(this.userRoleId);

    UserEto savedUser = this.usermanagement.saveUser(userRegistrationEto);
    this.usermanagement.saveUser(userRegistrationEto2);

    UserRegistrationEto userRegistrationEto3 = new UserRegistrationEto();
    userRegistrationEto3.setId(savedUser.getId());
    userRegistrationEto3.setEmail("another@mail.de");

    assertThatThrownBy(() -> {
      this.usermanagement.changeUser(userRegistrationEto3);
    }).isInstanceOf(DataIntegrityViolationException.class);

    // checking that no changes were made
    UserEto savedUserFromDB = this.usermanagement.findUser(savedUser.getId());
    assertThat(savedUserFromDB.getUsername()).isEqualTo(this.username);
    assertThat(savedUserFromDB.getEmail()).isEqualTo(this.email);
  }

}