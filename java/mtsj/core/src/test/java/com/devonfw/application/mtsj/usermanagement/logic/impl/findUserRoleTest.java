package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRoleEto;

/**
 * Tests for {@link Usermanagement#findUserRole}.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findUserRoleTest extends ApplicationComponentTest {
  @Inject
  private Usermanagement usermanagement;

  /**
   * Tests that finding a userRole by its id is possible.
   */
  @Test
  public void findUserRoleValid() {

    UserRoleEto userRole = this.usermanagement.findUserRole(0L);

    assertThat(userRole).isNotNull();
    assertThat(userRole.getName()).isEqualTo("Customer");
  }

  /**
   * Tests that an exception is thrown if an invalid UserRoleId is used.
   */
  @Test
  public void findUserInvalidUserId() {

    assertThatThrownBy(() -> {
      this.usermanagement.findUserRole(-1L);
    }).isInstanceOf(Exception.class);
  }

  /**
   * Tests that an exception is thrown if UserRoleId is null.
   */
  @Test
  public void findserUserIdNull() {

    assertThatThrownBy(() -> {
      this.usermanagement.findUserRole(null);
    }).isInstanceOf(Exception.class);
  }
}