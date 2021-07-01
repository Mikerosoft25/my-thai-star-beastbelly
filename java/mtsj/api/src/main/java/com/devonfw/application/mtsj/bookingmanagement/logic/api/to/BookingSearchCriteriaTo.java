package com.devonfw.application.mtsj.bookingmanagement.logic.api.to;

import java.time.Instant;
import java.util.List;

import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.general.logic.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * used to find {@link com.devonfw.application.mtsj.bookingmanagement.common.api.Booking}s.
 */
public class BookingSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private List<Long> ids;

  private String name;

  private String bookingToken;

  private String comment;

  private Instant bookingDate;

  private Instant expirationDate;

  private Instant creationDate;

  private String email;

  private Boolean canceled;

  private BookingType bookingType;

  private Long tableId;

  private Long orderId;

  private Integer assistants;

  private Long userId;

  private StringSearchConfigTo nameOption;

  private StringSearchConfigTo bookingTokenOption;

  private StringSearchConfigTo commentOption;

  private StringSearchConfigTo emailOption;

  private Boolean showAll;

  /**
   * The constructor.
   */
  public BookingSearchCriteriaTo() {

    super();
  }

  /**
   * @return ids
   */
  public List<Long> getIds() {

    return this.ids;
  }

  /**
   * @param ids new value of {@link #getIds}.
   */
  public void setIds(List<Long> ids) {

    this.ids = ids;
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
   * @return comment
   */
  public String getComment() {

    return this.comment;
  }

  /**
   * @param comment new value of {@link #getComment}.
   */
  public void setComment(String comment) {

    this.comment = comment;
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
   * @return expirationDate
   */
  public Instant getExpirationDate() {

    return this.expirationDate;
  }

  /**
   * @param expirationDate new value of {@link #getExpirationDate}.
   */
  public void setExpirationDate(Instant expirationDate) {

    this.expirationDate = expirationDate;
  }

  /**
   * @return creationDate
   */
  public Instant getCreationDate() {

    return this.creationDate;
  }

  /**
   * @param creationDate new value of {@link #getCreationDate}.
   */
  public void setCreationDate(Instant creationDate) {

    this.creationDate = creationDate;
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
   * @return canceled
   */
  public Boolean getCanceled() {

    return this.canceled;
  }

  /**
   * @param canceled new value of {@link #getCanceled}.
   */
  public void setCanceled(Boolean canceled) {

    this.canceled = canceled;
  }

  /**
   * @return bookingType
   */
  public BookingType getBookingType() {

    return this.bookingType;
  }

  /**
   * @param bookingType new value of {@link #getBookingType}.
   */
  public void setBookingType(BookingType bookingType) {

    this.bookingType = bookingType;
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
   * @return orderId
   */
  public Long getOrderId() {

    return this.orderId;
  }

  /**
   * @param orderId new value of {@link #getOrderId}.
   */
  public void setOrderId(Long orderId) {

    this.orderId = orderId;
  }

  /**
   * @return assistants
   */
  public Integer getAssistants() {

    return this.assistants;
  }

  /**
   * @param assistants new value of {@link #getAssistants}.
   */
  public void setAssistants(Integer assistants) {

    this.assistants = assistants;
  }

  /**
   * @return userId
   */
  public Long getUserId() {

    return this.userId;
  }

  /**
   * @param userId new value of {@link #getUserId}.
   */
  public void setUserId(Long userId) {

    this.userId = userId;
  }

  /**
   * @return nameOption
   */
  public StringSearchConfigTo getNameOption() {

    return this.nameOption;
  }

  /**
   * @param nameOption new value of {@link #getNameOption}.
   */
  public void setNameOption(StringSearchConfigTo nameOption) {

    this.nameOption = nameOption;
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
   * @return commentOption
   */
  public StringSearchConfigTo getCommentOption() {

    return this.commentOption;
  }

  /**
   * @param commentOption new value of {@link #getCommentOption}.
   */
  public void setCommentOption(StringSearchConfigTo commentOption) {

    this.commentOption = commentOption;
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
   * @return showAll
   */
  public Boolean getShowAll() {

    return this.showAll;
  }

  /**
   * @param showAll new value of {@link #getShowAll}.
   */
  public void setShowAll(Boolean showAll) {

    this.showAll = showAll;
  }

}
