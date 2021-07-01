package com.devonfw.application.mtsj.ordermanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

/**
 * Interface for Orders
 */
public interface Order extends ApplicationEntity {

  /**
   * @return the bookingId
   */
  public Long getBookingId();

  /**
   * Set the bookingId
   *
   * @param bookingId the bookingId
   */
  public void setBookingId(Long bookingId);

  /**
   * @return the invitedGuestId
   */
  public Long getInvitedGuestId();

  /**
   * Set the invitedGuestId
   *
   * @param invitedGuestId the invitedGuestId
   */
  public void setInvitedGuestId(Long invitedGuestId);

  /**
   * @return the hostId
   */
  public Long getHostId();

  /**
   * Set the hostId
   *
   * @param hostId the hostId
   */
  public void setHostId(Long hostId);

  /**
   * @return the orderStatusId
   */
  public Long getOrderStatusId();

  /**
   * Set the orderStatusId
   *
   * @param orderStatusId the orderStatusId
   */
  public void setOrderStatusId(Long orderStatusId);

  /**
   * @return the orderPayStatusId
   */
  public Long getOrderPayStatusId();

  /**
   * Set the orderPayStatusId.
   *
   * @param orderPayStatusId the orderPayStatusId.
   */
  public void setOrderPayStatusId(Long orderPayStatusId);

}
