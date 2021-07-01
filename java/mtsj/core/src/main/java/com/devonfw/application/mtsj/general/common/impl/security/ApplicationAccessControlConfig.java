package com.devonfw.application.mtsj.general.common.impl.security;

import javax.annotation.security.RolesAllowed;
import javax.inject.Named;

import com.devonfw.application.mtsj.general.common.api.constants.Roles;
import com.devonfw.module.security.common.api.accesscontrol.AccessControlGroup;
import com.devonfw.module.security.common.base.accesscontrol.AccessControlConfig;

/**
 * This class configures access control objects for authorization. To be used with {@link RolesAllowed} annotation.
 */
@Named
public class ApplicationAccessControlConfig extends AccessControlConfig {

  private static final String APP_ID = "mts";

  private static final String PREFIX = APP_ID + ".";

  // ======================================
  // Permissions for Bookingmanagement
  // ======================================

  public static final String PERMISSION_FIND_BOOKING_BY_ID = PREFIX + "FindBookingByID";

  public static final String PERMISSION_FIND_BOOKING_BY_GUEST_TOKEN = PREFIX + "FindBookingByGuestToken";

  public static final String PERMISSION_SAVE_BOOKING = PREFIX + "SaveBooking";

  public static final String PERMISSION_CHANGE_BOOKING = PREFIX + "ChangeBooking";

  public static final String PERMISSION_CANCEL_BOOKING = PREFIX + "CancelBooking";

  public static final String PERMISSION_CANCEL_BOOKING_BY_USER = PREFIX + "CancelBookingByUser";

  public static final String PERMISSION_SEARCH_BOOKINGS_BY_USER = PREFIX + "FindBookingsByUser";

  public static final String PERMISSION_SEARCH_BOOKINGS = PREFIX + "SearchBookings";

  public static final String PERMISSION_FIND_INVITEDGUESTS = PREFIX + "FindInvitedGuests";

  public static final String PERMISSION_SAVE_INVITEDGUESTS = PREFIX + "SaveInvitedGuests";

  public static final String PERMISSION_DELETE_INVITEDGUESTS = PREFIX + "DeleteInvitedGuests";

  public static final String PERMISSION_SEARCH_INVITEDGUESTS = PREFIX + "SearchInvitedGuests";

  public static final String PERMISSION_FIND_TABLE = PREFIX + "FindTable";

  public static final String PERMISSION_SAVE_TABLE = PREFIX + "SaveTable";

  public static final String PERMISSION_DELETE_TABLE = PREFIX + "DeleteTable";

  public static final String PERMISSION_SEARCH_TABLES = PREFIX + "SearchTables";

  public static final String PERMISSION_ACCEPT_INVITE = PREFIX + "AcceptInvite";

  public static final String PERMISSION_DECLINE_INVITE = PREFIX + "DeclineInvite";

  // ======================================
  // Permissions for Clustermanagement
  // ======================================

  public static final String PERMISSION_FIND_GEO_CLUSTER = PREFIX + "FindGeoCluster";

  // ======================================
  // Permissions for Dishmanagement
  // ======================================

  public static final String PERMISSION_FIND_CATEGORY = PREFIX + "FindCategory";

  public static final String PERMISSION_SAVE_CATEGORY = PREFIX + "SaveCategory";

  public static final String PERMISSION_DELETE_CATEGORY = PREFIX + "DeleteCategory";

  public static final String PERMISSION_SEARCH_CATEGORIES = PREFIX + "SearchCategories";

  public static final String PERMISSION_FIND_DISH = PREFIX + "FindDish";

  public static final String PERMISSION_SAVE_DISH = PREFIX + "SaveDish";

  public static final String PERMISSION_DELETE_DISH = PREFIX + "DeleteDish";

  public static final String PERMISSION_SEARCH_DISHES = PREFIX + "SearchDishes";

  public static final String PERMISSION_FIND_INGREDIENT = PREFIX + "FindIngredient";

  public static final String PERMISSION_SAVE_INGREDIENT = PREFIX + "SaveIngredient";

  public static final String PERMISSION_DELETE_INGREDIENT = PREFIX + "DeleteIngredient";

  public static final String PERMISSION_SEARCH_INGREDIENTS = PREFIX + "SearchIngredients";

  // ======================================
  // Permissions for Imagemanagement
  // ======================================

  public static final String PERMISSION_FIND_IMAGE = PREFIX + "FindImage";

  public static final String PERMISSION_SAVE_IMAGE = PREFIX + "SaveImage";

  public static final String PERMISSION_DELETE_IMAGE = PREFIX + "DeleteImage";

  public static final String PERMISSION_SEARCH_IMAGES = PREFIX + "SearchImages";

  // ======================================
  // Permissions for Ordermanagement
  // ======================================

  public static final String PERMISSION_FIND_ORDER = PREFIX + "FindOrder";

  public static final String PERMISSION_CHANGE_ORDER_STATUS = PREFIX + "ChangeOrderStatus";

  public static final String PERMISSION_CHANGE_ORDER_PAY_STATUS = PREFIX + "ChangeOrderPayStatus";

  public static final String PERMISSION_SAVE_ORDER = PREFIX + "SaveOrder";

  public static final String PERMISSION_SAVE_ALEXA_ORDER_FROM_HOME = PREFIX + "SaveAlexaOrderFromHome";

  public static final String PERMISSION_SAVE_ALEXA_DELIVERY_ORDER_FROM_HOME = PREFIX + "SaveAlexaDeliveryOrderFromHome";

  public static final String PERMISSION_SAVE_ALEXA_ORDER_INHOUSE = PREFIX + "SaveAlexaOrderInHouse";

  public static final String PERMISSION_CHANGE_ORDER = PREFIX + "ChangeOrder";

  public static final String PERMISSION_DELETE_ORDER = PREFIX + "DeleteOrder";

  public static final String PERMISSION_CANCEL_ORDER = PREFIX + "CancelOrder";

  public static final String PERMISSION_CANCEL_ORDER_BY_USER = PREFIX + "CancelOrderByUser";

  public static final String PERMISSION_FIND_ORDERS_BY_USER = PREFIX + "FindOrdersByUser";

  public static final String PERMISSION_SEARCH_ORDERS = PREFIX + "SearchOrders";

  public static final String PERMISSION_FIND_ORDERLINE = PREFIX + "FindOrderLine";

  public static final String PERMISSION_SAVE_ORDERLINE = PREFIX + "SaveOrderline";

  public static final String PERMISSION_DELETE_ORDERLINE = PREFIX + "DeleteOrderline";

  public static final String PERMISSION_SEARCH_ORDERLINE = PREFIX + "SearchOrderline";

  public static final String PERMISSION_FIND_ORDERED_DISHES = PREFIX + "FindOrderedDishes";

  public static final String PERMISSION_FIND_ORDER_STATUS = PREFIX + "FindOrderStatus";

  public static final String PERMISSION_SEARCH_ORDER_STATUS = PREFIX + "SearchOrderStatus";

  public static final String PERMISSION_FIND_ORDER_PAY_STATUS = PREFIX + "FindOrderPayStatus";

  public static final String PERMISSION_SEARCH_ORDER_PAY_STATUS = PREFIX + "SearchOrderPayStatus";

  // ======================================
  // Permissions for Predictionmanagement
  // ======================================

  public static final String PERMISSION_FIND_NEXT_WEEK_PREDICTION = PREFIX + "FindNextWeekPrediction";

  // ======================================
  // Permissions for Usermanagement
  // ======================================

  public static final String PERMISSION_FIND_USER = PREFIX + "FindUser";

  public static final String PERMISSION_FIND_USER_BY_AUTHORIZATION = PREFIX + "FindUserByAuthorization";

  public static final String PERMISSION_FIND_USER_BY_PASSWORD_RESET_TOKEN = PREFIX + "FindUserByPasswordResetToken";

  public static final String PERMISSION_SAVE_USER = PREFIX + "SaveUser";

  public static final String PERMISSION_CHANGE_USER = PREFIX + "ChangeUser";

  public static final String PERMISSION_SAVE_USER_BY_ADMIN = PREFIX + "SaveUserByAdmin";

  public static final String PERMISSION_RESET_PASSWORD = PREFIX + "ResetPassword";

  public static final String PERMISSION_CHANGE_PASSWORD = PREFIX + "ChangePassword";

  public static final String PERMISSION_CHANGE_USER_USER_ROLE = PREFIX + "ChangeUserUserRole";

  public static final String PERMISSION_DELETE_USER = PREFIX + "DeleteUser";

  public static final String PERMISSION_SEARCH_USERS = PREFIX + "SearchUsers";

  public static final String PERMISSION_FIND_USER_ROLE = PREFIX + "FindUserRole";

  public static final String PERMISSION_SEARCH_USER_ROLES = PREFIX + "SearchUserRoles";

  // ======================================
  // Permission Groups
  // ======================================

  public static final String GROUP_ADMIN = Roles.ADMIN;

  public static final String GROUP_WAITER = Roles.WAITER;

  public static final String GROUP_CUSTOMER = Roles.CUSTOMER;

  public static final String GROUP_MANAGER = Roles.MANAGER;

  /**
   * Defining access control groups in the constructor.
   */
  public ApplicationAccessControlConfig() {

    super();

    AccessControlGroup customer = group(GROUP_CUSTOMER, PERMISSION_CANCEL_BOOKING_BY_USER,
        PERMISSION_SEARCH_BOOKINGS_BY_USER, PERMISSION_CANCEL_ORDER_BY_USER, PERMISSION_FIND_ORDERS_BY_USER,
        PERMISSION_FIND_USER_BY_AUTHORIZATION);

    AccessControlGroup waiter = group(GROUP_WAITER, PERMISSION_FIND_BOOKING_BY_ID, PERMISSION_CHANGE_BOOKING,
        PERMISSION_CANCEL_BOOKING, PERMISSION_SEARCH_BOOKINGS, PERMISSION_FIND_INVITEDGUESTS,
        PERMISSION_SEARCH_INVITEDGUESTS, PERMISSION_FIND_TABLE, PERMISSION_SEARCH_TABLES, PERMISSION_FIND_CATEGORY,
        PERMISSION_SEARCH_CATEGORIES, PERMISSION_FIND_DISH, PERMISSION_FIND_INGREDIENT, PERMISSION_SEARCH_INGREDIENTS,
        PERMISSION_FIND_IMAGE, PERMISSION_SEARCH_IMAGES, PERMISSION_FIND_ORDER, PERMISSION_CHANGE_ORDER_STATUS,
        PERMISSION_CHANGE_ORDER_PAY_STATUS, PERMISSION_CHANGE_ORDER, PERMISSION_CANCEL_ORDER, PERMISSION_SEARCH_ORDERS,
        PERMISSION_FIND_ORDERLINE, PERMISSION_SEARCH_ORDERLINE, PERMISSION_FIND_ORDER_STATUS,
        PERMISSION_SEARCH_ORDER_STATUS, PERMISSION_FIND_ORDER_PAY_STATUS, PERMISSION_SEARCH_ORDER_PAY_STATUS);

    // AccessControlGroup manager = group(GROUP_MANAGER, PERMISSION_FIND_BOOKING_BY_ID, PERMISSION_SEARCH_BOOKINGS,
    // PERMISSION_FIND_INVITEDGUESTS, PERMISSION_SEARCH_INVITEDGUESTS, PERMISSION_FIND_TABLE, PERMISSION_SEARCH_TABLES,
    // PERMISSION_FIND_CATEGORY, PERMISSION_SAVE_CATEGORY, PERMISSION_DELETE_CATEGORY, PERMISSION_SEARCH_CATEGORIES,
    // PERMISSION_FIND_DISH, PERMISSION_SAVE_DISH, PERMISSION_DELETE_DISH, PERMISSION_FIND_INGREDIENT,
    // PERMISSION_SAVE_INGREDIENT, PERMISSION_DELETE_INGREDIENT, PERMISSION_SEARCH_INGREDIENTS, PERMISSION_FIND_IMAGE,
    // PERMISSION_SAVE_IMAGE, PERMISSION_DELETE_IMAGE, PERMISSION_SEARCH_IMAGES, PERMISSION_SEARCH_ORDERS,
    // PERMISSION_FIND_ORDERLINE, PERMISSION_SEARCH_ORDERLINE, PERMISSION_FIND_ORDER_STATUS,
    // PERMISSION_SEARCH_ORDER_STATUS, PERMISSION_FIND_ORDER_PAY_STATUS, PERMISSION_SEARCH_ORDER_PAY_STATUS);

    AccessControlGroup manager = group(GROUP_MANAGER, waiter);

    AccessControlGroup admin = group(GROUP_ADMIN, PERMISSION_FIND_BOOKING_BY_ID, PERMISSION_FIND_BOOKING_BY_GUEST_TOKEN,
        PERMISSION_SAVE_BOOKING, PERMISSION_CHANGE_BOOKING, PERMISSION_CANCEL_BOOKING,
        PERMISSION_CANCEL_BOOKING_BY_USER, PERMISSION_SEARCH_BOOKINGS_BY_USER, PERMISSION_SEARCH_BOOKINGS,
        PERMISSION_FIND_INVITEDGUESTS, PERMISSION_SAVE_INVITEDGUESTS, PERMISSION_DELETE_INVITEDGUESTS,
        PERMISSION_SEARCH_INVITEDGUESTS, PERMISSION_FIND_TABLE, PERMISSION_SAVE_TABLE, PERMISSION_DELETE_TABLE,
        PERMISSION_SEARCH_TABLES, PERMISSION_ACCEPT_INVITE, PERMISSION_DECLINE_INVITE, PERMISSION_FIND_GEO_CLUSTER,
        PERMISSION_FIND_CATEGORY, PERMISSION_SAVE_CATEGORY, PERMISSION_DELETE_CATEGORY, PERMISSION_SEARCH_CATEGORIES,
        PERMISSION_FIND_DISH, PERMISSION_SAVE_DISH, PERMISSION_DELETE_DISH, PERMISSION_SEARCH_DISHES,
        PERMISSION_FIND_INGREDIENT, PERMISSION_SAVE_INGREDIENT, PERMISSION_DELETE_INGREDIENT,
        PERMISSION_SEARCH_INGREDIENTS, PERMISSION_FIND_IMAGE, PERMISSION_SAVE_IMAGE, PERMISSION_DELETE_IMAGE,
        PERMISSION_SEARCH_IMAGES, PERMISSION_FIND_ORDER, PERMISSION_CHANGE_ORDER_STATUS,
        PERMISSION_CHANGE_ORDER_PAY_STATUS, PERMISSION_SAVE_ORDER, PERMISSION_SAVE_ALEXA_ORDER_FROM_HOME,
        PERMISSION_SAVE_ALEXA_DELIVERY_ORDER_FROM_HOME, PERMISSION_SAVE_ALEXA_ORDER_INHOUSE, PERMISSION_CHANGE_ORDER,
        PERMISSION_DELETE_ORDER, PERMISSION_CANCEL_ORDER, PERMISSION_CANCEL_ORDER_BY_USER,
        PERMISSION_FIND_ORDERS_BY_USER, PERMISSION_SEARCH_ORDERS, PERMISSION_FIND_ORDERLINE, PERMISSION_SAVE_ORDERLINE,
        PERMISSION_DELETE_ORDERLINE, PERMISSION_SEARCH_ORDERLINE, PERMISSION_FIND_ORDERED_DISHES,
        PERMISSION_FIND_ORDER_STATUS, PERMISSION_SEARCH_ORDER_STATUS, PERMISSION_FIND_ORDER_PAY_STATUS,
        PERMISSION_SEARCH_ORDER_PAY_STATUS, PERMISSION_FIND_NEXT_WEEK_PREDICTION, PERMISSION_FIND_USER,
        PERMISSION_FIND_USER_BY_AUTHORIZATION, PERMISSION_FIND_USER_BY_PASSWORD_RESET_TOKEN, PERMISSION_SAVE_USER,
        PERMISSION_CHANGE_USER, PERMISSION_SAVE_USER_BY_ADMIN, PERMISSION_RESET_PASSWORD, PERMISSION_CHANGE_PASSWORD,
        PERMISSION_CHANGE_USER_USER_ROLE, PERMISSION_DELETE_USER, PERMISSION_SEARCH_USERS, PERMISSION_FIND_USER_ROLE,
        PERMISSION_SEARCH_USER_ROLES);

  }

}