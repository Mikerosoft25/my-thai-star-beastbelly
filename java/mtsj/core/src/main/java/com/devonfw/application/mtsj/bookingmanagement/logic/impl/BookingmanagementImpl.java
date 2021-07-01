package com.devonfw.application.mtsj.bookingmanagement.logic.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.common.api.exception.InvalidBookingDateException;
import com.devonfw.application.mtsj.bookingmanagement.common.api.exception.InvalidBookingIdException;
import com.devonfw.application.mtsj.bookingmanagement.common.api.exception.InvalidTableIdException;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.InvitedGuestEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.TableEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.InvitedGuestRepository;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.TableRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestSearchCriteriaTo;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.TableEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.TableSearchCriteriaTo;
import com.devonfw.application.mtsj.general.logic.base.AbstractComponentFacade;
import com.devonfw.application.mtsj.mailservice.logic.api.Mail;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoBookingException;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.logic.api.to.UserEto;

/**
 * Implementation of component interface of bookingmanagement
 */

@Named
@Transactional
public class BookingmanagementImpl extends AbstractComponentFacade implements Bookingmanagement {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(BookingmanagementImpl.class);

  @Value("${client.port}")
  private int clientPort;

  @Value("${server.servlet.context-path}")
  private String serverContextPath;

  @Value("${mythaistar.hourslimitcancellation}")
  private int hoursLimit;

  /**
   * @see #getBookingDao()
   */
  @Inject
  private BookingRepository bookingDao;

  /**
   * @see #getInvitedGuestDao()
   */
  @Inject
  private InvitedGuestRepository invitedGuestDao;

  /**
   * @see #getTableDao()
   */
  @Inject
  private TableRepository tableDao;

  @Inject
  private Ordermanagement orderManagement;

  @Inject
  private Usermanagement userManagement;

  @Inject
  private Mail mailService;

  /**
   * The constructor.
   */
  public BookingmanagementImpl() {

    super();
  }

  @Override
  public BookingCto findBooking(Long id) {

    LOG.debug("Get Booking with id {} from database.", id);
    BookingEntity entity = getBookingDao().find(id);
    BookingCto cto = new BookingCto();
    cto.setBooking(getBeanMapper().map(entity, BookingEto.class));
    cto.setTable(getBeanMapper().map(entity.getTable(), TableEto.class));
    cto.setOrder(getBeanMapper().map(entity.getOrder(), OrderEto.class));
    cto.setInvitedGuests(getBeanMapper().mapList(entity.getInvitedGuests(), InvitedGuestEto.class));
    cto.setOrders(getBeanMapper().mapList(entity.getOrders(), OrderEto.class));
    return cto;
  }

  @Override
  public BookingEto findBookingByGuestToken(String guestToken) {

    InvitedGuestEntity invitedGuest = getInvitedGuestDao().findInvitedGuestByToken(guestToken);

    return getBeanMapper().map(invitedGuest.getBooking(), BookingEto.class);
  }

  @Override
  public BookingCto findAssociatedBooking(Instant timestamp, String email) {

    List<BookingEntity> nextBookingEntities = getBookingDao().findClosestBookings(timestamp);

    BookingEntity nextBooking = null;

    // checking for the next bookings that are coming
    for (BookingEntity bookingEntity : nextBookingEntities) {
      if (bookingEntity.getEmail().equals(email)
          && this.orderManagement.findOrdersByBookingToken(bookingEntity.getBookingToken()).size() == 0) {
        nextBooking = bookingEntity;
      }
    }

    // checking for bookings that are currently
    if (nextBooking == null) {
      List<BookingEntity> currentBookingEntites = getBookingDao().findCurrentBookings(timestamp);

      for (BookingEntity bookingEntity : currentBookingEntites) {
        if (bookingEntity.getEmail().equals(email)
            && this.orderManagement.findOrdersByBookingToken(bookingEntity.getBookingToken()).size() == 0) {
          nextBooking = bookingEntity;
        }
      }
    }

    if (nextBooking == null) {
      throw new NoBookingException();
    }

    BookingCto cto = new BookingCto();
    cto.setBooking(getBeanMapper().map(nextBooking, BookingEto.class));
    cto.setTable(getBeanMapper().map(nextBooking.getTable(), TableEto.class));
    cto.setOrder(getBeanMapper().map(nextBooking.getOrder(), OrderEto.class));
    cto.setInvitedGuests(getBeanMapper().mapList(nextBooking.getInvitedGuests(), InvitedGuestEto.class));
    cto.setOrders(getBeanMapper().mapList(nextBooking.getOrders(), OrderEto.class));
    return cto;
  }

  @Override
  public BookingCto findBookingByToken(String token) {

    BookingEntity entity = getBookingDao().findBookingByToken(token);
    BookingCto cto = null;
    if (entity != null) {
      cto = new BookingCto();
      cto.setBooking(getBeanMapper().map(entity, BookingEto.class));
      cto.setTable(getBeanMapper().map(entity.getTable(), TableEto.class));
      cto.setOrder(getBeanMapper().map(entity.getOrder(), OrderEto.class));
      cto.setInvitedGuests(getBeanMapper().mapList(entity.getInvitedGuests(), InvitedGuestEto.class));
      cto.setOrders(getBeanMapper().mapList(entity.getOrders(), OrderEto.class));
    }
    return cto;
  }

  @Override
  public InvitedGuestEto findInvitedGuestByToken(String token) {

    return getBeanMapper().map(getInvitedGuestDao().findInvitedGuestByToken(token), InvitedGuestEto.class);
  }

  @Override
  public Page<BookingCto> findBookingsByUser(BookingSearchCriteriaTo criteria) {

    String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    criteria.setName(username);

    return findBookingCtos(criteria);
  }

  @Override
  public Page<BookingCto> findBookingsByPost(BookingSearchCriteriaTo criteria) {

    return findBookingCtos(criteria);
  }

  @Override
  public Page<BookingCto> findBookingCtos(BookingSearchCriteriaTo criteria) {

    Page<BookingCto> pagListTo = null;
    Page<BookingEntity> bookings = getBookingDao().findBookings(criteria);
    List<BookingCto> ctos = new ArrayList<>();
    for (BookingEntity entity : bookings.getContent()) {
      BookingCto cto = new BookingCto();
      cto.setBooking(getBeanMapper().map(entity, BookingEto.class));
      cto.setInvitedGuests(getBeanMapper().mapList(entity.getInvitedGuests(), InvitedGuestEto.class));
      cto.setOrder(getBeanMapper().map(entity.getOrder(), OrderEto.class));
      cto.setTable(getBeanMapper().map(entity.getTable(), TableEto.class));
      cto.setUser(getBeanMapper().map(entity.getUser(), UserEto.class));
      cto.setOrders(getBeanMapper().mapList(entity.getOrders(), OrderEto.class));
      ctos.add(cto);
    }
    if (ctos.size() > 0) {
      Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), ctos.size());
      pagListTo = new PageImpl<>(ctos, pagResultTo, bookings.getTotalElements());
    }
    return pagListTo;
  }

  @Override
  public boolean cancelBooking(Long bookingId, boolean authorized) {

    BookingEntity booking = getBookingDao().find(bookingId);

    if (!authorized) {
      String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      if (!booking.getName().equals(username)) {
        throw new RuntimeException("Customer cannot cancel bookings of other users.");
      }
    }

    if (Instant.now().isAfter(booking.getExpirationDate())) {
      throw new RuntimeException("It is too late to cancel this booking.");
    }

    // deleting all associated orders
    List<OrderCto> bookingOrders = this.orderManagement.findOrders(bookingId);
    for (OrderCto orderCto : bookingOrders) {
      boolean deleteOrderResult = this.orderManagement.deleteOrder(orderCto.getOrder().getId());
      if (deleteOrderResult) {
        LOG.debug("The order with id '{}' has been deleted.", orderCto.getOrder().getId());
      }
    }

    // deleting all associated guests
    List<InvitedGuestEto> invitedGuests = findInvitedGuestByBooking(bookingId);
    for (InvitedGuestEto invitedGuest : invitedGuests) {
      boolean deletedInvitedGuest = deleteInvitedGuest(invitedGuest.getId());
      if (deletedInvitedGuest) {
        LOG.debug("The invitedGuest with id '{}' has been deleted.", invitedGuest.getId());
      }
    }

    getBookingDao().delete(booking);
    LOG.debug("The booking with id '{}' has been deleted.", bookingId);

    sendCancellationMails(booking);

    return true;
  }

  @Override
  public BookingEto saveBooking(BookingCto booking) {

    Objects.requireNonNull(booking, "booking");
    BookingEntity bookingEntity = getBeanMapper().map(booking.getBooking(), BookingEntity.class);
    bookingEntity.setCanceled(false);

    if (bookingEntity.getBookingType() != null && bookingEntity.getBookingType().isDelivery()) {
      bookingEntity.setTableId(null);
    } else if (bookingEntity.getTableId() != null) {
      bookingEntity.setBookingType(BookingType.COMMON);
    } else {
      bookingEntity.setTableId((long) (Math.random() * (20 - 1) + 1));
      bookingEntity.setBookingType(BookingType.COMMON);
    }

    List<InvitedGuestEntity> invites = getBeanMapper().mapList(booking.getInvitedGuests(), InvitedGuestEntity.class);
    List<InvitedGuestEntity> uniqueInvites = new ArrayList<>();

    for (InvitedGuestEntity invite : invites) {
      try {
        invite.setGuestToken(buildToken(invite.getEmail(), "GB_"));
      } catch (NoSuchAlgorithmException e) {
        LOG.debug("MD5 Algorithm not available at the enviroment");
      }
      // by default null, means the invite is neither accepted nor declined
      invite.setAccepted(null);

      // check if multiple invites with the same mail address exist
      boolean mailExists = false;

      if (invite.getEmail().equals(bookingEntity.getEmail())) {
        mailExists = true;

        if (invites.size() == 1) {
          throw new RuntimeException("Host of a booking cannot only invite himself");
        }
      }

      for (InvitedGuestEntity uniqueInvite : uniqueInvites) {
        if (invite.getEmail().equals(uniqueInvite.getEmail())) {
          mailExists = true;
          break;
        }
      }

      if (!mailExists) {
        uniqueInvites.add(invite);
      }
    }

    bookingEntity.setInvitedGuests(getBeanMapper().mapList(uniqueInvites, InvitedGuestEntity.class));

    if (!uniqueInvites.isEmpty()) {
      bookingEntity.setAssistants(uniqueInvites.size() + 1);
      bookingEntity.setBookingType(BookingType.INVITED);
    } else if (booking.getBooking().getAssistants() != null) {
      bookingEntity.setAssistants(booking.getBooking().getAssistants());
    } else {
      bookingEntity.setAssistants(1);
    }

    try {
      bookingEntity.setBookingToken(buildToken(bookingEntity.getEmail(), "CB_"));
    } catch (NoSuchAlgorithmException e) {
      LOG.debug("MD5 Algorithm not available at the enviroment");
    }

    bookingEntity.setCreationDate(Instant.now());
    bookingEntity.setExpirationDate(bookingEntity.getBookingDate().minus(Duration.ofHours(1)));

    BookingEntity resultEntity = getBookingDao().save(bookingEntity);
    LOG.debug("Booking with id '{}' has been created.", resultEntity.getId());
    for (InvitedGuestEntity invitedGuest : resultEntity.getInvitedGuests()) {
      invitedGuest.setBookingId(resultEntity.getId());
      InvitedGuestEntity resultInvitedGuest = getInvitedGuestDao().save(invitedGuest);
      LOG.info("InvitedGuest with id '{}' has been created.", resultInvitedGuest.getId());
    }

    sendConfirmationMails(resultEntity);

    return getBeanMapper().map(resultEntity, BookingEto.class);
  }

  @Override
  public BookingCto changeBooking(BookingCto booking) {

    Objects.requireNonNull(booking, "booking");
    BookingEntity bookingEntity = getBeanMapper().map(booking.getBooking(), BookingEntity.class);
    bookingEntity = validateChangedBooking(bookingEntity);

    // setting the expiration date 1hr before bookingDate
    bookingEntity.setExpirationDate(bookingEntity.getBookingDate().minus(1, ChronoUnit.HOURS));

    getBookingDao().save(bookingEntity);

    return findBooking(bookingEntity.getId());
  }

  /**
   * Returns the field 'bookingDao'.
   *
   * @return the {@link BookingRepository} instance.
   */
  public BookingRepository getBookingDao() {

    return this.bookingDao;
  }

  @Override
  public InvitedGuestEto findInvitedGuest(Long id) {

    LOG.debug("Get InvitedGuest with id {} from database.", id);
    return getBeanMapper().map(getInvitedGuestDao().find(id), InvitedGuestEto.class);
  }

  @Override
  public List<InvitedGuestEto> findInvitedGuestByBooking(Long bookingId) {

    List<InvitedGuestEntity> invitedGuestList = getInvitedGuestDao().findInvitedGuestByBooking(bookingId);
    List<InvitedGuestEto> invitedGuestEtoList = new ArrayList<>();
    for (InvitedGuestEntity invitedGuestEntity : invitedGuestList) {
      invitedGuestEtoList.add(getBeanMapper().map(invitedGuestEntity, InvitedGuestEto.class));
    }
    return invitedGuestEtoList;
  }

  @Override
  public Page<InvitedGuestEto> findInvitedGuestEtos(InvitedGuestSearchCriteriaTo criteria) {

    Page<InvitedGuestEntity> invitedguests = getInvitedGuestDao().findInvitedGuests(criteria);
    return mapPaginatedEntityList(invitedguests, InvitedGuestEto.class);
  }

  @Override
  public boolean deleteInvitedGuest(Long invitedGuestId) {

    InvitedGuestEntity invitedGuest = getInvitedGuestDao().find(invitedGuestId);
    List<OrderCto> guestOrdersCto = this.orderManagement
        .findOrdersByBookingToken(invitedGuest.getBooking().getBookingToken());
    for (OrderCto orderCto : guestOrdersCto) {
      this.orderManagement.deleteOrder(orderCto.getOrder().getId());
    }
    getInvitedGuestDao().delete(invitedGuest);
    LOG.debug("The invitedGuest with id '{}' has been deleted.", invitedGuestId);
    return true;
  }

  @Override
  public InvitedGuestEto saveInvitedGuest(InvitedGuestEto invitedGuest) {

    Objects.requireNonNull(invitedGuest, "invitedGuest");
    InvitedGuestEntity invitedGuestEntity = getBeanMapper().map(invitedGuest, InvitedGuestEntity.class);

    // initialize, validate invitedGuestEntity here if necessary
    InvitedGuestEntity resultEntity = getInvitedGuestDao().save(invitedGuestEntity);
    LOG.debug("InvitedGuest with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, InvitedGuestEto.class);
  }

  /**
   * Returns the field 'invitedGuestDao'.
   *
   * @return the {@link InvitedGuestRepository} instance.
   */
  public InvitedGuestRepository getInvitedGuestDao() {

    return this.invitedGuestDao;
  }

  @Override
  public TableEto findTable(Long id) {

    LOG.debug("Get Table with id {} from database.", id);
    return getBeanMapper().map(getTableDao().find(id), TableEto.class);
  }

  @Override
  public Page<TableEto> findTableEtos(TableSearchCriteriaTo criteria) {

    Page<TableEntity> tables = getTableDao().findTables(criteria);
    return mapPaginatedEntityList(tables, TableEto.class);
  }

  @Override
  public boolean deleteTable(Long tableId) {

    TableEntity table = getTableDao().find(tableId);
    getTableDao().delete(table);
    LOG.debug("The table with id '{}' has been deleted.", tableId);
    return true;
  }

  @Override
  public TableEto saveTable(TableEto table) {

    Objects.requireNonNull(table, "table");
    TableEntity tableEntity = getBeanMapper().map(table, TableEntity.class);

    // initialize, validate tableEntity here if necessary
    TableEntity resultEntity = getTableDao().save(tableEntity);
    LOG.debug("Table with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, TableEto.class);
  }

  @Override
  public InvitedGuestEto acceptInvite(String guestToken) {

    Objects.requireNonNull(guestToken);
    InvitedGuestEto invited = findInvitedGuestByToken(guestToken);

    if (invited.getAccepted() != null) {
      throw new RuntimeException("Invite has already been accepted or declined");
    }

    invited.setAccepted(true);

    BookingEntity booking = getBookingDao().find(invited.getBookingId());

    // invites for bookings in the past or in the next hour are expired.
    if (Instant.now().isAfter(booking.getExpirationDate())) {
      throw new RuntimeException("This invite has expired");
    }

    InvitedGuestEto savedInvitedGuestEto = saveInvitedGuest(invited);

    sendInviteAcceptedMailToHost(booking, savedInvitedGuestEto);
    sendInviteAcceptedMailToGuest(booking, savedInvitedGuestEto);

    return savedInvitedGuestEto;
  }

  @Override
  public InvitedGuestEto declineInvite(String guestToken) {

    Objects.requireNonNull(guestToken);
    InvitedGuestEto invited = findInvitedGuestByToken(guestToken);

    if (invited.getAccepted() != null) {
      throw new RuntimeException("Invite has already been accepted or declined");
    }
    invited.setAccepted(false);

    BookingEntity booking = getBookingDao().find(invited.getBookingId());
    booking.setAssistants(booking.getAssistants() - 1);
    getBookingDao().save(booking);

    // invites for bookings in the past or in the next hour are expired.
    if (Instant.now().isAfter(booking.getExpirationDate())) {
      throw new RuntimeException("This invite has expired");
    }

    InvitedGuestEto savedInvitedGuestEto = saveInvitedGuest(invited);

    sendInviteDeclinedMailToHost(booking, savedInvitedGuestEto);
    sendInviteDeclinedMailToGuest(booking, savedInvitedGuestEto);

    return savedInvitedGuestEto;
  }

  /**
   * Builds a booking-token or a guest-token.
   *
   * @param email the email of the host of the booking or the guest.
   * @param type the type of the booking. 'CB_' for booking-tokens. 'GB_' for guest-tokens.
   *
   * @return the token.
   */
  private String buildToken(String email, String type) throws NoSuchAlgorithmException {

    Instant now = Instant.now();
    LocalDateTime ldt1 = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
    String date = String.format("%04d", ldt1.getYear()) + String.format("%02d", ldt1.getMonthValue())
        + String.format("%02d", ldt1.getDayOfMonth()) + "_";

    String time = String.format("%02d", ldt1.getHour()) + String.format("%02d", ldt1.getMinute())
        + String.format("%02d", ldt1.getSecond());

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update((email + date + time).getBytes());
    byte[] digest = md.digest();
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return (type + date + sb).substring(0, 23);
  }

  /**
   * Validates all changeable fields of the booking.
   *
   * @param bookingEntity the {@link BookingEntity} with the changed field.
   *
   * @return the valid {@link BookingEntity}.
   */
  private BookingEntity validateChangedBooking(BookingEntity bookingEntity) {

    // bookingDate is null
    if (bookingEntity.getBookingDate() == null) {
      throw new InvalidBookingDateException("bookingDate is null.");
    }
    // new bookingdate is in the past
    if (Instant.now().compareTo(bookingEntity.getBookingDate()) > 0) {
      throw new InvalidBookingDateException("bookingDate is in the past.");
    }

    // bookingId null or no booking with given id
    if (bookingEntity.getId() == null || findBooking(bookingEntity.getId()) == null) {
      throw new InvalidBookingIdException();
    }

    // tableId null
    if (bookingEntity.getTableId() == null) {
      throw new InvalidTableIdException("TableId is null.");
    }

    // given tableId invalid
    if (findTable(bookingEntity.getTableId()) == null) {
      throw new InvalidTableIdException("TableId is invalid.");
    }

    BookingEntity validatedBookingEntity = getBookingDao().find(bookingEntity.getId());
    validatedBookingEntity.setBookingDate(bookingEntity.getBookingDate());
    validatedBookingEntity.setTableId(bookingEntity.getTableId());
    validatedBookingEntity.setName(bookingEntity.getName());
    validatedBookingEntity.setEmail(bookingEntity.getEmail());

    return validatedBookingEntity;
  }

  private void sendConfirmationMails(BookingEntity booking) {

    for (InvitedGuestEntity guest : booking.getInvitedGuests()) {
      sendInviteMailToGuest(booking, guest);
    }

    sendBookingConfirmationMailToHost(booking);
  }

  private void sendCancellationMails(BookingEntity booking) {

    for (InvitedGuestEntity guest : booking.getInvitedGuests()) {
      sendCancellationMailToGuest(booking, guest);
    }

    sendCancellationMailToHost(booking);
  }

  private void sendBookingConfirmationMailToHost(BookingEntity booking) {

    Map<String, Object> templateModel = new HashMap<>();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    templateModel.put("hostName", booking.getName());
    templateModel.put("hostEmail", booking.getEmail());
    templateModel.put("date", dfmt.format(Date.from(booking.getBookingDate())));
    templateModel.put("bookingToken", booking.getBookingToken());
    templateModel.put("attendees", booking.getAssistants());
    templateModel.put("noGuests", booking.getInvitedGuests().isEmpty());
    templateModel.put("invitedGuests", booking.getInvitedGuests());

    this.mailService.sendBookingConfirmationMailToHost(booking.getEmail(), templateModel);
  }

  private void sendInviteAcceptedMailToHost(BookingEntity booking, InvitedGuestEto guest) {

    Map<String, Object> templateModel = new HashMap<>();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    templateModel.put("hostName", booking.getName());
    templateModel.put("hostEmail", booking.getEmail());
    templateModel.put("guestEmail", guest.getEmail());
    templateModel.put("date", dfmt.format(Date.from(booking.getBookingDate())));
    templateModel.put("bookingToken", booking.getBookingToken());
    templateModel.put("tableId", booking.getTableId());
    templateModel.put("attendees", booking.getAssistants());
    templateModel.put("noGuests", booking.getInvitedGuests().isEmpty());
    templateModel.put("invitedGuests", booking.getInvitedGuests());

    this.mailService.sendBookingInviteAcceptMailToHost(booking.getEmail(), templateModel);
  }

  private void sendInviteDeclinedMailToHost(BookingEntity booking, InvitedGuestEto guest) {

    Map<String, Object> templateModel = new HashMap<>();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    templateModel.put("hostName", booking.getName());
    templateModel.put("hostEmail", booking.getEmail());
    templateModel.put("guestEmail", guest.getEmail());
    templateModel.put("date", dfmt.format(Date.from(booking.getBookingDate())));
    templateModel.put("bookingToken", booking.getBookingToken());
    templateModel.put("tableId", booking.getTableId());
    templateModel.put("attendees", booking.getAssistants());
    templateModel.put("noGuests", booking.getInvitedGuests().isEmpty());
    templateModel.put("invitedGuests", booking.getInvitedGuests());

    this.mailService.sendBookingInviteDeclineMailToHost(booking.getEmail(), templateModel);
  }

  private void sendInviteMailToGuest(BookingEntity booking, InvitedGuestEntity guest) {

    Map<String, Object> templateModel = new HashMap<>();

    String inviteURL = getClientUrl() + "/invite?token=" + guest.getGuestToken();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    templateModel.put("invitedEmail", guest.getEmail());
    templateModel.put("hostName", booking.getName());
    templateModel.put("hostEmail", booking.getEmail());
    templateModel.put("date", dfmt.format(Date.from(booking.getBookingDate())));
    templateModel.put("tableId", booking.getTableId());
    templateModel.put("attendees", booking.getAssistants());
    templateModel.put("inviteStatus", guest.getAccepted());
    templateModel.put("inviteURL", inviteURL);

    this.mailService.sendBookingInviteMailToGuest(guest.getEmail(), templateModel);
  }

  private void sendInviteAcceptedMailToGuest(BookingEntity booking, InvitedGuestEto guest) {

    Map<String, Object> templateModel = new HashMap<>();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    templateModel.put("invitedEmail", guest.getEmail());
    templateModel.put("hostName", booking.getName());
    templateModel.put("hostEmail", booking.getEmail());
    templateModel.put("date", dfmt.format(Date.from(booking.getBookingDate())));
    templateModel.put("tableId", booking.getTableId());
    templateModel.put("attendees", booking.getAssistants());
    templateModel.put("inviteStatus", guest.getAccepted());

    this.mailService.sendBookingInviteAcceptMailToGuest(guest.getEmail(), templateModel);
  }

  private void sendInviteDeclinedMailToGuest(BookingEntity booking, InvitedGuestEto guest) {

    Map<String, Object> templateModel = new HashMap<>();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    templateModel.put("invitedEmail", guest.getEmail());
    templateModel.put("hostName", booking.getName());
    templateModel.put("hostEmail", booking.getEmail());
    templateModel.put("date", dfmt.format(Date.from(booking.getBookingDate())));
    templateModel.put("tableId", booking.getTableId());
    templateModel.put("attendees", booking.getAssistants());
    templateModel.put("inviteStatus", guest.getAccepted());

    this.mailService.sendBookingInviteDeclineMailToGuest(guest.getEmail(), templateModel);
  }

  private void sendCancellationMailToGuest(BookingEntity booking, InvitedGuestEntity guest) {

    Map<String, Object> templateModel = new HashMap<>();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    templateModel.put("invitedEmail", guest.getEmail());
    templateModel.put("hostName", booking.getName());
    templateModel.put("hostEmail", booking.getEmail());
    templateModel.put("date", dfmt.format(Date.from(booking.getBookingDate())));
    templateModel.put("bookingToken", booking.getBookingToken());
    templateModel.put("attendees", booking.getAssistants());
    templateModel.put("inviteStatus", guest.getAccepted());

    this.mailService.sendBookingCancellationMailToGuest(guest.getEmail(), templateModel);

  }

  private void sendCancellationMailToHost(BookingEntity booking) {

    Map<String, Object> templateModel = new HashMap<>();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    templateModel.put("hostName", booking.getName());
    templateModel.put("hostEmail", booking.getEmail());
    templateModel.put("date", dfmt.format(Date.from(booking.getBookingDate())));
    templateModel.put("bookingToken", booking.getBookingToken());
    templateModel.put("attendees", booking.getAssistants());
    templateModel.put("noGuests", booking.getInvitedGuests().isEmpty());
    templateModel.put("invitedGuests", booking.getInvitedGuests());

    this.mailService.sendBookingCancellationMailToHost(booking.getEmail(), templateModel);

  }

  /**
   * Returns the field 'tableDao'.
   *
   * @return the {@link TableRepository} instance.
   */
  public TableRepository getTableDao() {

    return this.tableDao;
  }

  private String getClientUrl() {

    HttpServletRequest request = null;
    String clientUrl = null;

    if (RequestContextHolder.getRequestAttributes() != null) {
      request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    if (request != null) {
      clientUrl = request.getHeader("origin");
    }
    if (clientUrl == null) {
      return "http://localhost:" + this.clientPort;
    }
    return clientUrl;
  }

}
