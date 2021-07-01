package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import java.util.List;

import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.module.basic.common.api.to.AbstractCto;

/**
 * Composite transport object of Order
 */
public class OrderCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private OrderEto order;

  private BookingEto booking;

  private InvitedGuestEto invitedGuest;

  private List<OrderLineCto> orderLines;

  private BookingEto host;

  private OrderStatusEto orderStatus;

  private OrderPayStatusEto orderPayStatus;

  /**
   * @return the {@link OrderEto}.
   */
  public OrderEto getOrder() {

    return this.order;
  }

  /**
   * @param order new {@link OrderEto} to set.
   */
  public void setOrder(OrderEto order) {

    this.order = order;
  }

  /**
   * @return the {@link BookingEto}.
   */
  public BookingEto getBooking() {

    return this.booking;
  }

  /**
   * @param booking new {@link BookingEto} to set.
   */
  public void setBooking(BookingEto booking) {

    this.booking = booking;
  }

  /**
   * @return the {@link InvitedGuestEto}.
   */
  public InvitedGuestEto getInvitedGuest() {

    return this.invitedGuest;
  }

  /**
   * @param invitedGuest new {@link InvitedGuestEto} to set.
   */
  public void setInvitedGuest(InvitedGuestEto invitedGuest) {

    this.invitedGuest = invitedGuest;
  }

  /**
   * @return the {@link List} of {@link OrderLineCto}s.
   */
  public List<OrderLineCto> getOrderLines() {

    return this.orderLines;
  }

  /**
   * @param orderLines the new {@link List} of {@link OrderLineCto}s to set.
   */
  public void setOrderLines(List<OrderLineCto> orderLines) {

    this.orderLines = orderLines;
  }

  /**
   * @return the {@link BookingEto}.
   */
  public BookingEto getHost() {

    return this.host;
  }

  /**
   * @param host new {@link BookingEto} to set.
   */
  public void setHost(BookingEto host) {

    this.host = host;
  }

  /**
   * @return the {@link OrderStatusEto}.
   */
  public OrderStatusEto getOrderStatus() {

    return this.orderStatus;
  }

  /**
   * @param orderStatus new {@link OrderStatusEto} to set.
   */
  public void setOrderStatus(OrderStatusEto orderStatus) {

    this.orderStatus = orderStatus;
  }

  /**
   * @return the {@link OrderPayStatusEto}.
   */
  public OrderPayStatusEto getOrderPayStatus() {

    return this.orderPayStatus;
  }

  /**
   * @param orderPayStatus new {@link OrderPayStatusEto} to set.
   */
  public void setOrderPayStatus(OrderPayStatusEto orderPayStatus) {

    this.orderPayStatus = orderPayStatus;
  }

}
