package com.devonfw.application.mtsj.usermanagement.logic.api.to;

import java.util.List;

import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishEto;
import com.devonfw.module.basic.common.api.to.AbstractCto;

/**
 * Composite transport object of User
 */
public class UserCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private UserEto user;

  private UserRoleEto userRole;

  private List<BookingEto> bookings;

  private List<DishEto> favourites;

  /**
   * @return the {@link UserEto}.
   */
  public UserEto getUser() {

    return this.user;
  }

  /**
   * @param user the new {@link UserEto} to set.
   */
  public void setUser(UserEto user) {

    this.user = user;
  }

  /**
   * @return the {@link UserRoleEto}.
   */
  public UserRoleEto getUserRole() {

    return this.userRole;
  }

  /**
   * @param userRole the new {@link UserRoleEto} to set.
   */
  public void setUserRole(UserRoleEto userRole) {

    this.userRole = userRole;
  }

  /**
   * @return the {@link List} of {@link BookingEto} containig the bookings.
   */
  public List<BookingEto> getBookings() {

    return this.bookings;
  }

  /**
   * @param bookings the {@link List} of {@link BookingEto} containig the new bookings to set.
   */
  public void setBookings(List<BookingEto> bookings) {

    this.bookings = bookings;
  }

  /**
   * @return the {@link List} of {@link DishEto} containig the favourites.
   */
  public List<DishEto> getFavourites() {

    return this.favourites;
  }

  /**
   * @param favourites the {@link List} of {@link DishEto} containig the new favourites to set.
   */
  public void setFavourites(List<DishEto> favourites) {

    this.favourites = favourites;
  }

}
