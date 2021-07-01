package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.general.common.TestUtil;
import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;

/**
 * Tests for {@link Usermanagement#findUserByAuthorization}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findUserByAuthorizationTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  /**
   * Deletes the user from the DB after all tests.
   */
  @AfterAll
  public void cleanUp() {

    UserEto savedUserFromDb = this.usermanagement.findUserByName("testUser");

    if (savedUserFromDb != null) {
      this.usermanagement.deleteUser(savedUserFromDb.getId());
    }
  }

  /**
   * Tests that finding a logged in user is possible by his authorization.
   */
  @Test
  public void findUserByAuthorizationValid() {

    // login as testUser
    TestUtil.login("testUser", ApplicationAccessControlConfig.GROUP_CUSTOMER);

    UserRegistrationEto userRegistrationEto = new UserRegistrationEto();

    userRegistrationEto.setUsername("testUser");
    userRegistrationEto.setPassword("123456");
    userRegistrationEto.setEmail("valid@mail.de");
    userRegistrationEto.setUserRoleId(0L);

    this.usermanagement.saveUser(userRegistrationEto);

    UserEto savedUserFromDB = this.usermanagement.findUserByAuthorization();

    assertThat(savedUserFromDB).isNotNull();
    assertThat(savedUserFromDB.getUsername()).isEqualTo("testUser");
    assertThat(savedUserFromDB.getEmail()).isEqualTo("valid@mail.de");
    assertThat(savedUserFromDB.getUserRoleId()).isEqualTo(0L);

    TestUtil.logout();
  }

  /**
   * Tests that an exception is thrown if the user is not logged in.
   */
  @Test
  public void findUserByAuthorizationNotLoggedIn() {

    super.doTearDown();

    assertThatThrownBy(() -> {
      this.usermanagement.findUserByAuthorization();
    }).isInstanceOf(NullPointerException.class);
  }
}