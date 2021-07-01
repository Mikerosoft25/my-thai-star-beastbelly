package com.devonfw.application.mtsj.usermanagement.logic.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserRegistrationEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserSearchCriteriaTo;

/**
 * Tests for {@link Usermanagement#findUserEtos(UserSearchCriteriaTo)}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findUserEtosTest extends ApplicationComponentTest {

  @Inject
  private Usermanagement usermanagement;

  private UserEto user;

  /**
   * Creates a booking and order before all tests.
   */
  @BeforeAll
  public void startup() {

    UserRegistrationEto newUser = new UserRegistrationEto();
    newUser.setUsername("UserSearchTest");
    newUser.setPassword("password");
    newUser.setEmail("UserSearchTest@mail.com");

    this.user = this.usermanagement.saveUser(newUser);

  }

  /**
   * Deletes the booking after all tests are finished.
   */
  @AfterAll
  public void cleanup() {

    this.usermanagement.deleteUser(this.user.getId());
  }

  /**
   * Tests that a user can be found by filtering by username.
   */
  @Test
  public void findUserEtosByUsername() {

    UserSearchCriteriaTo criteria = new UserSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setUsername(this.user.getUsername());

    Page<UserEto> result = this.usermanagement.findUserEtos(criteria);

    assertThat(result).isNotNull();

    List<UserEto> resultList = result.toList();

    // checking that the only result is the above created user
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getId()).isEqualTo(this.user.getId());
  }

  /**
   * Tests that a user can be found by filtering by email.
   */
  @Test
  public void findUserEtosByEmail() {

    UserSearchCriteriaTo criteria = new UserSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    criteria.setEmail(this.user.getEmail());

    Page<UserEto> result = this.usermanagement.findUserEtos(criteria);

    assertThat(result).isNotNull();

    List<UserEto> resultList = result.toList();

    // checking that the only result is the above created user
    assertThat(resultList).hasSize(1);
    assertThat(resultList.get(0).getId()).isEqualTo(this.user.getId());
  }

  /**
   * Tests that a user can be found by filtering by user role.
   */
  @Test
  public void findUserEtosByUserrole() {

    UserSearchCriteriaTo criteria = new UserSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);

    // only customers
    criteria.setUserRoleId(0L);

    Page<UserEto> result = this.usermanagement.findUserEtos(criteria);

    assertThat(result).isNotNull();

    List<UserEto> resultList = result.toList();

    // checking that all returned users are customers
    for (UserEto resultUser : resultList) {
      assertThat(resultUser.getUserRoleId()).isEqualTo(0L);
    }
  }
}
