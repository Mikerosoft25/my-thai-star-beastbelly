package com.devonfw.application.mtsj.bookingmanagement.dataaccess.api;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.devonfw.application.mtsj.bookingmanagement.common.api.InvitedGuest;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderEntity;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link InvitedGuest}.
 */
@Entity
@Table(name = "InvitedGuest")
public class InvitedGuestEntity extends ApplicationPersistenceEntity implements InvitedGuest {

  private BookingEntity booking;

  private String guestToken;

  private String email;

  private Boolean accepted;

  private Instant modificationDate;

  private OrderEntity order;

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   */
  public InvitedGuestEntity() {

    super();
  }

  /**
   * @return booking
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idBooking")
  public BookingEntity getBooking() {

    return this.booking;
  }

  /**
   * @param booking new value of {@link #getBooking}.
   */
  public void setBooking(BookingEntity booking) {

    this.booking = booking;
  }

  /**
   * @return guestToken
   */
  @Override
  public String getGuestToken() {

    return this.guestToken;
  }

  /**
   * @param guestToken new value of {@link #getGuestToken}.
   */
  @Override
  public void setGuestToken(String guestToken) {

    this.guestToken = guestToken;
  }

  /**
   * @return email
   */
  @Override
  public String getEmail() {

    return this.email;
  }

  /**
   * @param email new value of {@link #getEmail}.
   */
  @Override
  public void setEmail(String email) {

    this.email = email;
  }

  /**
   * @return accepted
   */
  @Override
  public Boolean getAccepted() {

    return this.accepted;
  }

  /**
   * @param accepted new value of {@link #getAccepted}.
   */
  @Override
  public void setAccepted(Boolean accepted) {

    this.accepted = accepted;
  }

  /**
   * @return modificationDate
   */
  @Override
  public Instant getModificationDate() {

    return this.modificationDate;
  }

  /**
   * @param modificationDate new value of {@link #getModificationDate}.
   */
  @Override
  public void setModificationDate(Instant modificationDate) {

    this.modificationDate = modificationDate;
  }

  @Override
  @Transient
  public Long getBookingId() {

    if (this.booking == null) {
      return null;
    }
    return this.booking.getId();
  }

  @Override
  public void setBookingId(Long bookingId) {

    if (bookingId == null) {
      this.booking = null;
    } else {
      BookingEntity bookingEntity = new BookingEntity();
      bookingEntity.setId(bookingId);
      this.booking = bookingEntity;
    }
  }

  /**
   * @return order
   */
  @OneToOne
  @JoinColumn(name = "idOrder")
  public OrderEntity getOrder() {

    return this.order;
  }

  /**
   * @param order new value of {@link #getOrder}.
   */
  public void setOrder(OrderEntity order) {

    this.order = order;
  }

}
