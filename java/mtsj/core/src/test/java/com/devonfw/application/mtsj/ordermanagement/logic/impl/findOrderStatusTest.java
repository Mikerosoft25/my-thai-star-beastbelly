package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusSearchCriteriaTo;

/**
 * Tests for {@link Ordermanagement#findOrderStatus(OrderStatusSearchCriteriaTo)} component.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class findOrderStatusTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  /**
   * This test gets all the available dishes using an empty SearchCriteria object
   */
  @Test
  public void findAllOrderStatus() {

    OrderStatusSearchCriteriaTo criteria = new OrderStatusSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "status"));
    criteria.setPageable(pageable);
    Page<OrderStatusEto> result = this.ordermanagement.findOrderStatus(criteria);
    assertThat(result).isNotNull();
    assertThat(result.getContent().get(0).getStatus()).isEqualTo("Received");
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterOrderStatus() {

    OrderStatusSearchCriteriaTo criteria = new OrderStatusSearchCriteriaTo();
    criteria.setStatus("Delivered");
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);
    Page<OrderStatusEto> result = this.ordermanagement.findOrderStatus(criteria);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isEqualTo(1);
    assertThat(result.getContent().get(0).getStatus()).isEqualTo("Delivered");
  }

}
