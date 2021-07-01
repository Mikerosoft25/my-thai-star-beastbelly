package com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo;

import static com.querydsl.core.alias.Alias.$;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * {@link DefaultRepository} for {@link BookingEntity}.
 */
public interface BookingRepository extends DefaultRepository<BookingEntity> {

  /**
   * @param token the booking-token.
   *
   * @return the {@link BookingEntity} objects that matched the search.
   */
  @Query("SELECT booking FROM BookingEntity booking" //
      + " WHERE booking.bookingToken = :token")
  BookingEntity findBookingByToken(@Param("token") String token);

  /**
   * Returns bookings after the given time and orders them descending by bookingDate.
   *
   * @param timestamp the time.
   *
   * @return the {@link List} of the {@link BookingEntity} objects that matched the search.
   */
  @Query("SELECT booking FROM BookingEntity booking" //
      + " WHERE booking.bookingDate > :timestamp ORDER BY booking.bookingDate DESC")
  List<BookingEntity> findClosestBookings(@Param("timestamp") Instant timestamp);

  /**
   * Returns bookings within the last hour of the given time.
   *
   * @param timestamp the time.
   *
   * @return the {@link List} of the {@link BookingEntity} objects that matched the search.
   */
  @Query("SELECT booking FROM BookingEntity booking" //
      + " WHERE booking.bookingDate < :timestamp AND DATEADD('HOUR', 1, booking.bookingDate) > :timestamp ORDER BY booking.bookingDate ASC")
  List<BookingEntity> findCurrentBookings(@Param("timestamp") Instant timestamp);

  /**
   * @param criteria the {@link BookingSearchCriteriaTo} with the criteria to search.
   *
   * @return the {@link Page} of the {@link BookingEntity} objects that matched the search.
   */
  default Page<BookingEntity> findBookings(BookingSearchCriteriaTo criteria) {

    BookingEntity alias = newDslAlias();
    JPAQuery<BookingEntity> query = newDslQuery(alias);

    List<Long> ids = criteria.getIds();
    if (ids != null) {
      query.where(Alias.$(alias.getId()).in(ids));
    }
    String name = criteria.getName();
    if ((name != null) && !name.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getName()), name, criteria.getNameOption());
    }
    String bookingToken = criteria.getBookingToken();
    if (bookingToken != null && !bookingToken.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getBookingToken()), bookingToken, criteria.getBookingTokenOption());
    }
    String comment = criteria.getComment();
    if (comment != null && !comment.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getComment()), comment, criteria.getCommentOption());
    }
    Instant bookingDate = criteria.getBookingDate();
    if (bookingDate != null) {
      Instant startOfDay = bookingDate.truncatedTo(ChronoUnit.DAYS);
      Instant endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);

      query.where(Alias.$(alias.getBookingDate()).goe(startOfDay).and(Alias.$(alias.getBookingDate()).lt(endOfDay)));

    }
    Instant expirationDate = criteria.getExpirationDate();
    if (expirationDate != null) {
      query.where(Alias.$(alias.getExpirationDate()).eq(expirationDate));
    }
    Instant creationDate = criteria.getCreationDate();
    if (creationDate != null) {
      query.where(Alias.$(alias.getCreationDate()).eq(creationDate));
    }
    String email = criteria.getEmail();
    if (email != null && !email.isEmpty()) {
      QueryUtil.get().whereString(query, $(alias.getEmail()), email, criteria.getEmailOption());

    }
    Boolean canceled = criteria.getCanceled();
    if (canceled != null) {
      query.where(Alias.$(alias.getCanceled()).eq(canceled));
    }
    BookingType bookingType = criteria.getBookingType();
    if (bookingType != null) {
      query.where(Alias.$(alias.getBookingType()).eq(bookingType));
    }
    Long table = criteria.getTableId();
    if (table != null && alias.getTable() != null) {
      query.where(Alias.$(alias.getTable().getId()).eq(table));
    }
    Boolean showAll = criteria.getShowAll();
    if (showAll == null || showAll == false) {
      query.where(Alias.$(alias.getBookingDate()).gt(Instant.now().minus(2, ChronoUnit.HOURS)));
    }
    return QueryUtil.get().findPaginated(criteria.getPageable(), query, true);
  }
}
