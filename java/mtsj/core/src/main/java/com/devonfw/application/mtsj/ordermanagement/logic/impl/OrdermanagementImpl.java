package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.to.InvitedGuestEto;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.IngredientEntity;
import com.devonfw.application.mtsj.dishmanagement.logic.api.Dishmanagement;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.DishEto;
import com.devonfw.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import com.devonfw.application.mtsj.general.common.impl.security.ApplicationAccessControlConfig;
import com.devonfw.application.mtsj.general.logic.base.AbstractComponentFacade;
import com.devonfw.application.mtsj.mailservice.logic.api.Mail;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.CancelNotAllowedException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.ChangingOrderPayStatusNotAllowedException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.ChangingOrderStatusNotAllowedException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.InvalidChangedOrderException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoBookingException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoInviteException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.OrderAlreadyExistException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.WrongTokenException;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderEntity;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderLineEntity;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderPayStatusEntity;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderStatusEntity;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderedDishesPerDayEntity;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderedDishesPerMonthEntity;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo.OrderLineRepository;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo.OrderPayStatusRepository;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo.OrderRepository;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo.OrderStatusRepository;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo.OrderedDishesPerDayRepository;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo.OrderedDishesPerMonthRepository;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderLineSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderPayStatusSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderStatusSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderedDishesCto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderedDishesEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.to.OrderedDishesSearchCriteriaTo;

/**
 * Implementation of component interface of {@link Ordermanagement}
 */
@Named
@Transactional
public class OrdermanagementImpl extends AbstractComponentFacade implements Ordermanagement {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(OrdermanagementImpl.class);

  /**
   * @see #getOrderDao()
   */
  @Inject
  private OrderRepository orderDao;

  /**
   * @see #getOrderLineDao()
   */
  @Inject
  private OrderLineRepository orderLineDao;

  @Inject
  private OrderedDishesPerDayRepository orderedDishesPerDayDao;

  @Inject
  private OrderedDishesPerMonthRepository orderedDishesPerMonthDao;

  @Inject
  private Bookingmanagement bookingManagement;

  @Inject
  private Dishmanagement dishManagement;

  @Inject
  private Mail mailService;

  @Inject
  private OrderPayStatusRepository orderPayStatusDao;

  @Inject
  private OrderStatusRepository orderStatusDao;

  @Value("${client.port}")
  private int clientPort;

  @Value("${server.servlet.context-path}")
  private String serverContextPath;

  @Value("${mythaistar.hourslimitcancellation}")
  private int hoursLimit;

  /**
   * The constructor.
   */
  public OrdermanagementImpl() {

    super();
  }

  @Override
  public OrderCto findOrder(Long id) {

    LOG.debug("Get Order with id {} from database.", id);
    OrderEntity entity = getOrderDao().find(id);

    // manually creating list of orderLineCto necessary, otherwise orderLines not right
    List<OrderLineEntity> orderLineEntities = entity.getOrderLines();
    List<OrderLineCto> orderLineCtos = new ArrayList<>();

    for (OrderLineEntity orderLineEntity : orderLineEntities) {
      OrderLineCto orderLineCto = new OrderLineCto();

      orderLineCto.setDish(getBeanMapper().map(orderLineEntity.getDish(), DishEto.class));
      orderLineCto.setExtras(getBeanMapper().mapList(orderLineEntity.getExtras(), IngredientEto.class));
      orderLineCto.setOrder(getBeanMapper().map(orderLineEntity.getOrder(), OrderEto.class));
      orderLineCto.setOrderLine(getBeanMapper().map(orderLineEntity, OrderLineEto.class));

      orderLineCtos.add(orderLineCto);
    }

    OrderCto cto = new OrderCto();
    cto.setBooking(getBeanMapper().map(entity.getBooking(), BookingEto.class));
    cto.setHost(getBeanMapper().map(entity.getHost(), BookingEto.class));
    cto.setOrderLines(orderLineCtos);
    cto.setOrder(getBeanMapper().map(entity, OrderEto.class));
    cto.setInvitedGuest(getBeanMapper().map(entity.getInvitedGuest(), InvitedGuestEto.class));
    cto.setOrderStatus(getBeanMapper().map(entity.getOrderStatus(), OrderStatusEto.class));
    cto.setOrderPayStatus(getBeanMapper().map(entity.getOrderPayStatus(), OrderPayStatusEto.class));
    return cto;
  }

  @Override
  public OrderEto changeOrderStatus(Long id, OrderStatusEto newOrderStatus) {

    OrderEntity order = getOrderDao().find(id);
    OrderStatusEntity orderStatus = order.getOrderStatus();
    OrderPayStatusEntity orderPayStatus = order.getOrderPayStatus();

    Long nextStatusId = orderStatus.getId();

    if (newOrderStatus != null && newOrderStatus.getId() != null) {
      nextStatusId = newOrderStatus.getId();
    } else {
      if (orderStatus.getStatus().equals("Delivered") && orderPayStatus.getPayStatus().equals("Pending")) {
        throw new ChangingOrderStatusNotAllowedException(
            "Cannot change orderStatus from delivered to complete when order is not paid");
      } else if (orderStatus.getStatus().equals("Complete") || orderStatus.getStatus().equals("Canceled")) {
        throw new ChangingOrderStatusNotAllowedException(
            "Cannot further advance orderStatus from complete or canceled");
      } else {
        nextStatusId = orderStatus.getId() + 1;
      }

    }

    order.setOrderStatusId(nextStatusId);
    OrderEntity resultEntity = getOrderDao().save(order);

    return getBeanMapper().map(resultEntity, OrderEto.class);
  }

  @Override
  public OrderEto changeOrderPayStatus(Long id, OrderPayStatusEto newOrderPayStatus) {

    OrderEntity order = getOrderDao().find(id);
    OrderPayStatusEntity orderPayStatus = order.getOrderPayStatus();

    Long nextOrderPayStatus = orderPayStatus.getId();

    if (newOrderPayStatus != null && newOrderPayStatus.getId() != null) {
      nextOrderPayStatus = newOrderPayStatus.getId();
    } else {
      if (orderPayStatus.getPayStatus().equals("Pending")) {
        nextOrderPayStatus = 1L;
      } else {
        throw new ChangingOrderPayStatusNotAllowedException(
            "Cannot change orderPayStatus from paid (1) to not paid (0)");
      }
    }

    order.setOrderPayStatusId(nextOrderPayStatus);
    OrderEntity resultEntity = getOrderDao().save(order);

    return getBeanMapper().map(resultEntity, OrderEto.class);
  }

  @Override
  public OrderEto cancelOrder(Long id, boolean authorized) {

    OrderEntity order = getOrderDao().find(id);

    if (!authorized) {
      String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      if (!order.getBooking().getName().equals(username)) {
        throw new RuntimeException("Customer cannot cancel order of other users.");
      }
      if (order.getOrderStatusId() != 0L) {
        throw new RuntimeException("Customer can only cancel orders that are 'Received'");
      }
    }

    if (order.getOrderStatusId() == 3L || order.getOrderStatusId() == 4L) {
      throw new RuntimeException("Cannot cancel order that have the status 'Complete' or 'Canceled'");
    }

    // setting orderStatus and orderPayStatus to Canceled
    order.setOrderStatusId(4L);
    order.setOrderPayStatusId(2L);
    OrderEntity resultEntity = getOrderDao().save(order);

    return getBeanMapper().map(resultEntity, OrderEto.class);
  }

  @Override
  public Page<OrderCto> findOrdersByUser(OrderSearchCriteriaTo criteria) {

    String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    criteria.setName(username);

    return findOrderCtos(criteria);
  }

  @Override
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_ORDER)
  public Page<OrderCto> findOrdersByPost(OrderSearchCriteriaTo criteria) {

    return findOrderCtos(criteria);
  }

  @Override
  public List<OrderCto> findOrdersByInvitedGuest(Long invitedGuestId) {

    List<OrderCto> ctos = new ArrayList<>();
    List<OrderEntity> orders = getOrderDao().findOrdersByInvitedGuest(invitedGuestId);
    for (OrderEntity order : orders) {
      processOrders(ctos, order);
    }
    return ctos;

  }

  @Override
  public List<OrderCto> findOrdersByBookingToken(String bookingToken) {

    List<OrderCto> ctos = new ArrayList<>();
    List<OrderEntity> orders = getOrderDao().findOrdersByBookingToken(bookingToken);
    for (OrderEntity order : orders) {
      processOrders(ctos, order);
    }
    return ctos;

  }

  @Override
  public Page<OrderCto> findOrderCtos(OrderSearchCriteriaTo criteria) {

    List<OrderCto> ctos = new ArrayList<>();
    Page<OrderCto> pagListTo = null;
    Page<OrderEntity> orders = getOrderDao().findOrders(criteria);
    for (OrderEntity order : orders.getContent()) {
      processOrders(ctos, order);
    }

    if (ctos.size() > 0) {
      Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), ctos.size());
      pagListTo = new PageImpl<>(ctos, pagResultTo, orders.getTotalElements());
    }
    return pagListTo;
  }

  @Override
  public List<OrderCto> findOrders(Long idBooking) {

    List<OrderCto> ctos = new ArrayList<>();
    List<OrderEntity> orders = getOrderDao().findOrders(idBooking);
    for (OrderEntity order : orders) {
      processOrders(ctos, order);
    }

    return ctos;
  }

  @Override
  public boolean deleteOrder(Long orderId) {

    OrderEntity order = getOrderDao().find(orderId);

    if (!cancellationAllowed(order)) {
      throw new CancelNotAllowedException();
    }
    List<OrderLineEntity> orderLines = getOrderLineDao().findOrderLines(order.getId());

    for (OrderLineEntity orderLine : orderLines) {
      getOrderLineDao().deleteById(orderLine.getId());
    }
    getOrderDao().delete(order);
    LOG.debug("The order with id '{}' has been deleted.", orderId);

    return true;
  }

  @Override
  public OrderCto changeOrder(OrderCto order) {

    Objects.requireNonNull(order, "order");

    OrderEntity orderEntity = getValidatedChangedOrder(order);

    List<OrderLineCto> linesCto = order.getOrderLines();
    List<OrderLineEntity> orderLineEntities = new ArrayList<>();
    for (OrderLineCto lineCto : linesCto) {
      OrderLineEntity orderLineEntity = getBeanMapper().map(lineCto.getOrderLine(), OrderLineEntity.class);
      orderLineEntity.setExtras(getBeanMapper().mapList(lineCto.getExtras(), IngredientEntity.class));
      orderLineEntity.setDishId(lineCto.getOrderLine().getDishId());
      orderLineEntity.setAmount(lineCto.getOrderLine().getAmount());
      orderLineEntity.setComment(lineCto.getOrderLine().getComment());
      orderLineEntities.add(orderLineEntity);
    }

    OrderCto orderCto = new OrderCto();
    orderCto.setOrder(getBeanMapper().map(orderEntity, OrderEto.class));
    orderCto.setBooking(getBeanMapper().map(orderEntity.getBooking(), BookingEto.class));
    orderCto.setHost(getBeanMapper().map(orderEntity.getHost(), BookingEto.class));
    orderCto.setInvitedGuest(getBeanMapper().map(orderEntity.getInvitedGuest(), InvitedGuestEto.class));
    orderCto.setOrderStatus(getBeanMapper().map(orderEntity.getOrderStatus(), OrderStatusEto.class));
    orderCto.setOrderPayStatus(getBeanMapper().map(orderEntity.getOrderPayStatus(), OrderPayStatusEto.class));

    List<OrderLineCto> orderLineCtos = new ArrayList<>();

    for (OrderLineEntity orderLineEntity : orderLineEntities) {
      if (orderLineEntity.getOrderId() == null || orderLineEntity.getId() == null) {
        orderLineEntity.setOrderId(orderEntity.getId());
      }

      if (orderLineEntity.getDeleted() != null && orderLineEntity.getDeleted()) {
        getOrderLineDao().delete(orderLineEntity);
        LOG.info("OrderLine with id '{}' has been deleted.", orderLineEntity.getId());
      } else {
        OrderLineEntity resultEntity = getOrderLineDao().save(orderLineEntity);
        LOG.info("OrderLine with id '{}' has been created.", resultEntity.getId());

        OrderLineCto orderLineCto = new OrderLineCto();

        orderLineCto.setDish(getBeanMapper().map(resultEntity.getDish(), DishEto.class));
        orderLineCto.setExtras(getBeanMapper().mapList(resultEntity.getExtras(), IngredientEto.class));
        orderLineCto.setOrderLine(getBeanMapper().map(resultEntity, OrderLineEto.class));

        orderLineCtos.add(orderLineCto);

      }
    }

    orderCto.setOrderLines(orderLineCtos);

    return orderCto;
  }

  @Override
  public OrderEto saveOrder(OrderCto order) {

    Objects.requireNonNull(order, "order");
    List<OrderLineCto> linesCto = order.getOrderLines();
    List<OrderLineEntity> orderLineEntities = new ArrayList<>();
    for (OrderLineCto lineCto : linesCto) {
      OrderLineEntity orderLineEntity = getBeanMapper().map(lineCto, OrderLineEntity.class);
      orderLineEntity.setExtras(getBeanMapper().mapList(lineCto.getExtras(), IngredientEntity.class));
      orderLineEntity.setDishId(lineCto.getOrderLine().getDishId());
      orderLineEntity.setAmount(lineCto.getOrderLine().getAmount());
      orderLineEntity.setComment(lineCto.getOrderLine().getComment());
      orderLineEntities.add(orderLineEntity);
    }

    OrderEntity orderEntity = getBeanMapper().map(order, OrderEntity.class);

    orderEntity = getValidatedOrder(orderEntity.getBooking().getBookingToken(), orderEntity);
    orderEntity.setOrderLines(orderLineEntities);

    // setting default order status to 0
    OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
    orderStatusEntity.setId(0L);
    orderEntity.setOrderStatus(orderStatusEntity);

    // setting default payStatus to 0 (pending)
    OrderPayStatusEntity orderPayStatusEntity = new OrderPayStatusEntity();
    orderPayStatusEntity.setId(0L);
    orderEntity.setOrderPayStatus(orderPayStatusEntity);

    OrderEntity resultOrderEntity = getOrderDao().save(orderEntity);
    LOG.debug("Order with id '{}' has been created.", resultOrderEntity.getId());

    for (OrderLineEntity orderLineEntity : orderLineEntities) {
      orderLineEntity.setOrderId(resultOrderEntity.getId());
      OrderLineEntity resultOrderLine = getOrderLineDao().save(orderLineEntity);
      LOG.info("OrderLine with id '{}' has been created.", resultOrderLine.getId());
    }

    sendOrderConfirmationEmail(resultOrderEntity);

    return getBeanMapper().map(resultOrderEntity, OrderEto.class);
  }

  @Override
  public OrderCto saveAlexaOrder(OrderCto order, boolean inHouse, boolean deliveryOrder) {

    Objects.requireNonNull(order, "order");

    OrderCto cto = new OrderCto();

    // in House order at the table
    if (inHouse && !deliveryOrder) {
      BookingCto bookingCto = new BookingCto();
      BookingEto bookingEto = new BookingEto();
      bookingEto.setBookingDate(Instant.now());
      bookingEto.setName("in House");
      bookingEto.setEmail("inHouse@myThaiStar.com");

      if (order.getBooking() != null && order.getBooking().getTableId() != null) {
        bookingEto.setTableId(order.getBooking().getTableId());
      } else {
        throw new RuntimeException("tableId cannot be null on inHouse orders");
      }

      bookingCto.setBooking(bookingEto);

      BookingEto inHouseBooking = this.bookingManagement.saveBooking(bookingCto);

      order.setBooking(inHouseBooking);
      cto.setBooking(inHouseBooking);
    }
    // order from home to eat at the restaurant
    else if (!inHouse && !deliveryOrder) {

      // validateAlexaOrderFromHome();

      BookingCto nextBooking = this.bookingManagement.findAssociatedBooking(Instant.now(),
          order.getBooking().getEmail());

      BookingEto nextBookingEto = new BookingEto();
      nextBookingEto.setBookingToken(nextBooking.getBooking().getBookingToken());
      nextBookingEto.setEmail(getValidEmail(nextBooking.getBooking().getEmail()));
      nextBookingEto.setId(nextBooking.getBooking().getId());

      order.setBooking(nextBookingEto);
      cto.setBooking(nextBooking.getBooking());
    }
    // order from home with delivery
    else if (!inHouse && deliveryOrder) {
      BookingCto bookingCto = new BookingCto();
      BookingEto bookingEto = new BookingEto();

      bookingEto.setBookingDate(Instant.now().plus(30, ChronoUnit.MINUTES));
      bookingEto.setBookingType(BookingType.DELIVERY);

      if (order.getBooking() != null) {
        BookingEto orderBookingEto = order.getBooking();
        bookingEto.setName(orderBookingEto.getName());
        bookingEto.setEmail(getValidEmail(orderBookingEto.getEmail()));

        if (bookingEto.getEmail() == null) {
          throw new RuntimeException("Email cannot be null on alexa delivery orders!");
        }

      }

      bookingCto.setBooking(bookingEto);

      BookingEto resultBookingEto = this.bookingManagement.saveBooking(bookingCto);

      order.setBooking(resultBookingEto);
      cto.setBooking(resultBookingEto);
    }
    // order inHouse & delivery not possible
    else {
      throw new RuntimeException("Orders classified as inHouse and delivery are not possible");
    }

    List<OrderLineCto> linesCto = order.getOrderLines();
    List<OrderLineEntity> orderLineEntities = new ArrayList<>();
    for (OrderLineCto lineCto : linesCto) {
      OrderLineEntity orderLineEntity = getBeanMapper().map(lineCto, OrderLineEntity.class);
      orderLineEntity.setExtras(getBeanMapper().mapList(lineCto.getExtras(), IngredientEntity.class));
      orderLineEntity.setDishId(lineCto.getOrderLine().getDishId());
      orderLineEntity.setAmount(lineCto.getOrderLine().getAmount());
      orderLineEntity.setComment(lineCto.getOrderLine().getComment());

      if (orderLineEntity.getAmount() <= 0 || orderLineEntity.getAmount() == null) {
        throw new RuntimeException("OrderLineEntity invalid Amount : " + orderLineEntity.getAmount());
      }

      orderLineEntities.add(orderLineEntity);
    }

    OrderEntity orderEntity = getBeanMapper().map(order, OrderEntity.class);

    orderEntity.setOrderLines(orderLineEntities);

    // setting default order status to 0
    OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
    orderStatusEntity.setId(0L);
    orderEntity.setOrderStatus(orderStatusEntity);

    // setting deafult payStatus to 0 (pending)
    OrderPayStatusEntity orderPayStatusEntity = new OrderPayStatusEntity();
    orderPayStatusEntity.setId(0L);
    orderEntity.setOrderPayStatus(orderPayStatusEntity);

    OrderEntity resultOrderEntity = getOrderDao().save(orderEntity);
    LOG.debug("Order with id '{}' has been created.", resultOrderEntity.getId());

    for (OrderLineEntity orderLineEntity : orderLineEntities) {
      orderLineEntity.setOrderId(resultOrderEntity.getId());
      OrderLineEntity resultOrderLine = getOrderLineDao().save(orderLineEntity);
      LOG.info("OrderLine with id '{}' has been created.", resultOrderLine.getId());
    }

    cto.setHost(getBeanMapper().map(resultOrderEntity.getHost(), BookingEto.class));
    cto.setOrderLines(linesCto);
    cto.setOrder(getBeanMapper().map(resultOrderEntity, OrderEto.class));
    cto.setInvitedGuest(getBeanMapper().map(resultOrderEntity.getInvitedGuest(), InvitedGuestEto.class));
    cto.setOrderStatus(getBeanMapper().map(resultOrderEntity.getOrderStatus(), OrderStatusEto.class));
    cto.setOrderPayStatus(getBeanMapper().map(resultOrderEntity.getOrderPayStatus(), OrderPayStatusEto.class));

    return cto;
  }

  /**
   * Returns the field 'orderDao'.
   *
   * @return the {@link OrderRepository} instance.
   */
  public OrderRepository getOrderDao() {

    return this.orderDao;
  }

  @Override
  public OrderLineEto findOrderLine(Long id) {

    LOG.debug("Get OrderLine with id {} from database.", id);
    return getBeanMapper().map(getOrderLineDao().find(id), OrderLineEto.class);
  }

  @Override
  public Page<OrderLineCto> findOrderLineCtos(OrderLineSearchCriteriaTo criteria) {

    Page<OrderLineEntity> orderlines = getOrderLineDao().findOrderLines(criteria);
    List<OrderLineCto> orderLinesCto = new ArrayList<>();
    for (OrderLineEntity orderline : orderlines.getContent()) {
      OrderLineCto orderLineCto = new OrderLineCto();
      orderLineCto.setOrderLine(getBeanMapper().map(this.orderLineDao.find(orderline.getId()), OrderLineEto.class));
      orderLineCto.setExtras(getBeanMapper().mapList(orderline.getExtras(), IngredientEto.class));
      orderLinesCto.add(orderLineCto);
    }

    Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), orderLinesCto.size());
    Page<OrderLineCto> pagListTo = new PageImpl<>(orderLinesCto, pagResultTo, pagResultTo.getPageSize());
    return pagListTo;
  }

  @Override
  public boolean deleteOrderLine(Long orderLineId) {

    OrderLineEntity orderLine = getOrderLineDao().find(orderLineId);
    getOrderLineDao().delete(orderLine);
    LOG.debug("The orderLine with id '{}' has been deleted.", orderLineId);
    return true;
  }

  @Override
  public OrderLineEto saveOrderLine(OrderLineEto orderLine) {

    Objects.requireNonNull(orderLine, "orderLine");
    OrderLineEntity orderLineEntity = getBeanMapper().map(orderLine, OrderLineEntity.class);

    // initialize, validate orderLineEntity here if necessary
    OrderLineEntity resultEntity = getOrderLineDao().save(orderLineEntity);
    LOG.debug("OrderLine with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, OrderLineEto.class);
  }

  /**
   * Returns the field 'orderLineDao'.
   *
   * @return the {@link OrderLineRepository} instance.
   */
  public OrderLineRepository getOrderLineDao() {

    return this.orderLineDao;
  }

  /**
   * Returns the field 'orderedDishesPerDayDao'.
   *
   * @return the {@link OrderedDishesPerDayRepository} instance.
   */
  public OrderedDishesPerDayRepository getOrderedDishesPerDayDao() {

    return this.orderedDishesPerDayDao;
  }

  /**
   * Returns the field 'orderedDishesPerMonthDao'.
   *
   * @return the {@link OrderedDishesPerMonthRepository} instance.
   */
  public OrderedDishesPerMonthRepository getOrderedDishesPerMonthDao() {

    return this.orderedDishesPerMonthDao;
  }

  @Override
  public Page<OrderedDishesCto> findOrderedDishes(OrderedDishesSearchCriteriaTo criteria) {

    List<OrderedDishesCto> orderedDishesCtos = new ArrayList<>();
    if (criteria.getType() == OrderedDishesSearchCriteriaTo.Type.DAILY) {
      Page<OrderedDishesPerDayEntity> orderedDishes = getOrderedDishesPerDayDao().findOrderedDishesPerDay(criteria);
      for (OrderedDishesPerDayEntity orderedDishesPerDay : orderedDishes.getContent()) {
        OrderedDishesCto orderedDishesCto = new OrderedDishesCto();
        orderedDishesCto.setOrderedDishes(getBeanMapper().map(orderedDishesPerDay, OrderedDishesEto.class));
        orderedDishesCto.setDish(getBeanMapper().map(orderedDishesPerDay.getDish(), DishEto.class));
        orderedDishesCtos.add(orderedDishesCto);
      }
      Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), orderedDishesCtos.size());
      return new PageImpl<>(orderedDishesCtos, pagResultTo, orderedDishes.getTotalElements());
    } else {
      Page<OrderedDishesPerMonthEntity> orderedDishes = getOrderedDishesPerMonthDao()
          .findOrderedDishesPerMonth(criteria);
      for (OrderedDishesPerMonthEntity orderedDishesPerMonth : orderedDishes.getContent()) {
        OrderedDishesCto orderedDishesCto = new OrderedDishesCto();
        orderedDishesCto.setOrderedDishes(getBeanMapper().map(orderedDishesPerMonth, OrderedDishesEto.class));
        orderedDishesCto.setDish(getBeanMapper().map(orderedDishesPerMonth.getDish(), DishEto.class));
        orderedDishesCtos.add(orderedDishesCto);
      }
      Pageable pagResultTo = PageRequest.of(criteria.getPageable().getPageNumber(), orderedDishesCtos.size());
      return new PageImpl<>(orderedDishesCtos, pagResultTo, orderedDishes.getTotalElements());
    }
  }

  @Override
  public OrderStatusEto findOrderStatusById(long id) {

    LOG.debug("Get OrderStatus with id {} from database.", id);
    Optional<OrderStatusEntity> foundEntity = getOrderStatusDao().findById(id);
    if (foundEntity.isPresent())
      return getBeanMapper().map(foundEntity.get(), OrderStatusEto.class);
    else
      return null;
  }

  @Override
  public Page<OrderStatusEto> findOrderStatus(OrderStatusSearchCriteriaTo criteria) {

    Page<OrderStatusEntity> orderstatuss = getOrderStatusDao().findByCriteria(criteria);
    return mapPaginatedEntityList(orderstatuss, OrderStatusEto.class);
  }

  @Override
  public OrderPayStatusEto findOrderPayStatusById(long id) {

    LOG.debug("Get OrderPayStatus with id {} from database.", id);
    Optional<OrderPayStatusEntity> foundEntity = getOrderPayStatusDao().findById(id);
    if (foundEntity.isPresent())
      return getBeanMapper().map(foundEntity.get(), OrderPayStatusEto.class);
    else
      return null;
  }

  @Override
  public Page<OrderPayStatusEto> findOrderPayStatus(OrderPayStatusSearchCriteriaTo criteria) {

    Page<OrderPayStatusEntity> orderpaystatuss = getOrderPayStatusDao().findByCriteria(criteria);
    return mapPaginatedEntityList(orderpaystatuss, OrderPayStatusEto.class);
  }

  /**
   * Returns the field 'orderStatusDao'.
   *
   * @return the {@link OrderStatusRepository} instance.
   */
  public OrderStatusRepository getOrderStatusDao() {

    return this.orderStatusDao;
  }

  /**
   * Returns the field 'orderPayStatusDao'.
   *
   * @return the {@link OrderPayStatusRepository} instance.
   */
  public OrderPayStatusRepository getOrderPayStatusDao() {

    return this.orderPayStatusDao;
  }

  /**
   * @param ctos {@link OrderCto}s to process
   * @param order {@link OrderEntity} to add to CTOs
   */
  private void processOrders(List<OrderCto> ctos, OrderEntity order) {

    OrderCto cto = new OrderCto();
    cto.setBooking(getBeanMapper().map(order.getBooking(), BookingEto.class));
    cto.setHost(getBeanMapper().map(order.getHost(), BookingEto.class));
    cto.setInvitedGuest(getBeanMapper().map(order.getInvitedGuest(), InvitedGuestEto.class));
    cto.setOrder(getBeanMapper().map(order, OrderEto.class));
    cto.setOrderLines(getBeanMapper().mapList(order.getOrderLines(), OrderLineCto.class));
    cto.setOrderStatus(getBeanMapper().map(order.getOrderStatus(), OrderStatusEto.class));
    cto.setOrderPayStatus(getBeanMapper().map(order.getOrderPayStatus(), OrderPayStatusEto.class));
    List<OrderLineCto> orderLinesCto = new ArrayList<>();
    for (OrderLineEntity orderLine : order.getOrderLines()) {
      OrderLineCto orderLineCto = new OrderLineCto();
      orderLineCto.setDish(getBeanMapper().map(orderLine.getDish(), DishEto.class));
      orderLineCto.setExtras(getBeanMapper().mapList(orderLine.getExtras(), IngredientEto.class));
      orderLineCto.setOrderLine(getBeanMapper().map(orderLine, OrderLineEto.class));
      orderLinesCto.add(orderLineCto);
    }
    cto.setOrderLines(orderLinesCto);
    ctos.add(cto);
  }

  /**
   * Validates the fields of the given orderCto.
   *
   * @param orderCto {@link OrderCto} to validate.
   * @throws InvalidChangedOrderException if any validation fails.
   *
   * @return valid {@link OrderCto} mapped to {@link OrderEntity}
   */
  private OrderEntity getValidatedChangedOrder(OrderCto orderCto) {

    OrderEto orderEto = orderCto.getOrder();
    List<OrderLineCto> orderLineCtos = orderCto.getOrderLines();

    if (orderEto.getId() == null) {
      throw new InvalidChangedOrderException("OrderId is null");
    }

    if (getOrderDao().find(orderEto.getId()) == null) {
      throw new InvalidChangedOrderException("No order found with the given id");
    }

    for (OrderLineCto orderLineCto : orderLineCtos) {
      OrderLineEto orderLineEto = orderLineCto.getOrderLine();
      List<IngredientEto> extras = orderLineCto.getExtras();

      // orderLineId is set, meaning its not a new orderline and needs a valid id >= 0
      if (orderLineEto.getId() != null && orderLineEto.getId() < 0) {
        throw new InvalidChangedOrderException("Invalid OrderLineId");
      }

      if (orderLineEto.getDishId() == null || this.dishManagement.findDish(orderLineEto.getDishId()) == null) {
        throw new InvalidChangedOrderException("Invalid dishId");
      }

      if (orderLineEto.getAmount() == null || orderLineEto.getAmount() <= 0) {
        throw new InvalidChangedOrderException("Invalid orderLine amount");
      }

      for (IngredientEto extra : extras) {
        if (extra.getId() == null) {
          throw new InvalidChangedOrderException("Invalid extraId");
        }
      }

    }

    OrderEntity orderEntity = getBeanMapper().map(orderCto, OrderEntity.class);
    orderEntity.setId(orderCto.getOrder().getId());

    return orderEntity;
  }

  /**
   * Validates the given {@link OrderEntity}.
   *
   * @param token booking-token or guest-token.
   * @param order {@link OrderEntity} to validated.
   *
   * @return valid {@link OrderEntity}.
   */
  private OrderEntity getValidatedOrder(String token, OrderEntity orderEntity) {

    // BOOKING VALIDATION
    if (getOrderType(token) == BookingType.COMMON) {
      BookingCto booking = getBookingbyToken(token);
      if (booking == null) {
        throw new NoBookingException();
      }
      List<OrderCto> currentOrders = getBookingOrders(booking.getBooking().getId());
      for (OrderCto currentOrder : currentOrders) {
        // Canceled order
        if (!currentOrder.getOrder().getOrderStatusId().equals(4L)) {
          throw new OrderAlreadyExistException();
        }
      }

      orderEntity.setBookingId(booking.getBooking().getId());

      // GUEST VALIDATION
    } else if (getOrderType(token) == BookingType.INVITED) {

      InvitedGuestEto guest = getInvitedGuestByToken(token);
      if (guest == null) {
        throw new NoInviteException();
      }
      List<OrderCto> currentGuestOrders = getInvitedGuestOrders(guest.getId());
      if (!currentGuestOrders.isEmpty()) {
        throw new OrderAlreadyExistException();
      }
      orderEntity.setBookingId(guest.getBookingId());
      orderEntity.setInvitedGuestId(guest.getId());
    }

    return orderEntity;

  }

  private BookingType getOrderType(String token) throws WrongTokenException {

    if (token.startsWith("CB_")) {
      return BookingType.COMMON;
    } else if (token.startsWith("GB_")) {
      return BookingType.INVITED;
    } else {
      throw new WrongTokenException();
    }
  }

  private String getValidEmail(String email) {

    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (AddressException ex) {
      throw new RuntimeException("Email format is not valid");
    }

    return email;
  }

  private BookingCto getBookingbyToken(String token) {

    return this.bookingManagement.findBookingByToken(token);
  }

  private List<OrderCto> getBookingOrders(Long idBooking) {

    return findOrders(idBooking);
  }

  private InvitedGuestEto getInvitedGuestByToken(String token) {

    return this.bookingManagement.findInvitedGuestByToken(token);
  }

  private List<OrderCto> getInvitedGuestOrders(Long idInvitedGuest) {

    return findOrdersByInvitedGuest(idInvitedGuest);
  }

  private void sendOrderConfirmationEmail(OrderEntity order) {

    Map<String, Object> templateModel = new HashMap<>();

    DateFormat dfmt = new SimpleDateFormat("dd. MMMM yyyy, HH:mm");

    OrderCto orderCto = findOrder(order.getId());
    BookingCto booking = this.bookingManagement.findBooking(order.getBookingId());

    templateModel.put("hostName", booking.getBooking().getName());
    templateModel.put("date", dfmt.format(Date.from(booking.getBooking().getBookingDate())));
    templateModel.put("bookingToken", booking.getBooking().getBookingToken());
    templateModel.put("hostEmail", booking.getBooking().getEmail());
    templateModel.put("hostName", booking.getBooking().getName());
    templateModel.put("attendees", booking.getBooking().getAssistants());

    List<String> dishNames = new ArrayList<>();
    List<String> extras = new ArrayList<>();
    List<Integer> amounts = new ArrayList<>();
    List<BigDecimal> prices = new ArrayList<>();

    BigDecimal totalPrice = BigDecimal.valueOf(0);

    for (OrderLineCto orderLine : orderCto.getOrderLines()) {

      DishEto dish = this.dishManagement.findDish(orderLine.getDish().getId()).getDish();
      dishNames.add(dish.getName());

      Integer amount = orderLine.getOrderLine().getAmount();
      amounts.add(amount);

      BigDecimal price = dish.getPrice().multiply(BigDecimal.valueOf(amount));

      List<IngredientEto> extraEtos = orderLine.getExtras();

      String extrasString = "";

      for (int i = 0; i < extraEtos.size(); i++) {
        IngredientEto extra = this.dishManagement.findIngredient(extraEtos.get(i).getId());
        price = price.add(extra.getPrice());

        extrasString += extra.getName();

        if (i != extraEtos.size() - 1) {
          extrasString += ", ";
        }

      }

      extras.add(extrasString);
      prices.add(price.setScale(2, RoundingMode.CEILING));

      totalPrice = totalPrice.add(price.setScale(2, RoundingMode.CEILING));
    }

    templateModel.put("dishNames", dishNames);
    templateModel.put("extras", extras);
    templateModel.put("amounts", amounts);
    templateModel.put("prices", prices);
    templateModel.put("totalPrice", totalPrice);

    this.mailService.sendOrderConfirmationMail(booking.getBooking().getEmail(), templateModel);
  }

  private boolean cancellationAllowed(OrderEntity order) {

    BookingCto booking = this.bookingManagement.findBooking(order.getBookingId());
    Instant bookingTime = booking.getBooking().getBookingDate();
    long bookingTimeMillis = bookingTime.toEpochMilli();
    long cancellationLimit = bookingTimeMillis - (3600000 * this.hoursLimit);
    long now = Instant.now().toEpochMilli();

    return (now > cancellationLimit) ? false : true;
  }

}
