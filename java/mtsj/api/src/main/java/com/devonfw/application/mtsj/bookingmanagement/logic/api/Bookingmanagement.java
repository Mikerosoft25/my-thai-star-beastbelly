package com.devonfw.application.mtsj.bookingmanagement.logic.api;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.TableEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.TableSearchCriteriaTo;

/**
 * Interface for Bookingmanagement component.
 */
public interface Bookingmanagement {

  /**
   * Returns a Booking by its id 'id'.
   *
   * @param id The id 'id' of the Booking.
   * @return The {@link BookingEto} with id 'id'
   */
  BookingCto findBooking(Long id);

  /**
   * Returns a Booking that is associated to the given guestToken.
   *
   * @param guestToken the guestToken.
   * @return The {@link BookingEto} associated to the guestToken.
   */
  BookingEto findBookingByGuestToken(String guestToken);

  /**
   * Returns the next booking associated with the given email. If no booking in the future is found, a booking in the
   * last hour with the given email will be searched.
   *
   * @param timestamp time of the booking.
   * @param email email of the host.
   *
   * @return The next or current {@link BookingCto} associated with the email.
   */
  BookingCto findAssociatedBooking(Instant timestamp, String email);

  /**
   * Returns Booking by token.
   *
   * @param token the booking-token.
   * @return The {@link BookingCto} with the given booking-token.
   */
  BookingCto findBookingByToken(String token);

  /**
   * Returns InvitedGuest by guest-token.
   *
   * @param token the guest-token.
   * @return The {@link InvitedGuestEto} with the given guest-token.
   */
  InvitedGuestEto findInvitedGuestByToken(String token);

  /**
   * Returns a list with all invited guests of a booking by booking id.
   *
   * @param bookingId the id of the booking.
   * @return {@link List} of {@link InvitedGuestEto}s of the booking with the given id.
   */
  List<InvitedGuestEto> findInvitedGuestByBooking(Long bookingId);

  /**
   * Returns all bookings of the currently logged in user by using the bearer-authorization-token in the request header.
   *
   * @param criteria the {@link BookingSearchCriteriaTo}.
   * @return the {@link Page} of matching {@link BookingEto}s.
   */
  Page<BookingCto> findBookingsByUser(BookingSearchCriteriaTo criteria);

  /**
   * Returns a paginated list of Bookings matching the search criteria. Needs Authorization.
   *
   * @param criteria the {@link BookingSearchCriteriaTo}.
   * @return the {@link Page} of matching {@link BookingEto}s.
   */
  Page<BookingCto> findBookingsByPost(BookingSearchCriteriaTo criteria);

  /**
   * Returns a paginated list of Bookings matching the search criteria.
   *
   * @param criteria the {@link BookingSearchCriteriaTo}.
   * @return the {@link List} of matching {@link BookingEto}s.
   */
  Page<BookingCto> findBookingCtos(BookingSearchCriteriaTo criteria);

  /**
   * Cancels a booking and deletes it from the database by its id 'bookingId'.
   *
   * @param bookingId Id of the booking to cancel.
   * @param authorized Whether the cancellation is done by a waiter or a higher role or not.
   *
   * @return boolean <code>true</code> if the booking can be deleted, <code>false</code> otherwise.
   */
  boolean cancelBooking(Long bookingId, boolean authorized);

  /**
   * Saves a booking and store it in the database.
   *
   * @param booking the {@link BookingEto} to create.
   * @return the new {@link BookingEto} that has been saved with ID and version.
   */
  BookingEto saveBooking(BookingCto booking);

  /**
   * Changes the details of a booking and saves the changes in the database.
   *
   * @param booking the {@link BookingCto} with the changed details.
   * @return the {@link BookingCto} with the changed details.
   */
  BookingCto changeBooking(BookingCto booking);

  /**
   * Returns a InvitedGuest by its id 'id'.
   *
   * @param id The id 'id' of the InvitedGuest.
   * @return The {@link InvitedGuestEto} with id 'id'
   */
  InvitedGuestEto findInvitedGuest(Long id);

  /**
   * Returns a paginated list of InvitedGuests matching the search criteria.
   *
   * @param criteria the {@link InvitedGuestSearchCriteriaTo}.
   * @return the {@link Page} of matching {@link InvitedGuestEto}s.
   */
  Page<InvitedGuestEto> findInvitedGuestEtos(InvitedGuestSearchCriteriaTo criteria);

  /**
   * Deletes a invitedGuest from the database by its id 'invitedGuestId'.
   *
   * @param invitedGuestId Id of the invitedGuest to delete.
   * @return boolean <code>true</code> if the invitedGuest can be deleted, <code>false</code> otherwise.
   */
  boolean deleteInvitedGuest(Long invitedGuestId);

  /**
   * Saves a invitedGuest and store it in the database.
   *
   * @param invitedGuest the {@link InvitedGuestEto} to create.
   * @return the new {@link InvitedGuestEto} that has been saved with ID and version.
   */
  InvitedGuestEto saveInvitedGuest(InvitedGuestEto invitedGuest);

  /**
   * Returns a Table by its id 'id'.
   *
   * @param id The id 'id' of the Table.
   * @return The {@link TableEto} with id 'id'
   */
  TableEto findTable(Long id);

  /**
   * Returns a paginated list of Tables matching the search criteria.
   *
   * @param criteria the {@link TableSearchCriteriaTo}.
   * @return the {@link List} of matching {@link TableEto}s.
   */
  Page<TableEto> findTableEtos(TableSearchCriteriaTo criteria);

  /**
   * Deletes a table from the database by its id 'tableId'.
   *
   * @param tableId Id of the table to delete.
   * @return boolean <code>true</code> if the table can be deleted, <code>false</code> otherwise.
   */
  boolean deleteTable(Long tableId);

  /**
   * Saves a table and store it in the database.
   *
   * @param table the {@link TableEto} to create.
   * @return the new {@link TableEto} that has been saved with ID and version.
   */
  TableEto saveTable(TableEto table);

  /**
   * Accepts an invite to a booking.
   *
   * @param guestToken the guest-token of the invited guest.
   *
   * @return the {@link InvitedGuestEto} of the invited guest.
   */
  InvitedGuestEto acceptInvite(String guestToken);

  /**
   * Declines an invite to a booking.
   *
   * @param guestToken the guest-token of the invited guest.
   *
   * @return the {@link InvitedGuestEto} of the invited guest.
   */
  InvitedGuestEto declineInvite(String guestToken);

}
