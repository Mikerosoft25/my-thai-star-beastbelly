package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusEto;

/**
 * Unit Tests for {@link Ordermanagement#findOrderStatusById}
 */
@SpringBootTest(classes = SpringBootApp.class)
@TestInstance(Lifecycle.PER_CLASS)
public class findOrderStatusByIdTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  /**
   * Tests that the correct order status is found.
   */
  @Test
  public void findOrderStatusValid() {

    // when
    OrderStatusEto orderStatus = this.ordermanagement.findOrderStatusById(0L);

    assertThat(orderStatus.getStatus()).isEqualTo("Received");
  }

  /**
   * Tests that an exception is thrown if an invalid id is used.
   */
  @Test
  public void findOrderWithInvalidId() {

    OrderStatusEto orderStatus = this.ordermanagement.findOrderStatusById(-1L);

    assertThat(orderStatus).isNull();
  }
}
