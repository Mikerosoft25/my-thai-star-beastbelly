package com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderEntity;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderSearchCriteriaTo;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * {@link DefaultRepository} for {@link OrderEntity}.
 */
public interface OrderRepository extends DefaultRepository<OrderEntity> {

  /**
   * @param idBooking
   * @return the list {@link OrderEntity} objects that matched the search.
   */
  @Query("SELECT orders FROM OrderEntity orders" //
      + " WHERE orders.booking.id = :idBooking")
  List<OrderEntity> findOrders(@Param("idBooking") Long idBooking);

  /**
   * @param idInvitedGuest
   * @return the list {@link OrderEntity} objects that matched the search.
   */
  @Query("SELECT orders FROM OrderEntity orders" //
      + " WHERE orders.invitedGuest.id = :idInvitedGuest")
  List<OrderEntity> findOrdersByInvitedGuest(@Param("idInvitedGuest") Long idInvitedGuest);

  /**
   * @param bookingToken
   * @return the {@link OrderEntity} objects that matched the search.
   */
  @Query("SELECT orders FROM OrderEntity orders" //
      + " WHERE orders.booking.bookingToken = :bookingToken")
  List<OrderEntity> findOrdersByBookingToken(@Param("bookingToken") String bookingToken);

  /**
   * @param criteria the {@link OrderSearchCriteriaTo} with the criteria to search.
   * @return the {@link Page} of the {@link OrderEntity} objects that matched the search.
   */
  default Page<OrderEntity> findOrders(OrderSearchCriteriaTo criteria) {

    OrderEntity alias = newDslAlias();
    JPAQuery<OrderEntity> query = newDslQuery(alias);

    Long booking = criteria.getBookingId();
    if (booking != null && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getId()).eq(booking));
    }
    Long invitedGuest = criteria.getInvitedGuestId();
    if (invitedGuest != null && alias.getInvitedGuest() != null) {
      query.where(Alias.$(alias.getInvitedGuest().getId()).eq(invitedGuest));
    }
    String hostToken = criteria.getHostToken();
    if (hostToken != null && alias.getHost() != null) {
      query.where(Alias.$(alias.getBooking().getBookingToken()).eq(hostToken));
    }
    String email = criteria.getEmail();
    if ((email != null) && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getEmail()).toLowerCase().like(email.toLowerCase()));
    }
    String name = criteria.getName();
    if ((name != null) && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getName()).toLowerCase().like(name.toLowerCase()));
    }
    String bookingToken = criteria.getBookingToken();
    if ((bookingToken != null) && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getBookingToken()).toLowerCase().eq(bookingToken.toLowerCase()));
    }
    Long[] orderStatuses = criteria.getOrderStatusIds();
    if (orderStatuses != null) {
      List<Long> orderStatusIds = new ArrayList<>();
      Collections.addAll(orderStatusIds, orderStatuses);
      query.where(Alias.$(alias.getOrderStatus().getId()).in(orderStatusIds));
    }
    Long orderStatus = criteria.getOrderStatusId();
    if (orderStatus != null && alias.getOrderStatus() != null) {
      query.where(Alias.$(alias.getOrderStatus().getId()).eq(orderStatus));
    }
    Long orderPayStatus = criteria.getOrderPayStatusId();
    if (orderPayStatus != null && alias.getOrderPayStatus() != null) {
      query.where(Alias.$(alias.getOrderPayStatus().getId()).eq(orderPayStatus));
    }
    Long table = criteria.getTableId();
    if (table != null && alias.getBooking().getTable().getId() != null) {
      query.where(Alias.$(alias.getBooking().getTable().getId()).eq(table));
    }
    Instant bookingDate = criteria.getBookingDate();
    if (bookingDate != null && alias.getBooking() != null) {
      Instant startOfDay = bookingDate.truncatedTo(ChronoUnit.DAYS);
      Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);

      query.where(Alias.$(alias.getBooking().getBookingDate()).goe(startOfDay)
          .and(Alias.$(alias.getBooking().getBookingDate()).lt(endOfDay)));

    }
    Boolean inHouse = criteria.getInHouse();
    if (inHouse != null && alias.getBooking().getBookingType() != null) {
      if (inHouse) {
        query.where(Alias.$(alias.getBooking().getBookingType()).ne(BookingType.DELIVERY));
      } else {
        query.where(Alias.$(alias.getBooking().getBookingType()).eq(BookingType.DELIVERY));
      }
    }
    Boolean paid = criteria.getPaid();
    if (paid != null && alias.getOrderPayStatusId() != null) {
      if (paid) {
        query.where(Alias.$(alias.getOrderPayStatus().getId()).eq(1L));
      } else {
        query.where(Alias.$(alias.getOrderPayStatus().getId()).eq(0L));
      }
    }

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, true);
  }

}
