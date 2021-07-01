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
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusSearchCriteriaTo;

/**
 * Tests for {@link Ordermanagement#findOrderPayStatus(OrderPayStatusSearchCriteriaTo)} component.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class findOrderPayStatusTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement ordermanagement;

  /**
   * This test gets all the available dishes using an empty SearchCriteria object
   */
  @Test
  public void findAllOrderPayStatus() {

    OrderPayStatusSearchCriteriaTo criteria = new OrderPayStatusSearchCriteriaTo();
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "payStatus"));
    criteria.setPageable(pageable);
    Page<OrderPayStatusEto> result = this.ordermanagement.findOrderPayStatus(criteria);
    assertThat(result).isNotNull();
    assertThat(result.getContent().get(0).getPayStatus()).isEqualTo("Pending");
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterOrderPayStatus() {

    OrderPayStatusSearchCriteriaTo criteria = new OrderPayStatusSearchCriteriaTo();
    criteria.setPayStatus("Pending");
    PageRequest pageable = PageRequest.of(0, 100);
    criteria.setPageable(pageable);
    Page<OrderPayStatusEto> result = this.ordermanagement.findOrderPayStatus(criteria);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isEqualTo(1);
    assertThat(result.getContent().get(0).getPayStatus()).isEqualTo("Pending");
  }

}
