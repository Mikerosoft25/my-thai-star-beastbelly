package com.devonfw.application.mtsj.bookingmanagement.service.impl.rest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.TableEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.TableSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.service.api.rest.BookingmanagementRestService;
import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Bookingmanagement}.
 */
@Named("BookingmanagementRestService")
@Validated
public class BookingmanagementRestServiceImpl implements BookingmanagementRestService {

  @Inject
  private Bookingmanagement bookingmanagement;

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_BOOKING_BY_ID)
  public BookingCto getBooking(long id) {

    return this.bookingmanagement.findBooking(id);
  }

  @Override
  @PermitAll
  public BookingEto getBookingByGuestToken(String guestToken) {

    return this.bookingmanagement.findBookingByGuestToken(guestToken);
  }

  @Override
  @PermitAll
  public BookingEto saveBooking(@Valid BookingCto booking) {

    return this.bookingmanagement.saveBooking(booking);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CHANGE_BOOKING)
  public BookingCto changeBooking(BookingCto booking) {

    return this.bookingmanagement.changeBooking(booking);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CANCEL_BOOKING)
  public boolean cancelBooking(long id) {

    return this.bookingmanagement.cancelBooking(id, true);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_CANCEL_BOOKING_BY_USER)
  public boolean cancelBookingByUser(long id) {

    return this.bookingmanagement.cancelBooking(id, false);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_BOOKINGS_BY_USER)
  public Page<BookingCto> findBookingsByUser(BookingSearchCriteriaTo searchCriteriaTo) {

    return this.bookingmanagement.findBookingsByUser(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_BOOKINGS)
  public Page<BookingCto> findBookingsByPost(BookingSearchCriteriaTo searchCriteriaTo) {

    return this.bookingmanagement.findBookingsByPost(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_INVITEDGUESTS)
  public InvitedGuestEto getInvitedGuest(long id) {

    return this.bookingmanagement.findInvitedGuest(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_INVITEDGUESTS)
  public InvitedGuestEto saveInvitedGuest(InvitedGuestEto invitedguest) {

    return this.bookingmanagement.saveInvitedGuest(invitedguest);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_INVITEDGUESTS)
  public void deleteInvitedGuest(long id) {

    this.bookingmanagement.deleteInvitedGuest(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_INVITEDGUESTS)
  public Page<InvitedGuestEto> findInvitedGuestsByPost(InvitedGuestSearchCriteriaTo searchCriteriaTo) {

    return this.bookingmanagement.findInvitedGuestEtos(searchCriteriaTo);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_TABLE)
  public TableEto getTable(long id) {

    return this.bookingmanagement.findTable(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_TABLE)
  public TableEto saveTable(TableEto table) {

    return this.bookingmanagement.saveTable(table);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_TABLE)
  public void deleteTable(long id) {

    this.bookingmanagement.deleteTable(id);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SEARCH_TABLES)
  public Page<TableEto> findTablesByPost(TableSearchCriteriaTo searchCriteriaTo) {

    return this.bookingmanagement.findTableEtos(searchCriteriaTo);
  }

  @Override
  @PermitAll
  public InvitedGuestEto acceptInvite(String guestToken) {

    return this.bookingmanagement.acceptInvite(guestToken);
  }

  @Override
  @PermitAll
  public InvitedGuestEto declineInvite(String guestToken) {

    return this.bookingmanagement.declineInvite(guestToken);
  }
}
