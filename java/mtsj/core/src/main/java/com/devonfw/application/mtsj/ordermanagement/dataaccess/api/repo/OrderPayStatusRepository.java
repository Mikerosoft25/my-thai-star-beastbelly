package com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo;

import static com.querydsl.core.alias.Alias.$;

import java.util.Iterator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderPayStatusEntity;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusSearchCriteriaTo;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * {@link DefaultRepository} for {@link OrderPayStatusEntity}
 */
public interface OrderPayStatusRepository extends DefaultRepository<OrderPayStatusEntity> {

  /**
   * @param criteria the {@link OrderPayStatusSearchCriteriaTo} with the criteria to search.
   * @return the {@link Page} of the {@link OrderPayStatusEntity} objects that matched the search. If no pageable is
   *         set, it will return a unique page with all the objects that matched the search.
   */
  default Page<OrderPayStatusEntity> findByCriteria(OrderPayStatusSearchCriteriaTo criteria) {

    OrderPayStatusEntity alias = newDslAlias();
    JPAQuery<OrderPayStatusEntity> query = newDslQuery(alias);

    String payStatus = criteria.getPayStatus();
    if (payStatus != null && !payStatus.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getPayStatus()), payStatus, criteria.getPayStatusOption());
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
  public default void addOrderBy(JPAQuery<OrderPayStatusEntity> query, OrderPayStatusEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "payStatus":
            if (next.isAscending()) {
              query.orderBy($(alias.getPayStatus()).asc());
            } else {
              query.orderBy($(alias.getPayStatus()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }

}