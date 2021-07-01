package com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo;

import static com.querydsl.core.alias.Alias.$;

import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderStatusEntity;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusSearchCriteriaTo;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * {@link DefaultRepository} for {@link OrderStatusEntity}
 */
public interface OrderStatusRepository extends DefaultRepository<OrderStatusEntity> {

  /**
   * @param criteria the {@link OrderStatusSearchCriteriaTo} with the criteria to search.
   * @return the {@link Page} of the {@link OrderStatusEntity} objects that matched the search. If no pageable is set,
   *         it will return a unique page with all the objects that matched the search.
   */
  default Page<OrderStatusEntity> findByCriteria(OrderStatusSearchCriteriaTo criteria) {

    OrderStatusEntity alias = newDslAlias();
    JPAQuery<OrderStatusEntity> query = newDslQuery(alias);

    String status = criteria.getStatus();
    if (status != null && !status.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getStatus()), status, criteria.getStatusOption());
    }
    if (criteria.getPageable() == null) {
      criteria.setPageable(PageRequest.of(0, Integer.MAX_VALUE));
    } else {
      addOrderBy(query, alias, criteria.getPageable().getSort());
    }

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, true);
  }

  /**
   * Add sorting to the given query on the given alias
   *
   * @param query to add sorting to
   * @param alias to retrieve columns from for sorting
   * @param sort specification of sorting
   */
  public default void addOrderBy(JPAQuery<OrderStatusEntity> query, OrderStatusEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "status":
            if (next.isAscending()) {
              query.orderBy($(alias.getStatus()).asc());
            } else {
              query.orderBy($(alias.getStatus()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }

}
