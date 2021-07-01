import { Pageable } from '../backend-models/interfaces';
import { Sort } from '@angular/material/sort';
import { StringifyOptions } from 'querystring';

// DISHES
export interface DishView {
  dish: PlateView;
  image: { content: string };
  extras: ExtraView[];
  likes: number;
  isfav: boolean;
  categories?: { id: string }[];
}

export interface DishViewList {
  dish: PlateView;
  extras: ExtraView[];
  orderLine: {
    amount: number;
    comment: string;
    id?: number;
    deleted?: boolean;
    orderId?: number;
    modificationCounter?: number;
  };
}

export interface DishViewWaiterCockpit {
  dish: PlateView;
  extras: ExtraView[];
  categories?: { id: string }[];
}

export interface PlateView {
  id: number;
  name: string;
  description: string;
  price: number;
}

export interface ExtraView {
  id: number;
  name: string;
  price: number;
  selected?: boolean;
  change?: boolean;
}

export interface DishResponse {
  pageable: Pageable;
  content: DishView;
}

// BOOKING
export interface ReservationView {
  booking: BookingView;
  invitedGuests?: FriendsInvite[];
}

export interface BookingView {
  bookingDate: string;
  name: string;
  email: string;
  assistants?: number;
  tableId?: number;
  bookingToken?: number;
  creationDate?: string;
  orderStatus?: OrderStatus[];
  id?: number;
}

export interface WaiterBookingView {
  bookingDate: string;
  creationDate: string;
  user: string;
  email: string;
  table: number;
  orderStatus: string;
}

export interface OrderStatus {
  id: number;
  status: string;
}

export interface OrderPayStatus {
  payStatus: string;
}

export interface Tables {
  id: number;
  seatsNumber: number;
}

export interface FriendsInvite {
  email: string;
  accepted: boolean;
}
export interface OrderDishView {
  dish: {
    id: number;
    name: string;
    price: number;
  };
}

export interface OrderView {
  dish: {
    id: number;
    name: string;
    price: number;
  };
  orderLine: {
    amount: number;
    comment: string;
    id?: number;
    deleted?: boolean;
    orderId?: number;
    modificationCounter?: number;
  };
  extras: ExtraView[];
}

export interface OrderViewResult {
  dish: {
    id: number;
    name: string;
    price: number;
  };
  orderLine?: {
    amount: number;
    comment: string;
  };
  extras: string;
}

export interface OrderListView {
  order?: {
    id: number;
  };
  id?: number;
  orderLines: OrderView[];
  booking: BookingView;
  orderStatus?: {
    status: string;
    id: number;
  };
  orderPayStatus?: {
    payStatus: string;
  };
}

export interface OrderDishListView {
  orderLines: OrderDishView[];
  booking: BookingView;
}

// Interface to recieve responeses from the server using httpclient for getReservations
export interface BookingResponse {
  pageable: Pageable;
  content: ReservationView;
}

// Interface to recieve responeses from the server using httpclient for get orders
export interface OrderResponse {
  pageable: Pageable;
  content: OrderListView;
}

// Interface to recieve responeses from the server using httpclient for get one single order
export interface OrderResponseDialog {
  order?: {
    id: number;
  };
  id?: number;
  orderLines: OrderView[];
  booking: BookingView;
  orderStatus?: {
    status: string;
    id: number;
  };
  orderPayStatus?: {
    payStatus: string;
  };
}

// Interface to recieve responeses from the server using httpclient for get users
export interface UserResponse {
  pageable: Pageable;
  content: UserListView;
}

// Interface for User Roles
export interface UserRolesResponse {
  pageable: Pageable;
  content: UserRole[];
}

// Interface to recieve responeses from the server using httpclient for get OrderDishResponse
export interface OrderDishResponse {
  pageable: Pageable;
  result: OrderDishListView;
}

// Interface to recieve responeses from the server using httpclient for email invitations
export interface InvitationResponse {
  id: number;
  modificationCounter: number;
  accepted: boolean;
  guestToken: string;
  modificationDate: any;
  revision: any;
}

// Not sure if this should just use bookingview interface, just in case Im creating a new interface that extends booking view
export interface BookingTableResponse extends BookingView {
  bookingType: string;
  canceled: false;
  comment: string;
  expeditionDate: string;
  id: number;
  modificationCounter: number;
  orderId: number;
  revision: any;
  userId: number;
}

// Interface to recieve responeses from the server using httpclient for SaveOrders
export interface SaveOrderResponse {
  bookingId: number;
  bokingToken: string;
  hostId: number;
  id: number;
  invitedGuestId: number;
  modificationCounter: number;
  revision: any;
}

// Interface for admin data

export interface UserListView {
  id?: number;
  username: string;
  email: string;
  twoFactorStatus?: boolean;
  userRoleId: number;
  userRoleName?: string;
}

export interface UserRole {
  name: string;
  id: number;
  active?: boolean;
}

// Roles
export interface Role {
  name: string;
  permission: number;
}

// Interface for prediction data for a dish
export interface OrdersData {
  dates?: Date[];
  holidays?: string[];
  weather?: number[];
  dishes: DishOrdersData[];
}

// Interface for order of a dish
export interface DishOrdersData {
  id: number;
  // name of the dish
  name: string;
  // count of orders of the dish, that have been ordered in certain period
  orders: number[];
}

// Interface for Cluster
export interface Cluster {
  id: number;
  dishId: number;
  dishName: string;
  amount: number;
  polygon: {};
  x: string;
  y: string;
}
export interface ClustersData {
  data: Cluster[];
  id: number;
  modificationCounter: number;
}

// Interface for Two-Factor Authentication
export interface TwoFactorResponse {
  twoFactorStatus?: boolean;
  base64QrCode?: string;
  secret?: string;
}
