package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusEto;

/**
 * Unit Tests for {@link Ordermanagement#findOrderPayStatusById(long)}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findOrderPayStatusByIdTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  /**
   * Tests that the correct order status is found.
   */
  @Test
  public void findOrderStatusValid() {

    // when
    OrderPayStatusEto orderPayStatus = this.ordermanagement.findOrderPayStatusById(0L);

    assertThat(orderPayStatus.getPayStatus()).isEqualTo("Pending");
  }

  /**
   * Tests that an exception is thrown if an invalid id is used.
   */
  @Test
  public void findOrderWithInvalidId() {

    OrderPayStatusEto orderPayStatus = this.ordermanagement.findOrderPayStatusById(-1L);

    assertThat(orderPayStatus).isNull();
  }

}
