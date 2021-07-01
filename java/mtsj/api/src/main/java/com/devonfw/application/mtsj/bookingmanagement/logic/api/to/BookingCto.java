package com.devonfw.application.mtsj.bookingmanagement.logic.api.to;

import java.util.List;

import javax.validation.Valid;

import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;
import com.devonfw.module.basic.common.api.to.AbstractCto;

/**
 * Composite transport object of Booking
 */
public class BookingCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  @Valid
  private BookingEto booking;

  private TableEto table;

  private List<InvitedGuestEto> invitedGuests;

  private OrderEto order;

  private List<OrderEto> orders;

  private UserEto user;

  /**
   * @return the {@link BookingEto}.
   */
  public BookingEto getBooking() {

    return this.booking;
  }

  /**
   * @param booking the {@link BookingEto} to set.
   */
  public void setBooking(BookingEto booking) {

    this.booking = booking;
  }

  /**
   * @return the {@link BookingEto}.
   */
  public TableEto getTable() {

    return this.table;
  }

  /**
   * @param table the {@link TableEto} to set.
   */
  public void setTable(TableEto table) {

    this.table = table;
  }

  /**
   * @return the {@link List} of {@link InvitedGuestEto}s.
   */
  public List<InvitedGuestEto> getInvitedGuests() {

    return this.invitedGuests;
  }

  /**
   * @param invitedGuests the {@link List} of {@link InvitedGuestEto}s to set.
   */
  public void setInvitedGuests(List<InvitedGuestEto> invitedGuests) {

    this.invitedGuests = invitedGuests;
  }

  /**
   * @return the {@link OrderEto}.
   */
  public OrderEto getOrder() {

    return this.order;
  }

  /**
   * @param order the {@link OrderEto} to set.
   */
  public void setOrder(OrderEto order) {

    this.order = order;
  }

  /**
   * @return the {@link List} of {@link OrderEto}s.
   */
  public List<OrderEto> getOrders() {

    return this.orders;
  }

  /**
   * @param orders the {@link List} of {@link OrderEto}s to set.
   */
  public void setOrders(List<OrderEto> orders) {

    this.orders = orders;
  }

  /**
   * @return the {@link UserEto}.
   */
  public UserEto getUser() {

    return this.user;
  }

  /**
   * @param user the {@link UserEto} to set.
   */
  public void setUser(UserEto user) {

    this.user = user;
  }

}
