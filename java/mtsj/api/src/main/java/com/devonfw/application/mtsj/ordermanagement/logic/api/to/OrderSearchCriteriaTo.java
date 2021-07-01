package com.devonfw.application.mtsj.ordermanagement.logic.api.to;

import java.time.Instant;

import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * used to find {@link com.devonfw.application.mtsj.ordermanagement.common.api.Order}s.
 */
public class OrderSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Long bookingId;

  private Long invitedGuestId;

  private String hostToken;

  private Long hostId;

  private String email;

  private String bookingToken;

  private StringSearchConfigTo hostTokenOption;

  private StringSearchConfigTo emailOption;

  private StringSearchConfigTo bookingTokenOption;

  private Long orderStatusId;

  private Long[] orderStatusIds;

  private Long orderPayStatusId;

  private Long tableId;

  private Boolean inHouse;

  private Instant bookingDate;

  private String name;

  private Boolean paid;

  /**
   * The constructor.
   */
  public OrderSearchCriteriaTo() {

    super();
  }

  /**
   * @return bookingId
   */
  public Long getBookingId() {

    return this.bookingId;
  }

  /**
   * @param bookingId new value of {@link #getBookingId}.
   */
  public void setBookingId(Long bookingId) {

    this.bookingId = bookingId;
  }

  /**
   * @return invitedGuestId
   */
  public Long getInvitedGuestId() {

    return this.invitedGuestId;
  }

  /**
   * @param invitedGuestId new value of {@link #getInvitedGuestId}.
   */
  public void setInvitedGuestId(Long invitedGuestId) {

    this.invitedGuestId = invitedGuestId;
  }

  /**
   * @return hostToken
   */
  public String getHostToken() {

    return this.hostToken;
  }

  /**
   * @param hostToken new value of {@link #getHostToken}.
   */
  public void setHostToken(String hostToken) {

    this.hostToken = hostToken;
  }

  /**
   * @return hostId
   */
  public Long getHostId() {

    return this.hostId;
  }

  /**
   * @param hostId new value of {@link #getHostId}.
   */
  public void setHostId(Long hostId) {

    this.hostId = hostId;
  }

  /**
   * @return email
   */
  public String getEmail() {

    return this.email;
  }

  /**
   * @param email new value of {@link #getEmail}.
   */
  public void setEmail(String email) {

    this.email = email;
  }

  /**
   * @return bookingToken
   */
  public String getBookingToken() {

    return this.bookingToken;
  }

  /**
   * @param bookingToken new value of {@link #getBookingToken}.
   */
  public void setBookingToken(String bookingToken) {

    this.bookingToken = bookingToken;
  }

  /**
   * @return hostTokenOption
   */
  public StringSearchConfigTo getHostTokenOption() {

    return this.hostTokenOption;
  }

  /**
   * @param hostTokenOption new value of {@link #getHostTokenOption}.
   */
  public void setHostTokenOption(StringSearchConfigTo hostTokenOption) {

    this.hostTokenOption = hostTokenOption;
  }

  /**
   * @return emailOption
   */
  public StringSearchConfigTo getEmailOption() {

    return this.emailOption;
  }

  /**
   * @param emailOption new value of {@link #getEmailOption}.
   */
  public void setEmailOption(StringSearchConfigTo emailOption) {

    this.emailOption = emailOption;
  }

  /**
   * @return bookingTokenOption
   */
  public StringSearchConfigTo getBookingTokenOption() {

    return this.bookingTokenOption;
  }

  /**
   * @param bookingTokenOption new value of {@link #getBookingTokenOption}.
   */
  public void setBookingTokenOption(StringSearchConfigTo bookingTokenOption) {

    this.bookingTokenOption = bookingTokenOption;
  }

  /**
   * @return orderStatusId
   */
  public Long getOrderStatusId() {

    return this.orderStatusId;
  }

  /**
   * @param orderStatusId new value of {@link #getOrderStatusId}.
   */
  public void setOrderStatusId(Long orderStatusId) {

    this.orderStatusId = orderStatusId;
  }

  /**
   * @return orderPayStatusId
   */
  public Long getOrderPayStatusId() {

    return this.orderPayStatusId;
  }

  /**
   * @param orderPayStatusId new value of {@link #getOrderPayStatusId}.
   */
  public void setOrderPayStatusId(Long orderPayStatusId) {

    this.orderPayStatusId = orderPayStatusId;
  }

  /**
   * @return orderStatusIds
   */
  public Long[] getOrderStatusIds() {

    return this.orderStatusIds;
  }

  /**
   * @param orderStatusIds new value of {@link #getOrderStatusIds}.
   */
  public void setOrderStatusIds(Long[] orderStatusIds) {

    this.orderStatusIds = orderStatusIds;
  }

  /**
   * @return tableId
   */
  public Long getTableId() {

    return this.tableId;
  }

  /**
   * @param tableId new value of {@link #getTableId}.
   */
  public void setTableId(Long tableId) {

    this.tableId = tableId;
  }

  /**
   * @return inHouse
   */
  public Boolean getInHouse() {

    return this.inHouse;
  }

  /**
   * @param inHouse new value of {@link #getInHouse}.
   */
  public void setInHouse(Boolean inHouse) {

    this.inHouse = inHouse;
  }

  /**
   * @return bookingDate
   */
  public Instant getBookingDate() {

    return this.bookingDate;
  }

  /**
   * @param bookingDate new value of {@link #getBookingDate}.
   */
  public void setBookingDate(Instant bookingDate) {

    this.bookingDate = bookingDate;
  }

  /**
   * @return name
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getName}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return paid
   */
  public Boolean getPaid() {

    return this.paid;
  }

  /**
   * @param paid new value of {@link #getPaid}.
   */
  public void setPaid(Boolean paid) {

    this.paid = paid;
  }

}
