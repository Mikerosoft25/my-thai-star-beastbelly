package com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo;

import static com.querydsl.core.alias.Alias.$;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devonfw.application.mtsj.usermanagement.dataaccess.api.PasswordResetTokenEntity;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.PasswordResetTokenSearchCriteriaTo;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * {@link DefaultRepository} for {@link PasswordResetTokenEntity}
 */
public interface PasswordResetTokenRepository extends DefaultRepository<PasswordResetTokenEntity> {

  /**
   * @param token the password-reset-token.
   *
   * @return the {@link PasswordResetTokenEntity} matching the password-reset-token.
   */
  @Query("SELECT passwordresettoken FROM PasswordResetTokenEntity passwordresettoken" //
      + " WHERE passwordresettoken.token = :token")
  PasswordResetTokenEntity findByToken(@Param("token") String token);

  /**
   * @param idUser the id of the user.
   *
   * @return {@link List} of {@link PasswordResetTokenEntity}s that are associated to the give id of the user.
   */
  @Query("SELECT passwordresettoken FROM PasswordResetTokenEntity passwordresettoken" //
      + " WHERE passwordresettoken.user.id = :idUser")
  List<PasswordResetTokenEntity> findByUser(@Param("idUser") long idUser);

  /**
   * @param criteria the {@link PasswordResetTokenSearchCriteriaTo} with the criteria to search.
   *
   * @return the {@link Page} of the {@link PasswordResetTokenEntity} objects that matched the search. If no pageable is
   *         set, it will return a unique page with all the objects that matched the search.
   */
  default Page<PasswordResetTokenEntity> findByCriteria(PasswordResetTokenSearchCriteriaTo criteria) {

    PasswordResetTokenEntity alias = newDslAlias();
    JPAQuery<PasswordResetTokenEntity> query = newDslQuery(alias);

    String token = criteria.getToken();
    if (token != null && !token.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getToken()), token, criteria.getTokenOption());
    }
    Long user = criteria.getUserId();
    if (user != null) {
      query.where($(alias.getUser().getId()).eq(user));
    }
    Instant expiryDate = criteria.getExpiryDate();
    if (expiryDate != null) {
      query.where($(alias.getExpiryDate()).eq(expiryDate));
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
  public default void addOrderBy(JPAQuery<PasswordResetTokenEntity> query, PasswordResetTokenEntity alias, Sort sort) {

    if (sort != null && sort.isSorted()) {
      Iterator<Order> it = sort.iterator();
      while (it.hasNext()) {
        Order next = it.next();
        switch (next.getProperty()) {
          case "token":
            if (next.isAscending()) {
              query.orderBy($(alias.getToken()).asc());
            } else {
              query.orderBy($(alias.getToken()).desc());
            }
            break;
          case "user":
            if (next.isAscending()) {
              query.orderBy($(alias.getUser().getId().toString()).asc());
            } else {
              query.orderBy($(alias.getUser().getId().toString()).desc());
            }
            break;
          case "expiryDate":
            if (next.isAscending()) {
              query.orderBy($(alias.getExpiryDate()).asc());
            } else {
              query.orderBy($(alias.getExpiryDate()).desc());
            }
            break;
          default:
            throw new IllegalArgumentException("Sorted by the unknown property '" + next.getProperty() + "'");
        }
      }
    }
  }

}