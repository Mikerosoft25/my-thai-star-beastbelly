package com.devonfw.application.mtsj.bookingmanagement.service.api.rest;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.TableEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.TableSearchCriteriaTo;

/**
 * The service interface for REST calls in order to execute the logic of component {@link Bookingmanagement}.
 */
@Path("/bookingmanagement/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BookingmanagementRestService {

  /**
   * Delegates to {@link Bookingmanagement#findBooking}.
   *
   * @param id The id 'id' of the Booking.
   *
   * @return The {@link BookingEto} with id 'id'
   */
  @GET
  @Path("/booking/{id}/")
  public BookingCto getBooking(@PathParam("id") long id);

  /**
   * Delegates to {@link Bookingmanagement#findBookingByGuestToken}.
   *
   * @param guestToken the guestToken.
   *
   * @return The {@link BookingEto} associated to the guestToken.
   */
  @GET
  @Path("/bookingbyguesttoken/{token}/")
  public BookingEto getBookingByGuestToken(@PathParam("token") String guestToken);

  /**
   * Delegates to {@link Bookingmanagement#saveBooking}.
   *
   * @param booking the {@link BookingEto} to be saved.
   *
   * @return the recently created {@link BookingEto}.
   */
  @POST
  @Path("/booking/")
  public BookingEto saveBooking(@Valid BookingCto booking);

  /**
   * Delegates to {@link Bookingmanagement#changeBooking}.
   *
   * @param booking the {@link BookingCto} with the changed details.
   *
   * @return the {@link BookingCto} with the changed details.
   */
  @POST
  @Path("/booking/changebooking")
  public BookingCto changeBooking(BookingCto booking);

  /**
   * Delegates to {@link Bookingmanagement#cancelBooking} with "authorized" set to true.
   *
   * @param id ID of the {@link BookingEto} to be cancel.
   *
   * @return boolean <code>true</code> if the booking can be canceled, <code>false</code> otherwise.
   */
  @GET
  @Path("/booking/cancel/{id}/")
  public boolean cancelBooking(@PathParam("id") long id);

  /**
   * Delegates to {@link Bookingmanagement#cancelBooking} with "authorized" set to false.
   *
   * @param id ID of the {@link BookingEto} to be cancel.
   *
   * @return boolean <code>true</code> if the booking can be canceled, <code>false</code> otherwise.
   */
  @GET
  @Path("/booking/cancelbyuser/{id}/")
  public boolean cancelBookingByUser(@PathParam("id") long id);

  /**
   * Delegates to {@link Bookingmanagement#findBookingsByUser}
   *
   * @param searchCriteriaTo the {@link BookingSearchCriteriaTo}.
   *
   * @return the {@link Page} of matching {@link BookingCto}s.
   */
  @Path("/booking/searchbyuser")
  @POST
  public Page<BookingCto> findBookingsByUser(BookingSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Bookingmanagement#findBookingsByPost}.
   *
   * @param searchCriteriaTo the {@link BookingSearchCriteriaTo}.
   *
   * @return the {@link Page} of matching {@link BookingEto}s.
   */
  @Path("/booking/search")
  @POST
  public Page<BookingCto> findBookingsByPost(BookingSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Bookingmanagement#findInvitedGuest}.
   *
   * @param id The id 'id' of the InvitedGuest.
   *
   * @return The {@link InvitedGuestEto} with id 'id'
   */
  @GET
  @Path("/invitedguest/{id}/")
  public InvitedGuestEto getInvitedGuest(@PathParam("id") long id);

  /**
   * Delegates to {@link Bookingmanagement#saveInvitedGuest}.
   *
   * @param invitedGuest the {@link InvitedGuestEto} to create.
   *
   * @return the new {@link InvitedGuestEto} that has been saved with ID and version.
   */
  @POST
  @Path("/invitedguest/")
  public InvitedGuestEto saveInvitedGuest(InvitedGuestEto invitedGuest);

  /**
   * Delegates to {@link Bookingmanagement#deleteInvitedGuest}.
   *
   * @param id Id of the invitedGuest to delete.
   */
  @DELETE
  @Path("/invitedguest/{id}/")
  public void deleteInvitedGuest(@PathParam("id") long id);

  /**
   * Delegates to {@link Bookingmanagement#findInvitedGuestEtos}.
   *
   * @param searchCriteriaTo the {@link InvitedGuestSearchCriteriaTo}.
   *
   * @return the {@link Page} of matching {@link InvitedGuestEto}s.
   */
  @Path("/invitedguest/search")
  @POST
  public Page<InvitedGuestEto> findInvitedGuestsByPost(InvitedGuestSearchCriteriaTo searchCriteriaTo);

  /**
   * Delegates to {@link Bookingmanagement#acceptInvite}.
   *
   * @param guestToken the guest-token of the invited guest.
   *
   * @return the {@link InvitedGuestEto} of the invited guest.
   */
  @Path("/invitedguest/accept/{token}")
  @GET
  public InvitedGuestEto acceptInvite(@PathParam("token") String guestToken);

  /**
   * Delegates to {@link Bookingmanagement#declineInvite}.
   *
   * @param guestToken the guest-token of the invited guest.
   *
   * @return the {@link InvitedGuestEto} of the invited guest.
   */
  @Path("/invitedguest/decline/{token}")
  @GET
  public InvitedGuestEto declineInvite(@PathParam("token") String guestToken);

  /**
   * Delegates to {@link Bookingmanagement#findTable}.
   *
   * @param id the ID of the {@link TableEto}.
   *
   * @return the {@link TableEto}.
   */
  @GET
  @Path("/table/{id}/")
  public TableEto getTable(@PathParam("id") long id);

  /**
   * Delegates to {@link Bookingmanagement#saveTable}.
   *
   * @param table the {@link TableEto} to be saved.
   *
   * @return the recently created {@link TableEto}.
   */
  @POST
  @Path("/table/")
  public TableEto saveTable(TableEto table);

  /**
   * Delegates to {@link Bookingmanagement#deleteTable}.
   *
   * @param id ID of the {@link TableEto} to be deleted.
   */
  @DELETE
  @Path("/table/{id}/")
  public void deleteTable(@PathParam("id") long id);

  /**
   * Delegates to {@link Bookingmanagement#findTableEtos}.
   *
   * @param searchCriteriaTo the pagination and search criteria to be used for finding tables.
   *
   * @return the {@link Page} of matching {@link TableEto}s.
   */
  @Path("/table/search")
  @POST
  public Page<TableEto> findTablesByPost(TableSearchCriteriaTo searchCriteriaTo);

}
