package com.devonfw.application.mtsj.bookingmanagement.common.api.datatype;

/**
 * @author rudiazma
 *
 */
public enum BookingType {

  /**
   * if the booking is a normal booking without invited guests.
   */
  COMMON,
  /**
   * if the booking is a normal booking with invited guests.
   */
  INVITED,
  /**
   * if the booking is an alexa delivery booking.
   */
  DELIVERY;

  /**
   * @return true if the booking is COMMON.
   */
  public boolean isCommon() {

    return (this == COMMON);
  }

  /**
   * @return true if the booking is INVITED.
   */
  public boolean isInvited() {

    return (this == INVITED);
  }

  /**
   * @return true if the booking is DELIVERY.
   */
  public boolean isDelivery() {

    return (this == DELIVERY);
  }

}
