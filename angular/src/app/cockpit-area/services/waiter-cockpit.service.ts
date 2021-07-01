import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  Filter,
  FilterCockpit,
  FilterReservations,
  Pageable,
  Sort,
} from 'app/shared/backend-models/interfaces';
import { cloneDeep, filter, map } from 'lodash';
import { Observable } from 'rxjs';
import { exhaustMap } from 'rxjs/operators';
import { ConfigService } from '../../core/config/config.service';
import {
  BookingResponse,
  BookingView,
  DishResponse,
  DishViewWaiterCockpit,
  OrderListView,
  OrderResponse,
  OrderStatus,
  OrderView,
  OrderViewResult,
  Tables,
  OrderPayStatus,
  DishViewList,
  OrderResponseDialog,
} from '../../shared/view-models/interfaces';
import { PriceCalculatorService } from '../../sidenav/services/price-calculator.service';

@Injectable()
export class WaiterCockpitService {
  private readonly getReservationsRestPath: string =
    'bookingmanagement/v1/booking/search';
  private readonly getReservationsByUserRestPath: string =
    'bookingmanagement/v1/booking/searchbyuser';
  private readonly cancelReservationRestPath: string =
    'bookingmanagement/v1/booking/cancel/';
  private readonly cancelReservationByUserRestPath: string =
    'bookingmanagement/v1/booking/cancelbyuser/';
  private readonly getOrdersRestPath: string =
    'ordermanagement/v1/order/search';
  private readonly getOrderStatusRestPath: string =
    'ordermanagement/v1/orderstatus/search';
  private readonly getOrderPayStatusRestPath: string =
    'ordermanagement/v1/orderpaystatus/search';
  private readonly getTablesRestPath: string =
    'bookingmanagement/v1/table/search';
  private readonly getDishesRestPath: string = 'dishmanagement/v1/dish/search';
  private readonly filterOrdersRestPath: string =
    'ordermanagement/v1/order/search';
  private readonly changeBookingRestPath: string =
    'bookingmanagement/v1/booking/changebooking';
  private readonly changeOrderRestPath: string =
    'ordermanagement/v1/order/changeorder';
  private readonly getOrderRestPath: string = 'ordermanagement/v1/order/';
  private readonly cancelOrderRestPath: string =
    'ordermanagement/v1/order/cancel/';
  private readonly cancelOrderByUserRestPath: string =
    'ordermanagement/v1/order/cancelbyuser/';

  private readonly getOrderbyUserRestPath: string =
    'ordermanagement/v1/order/searchbyuser';

  // paths to advance order status and change order pay status
  private readonly changeOrderPayStatusRestPath: string =
    'ordermanagement/v1/order/changepaystatus/';
  private readonly advanceOrderStatusRestPath: string =
    'ordermanagement/v1/order/changestatus/';

  private readonly restServiceRoot$: Observable<string> =
    this.config.getRestServiceRoot();

  constructor(
    private http: HttpClient,
    private priceCalculator: PriceCalculatorService,
    private config: ConfigService,
  ) {}

  getOrders(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterCockpit,
  ): Observable<OrderResponse[]> {
    let path: string;
    filters.pageable = pageable;
    filters.pageable.sort = sorting;
    if (
      filters.name ||
      filters.bookingToken ||
      filters.orderStatusId ||
      filters.inHouse ||
      filters.bookingDate ||
      filters.orderPayStatusId ||
      filters.tableId
    ) {
      path = this.filterOrdersRestPath;
    } else {
      delete filters.name;
      delete filters.bookingToken;
      delete filters.bookingDate;
      delete filters.tableId;
      path = this.getOrdersRestPath;
    }
    if (filters.inHouse === null) {
      delete filters.inHouse;
    }
    if (filters.paid === null) {
      delete filters.paid;
    }
    if (filters.orderStatusId === null) {
      delete filters.orderStatusId;
    }

    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<OrderResponse[]>(`${restServiceRoot}${path}`, filters),
      ),
    );
  }

  getOrderStatus(): Observable<OrderStatus[]> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<OrderStatus[]>(
          `${restServiceRoot}${this.getOrderStatusRestPath}`,
          { orderStatus: undefined },
        ),
      ),
    );
  }

  getOrderPayStatus(): Observable<OrderPayStatus[]> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<OrderPayStatus[]>(
          `${restServiceRoot}${this.getOrderPayStatusRestPath}`,
          { payStatus: undefined },
        ),
      ),
    );
  }

  getTables(): Observable<Tables[]> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<Tables[]>(
          `${restServiceRoot}${this.getTablesRestPath}`,
          { pageable: { pageSize: 1000, pageNumber: 0, sort: [] } },
        ),
      ),
    );
  }

  getReservations(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterReservations,
  ): Observable<BookingResponse[]> {
    filters.pageable = pageable;
    filters.pageable.sort = sorting;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<BookingResponse[]>(
          `${restServiceRoot}${this.getReservationsRestPath}`,
          filters,
        ),
      ),
    );
  }

  getReservationsByUser(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterReservations,
  ): Observable<BookingResponse[]> {
    let path: string;
    filters.pageable = pageable;
    filters.pageable.sort = sorting;
    if (filters.name || filters.bookingToken || filters.bookingDate) {
      path = this.getReservationsByUserRestPath;
    } else {
      delete filters.name;
      delete filters.bookingToken;
      delete filters.bookingDate;
      path = this.getReservationsByUserRestPath;
    }
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<BookingResponse[]>(
          `${restServiceRoot}${this.getReservationsByUserRestPath}`,
          filters,
        ),
      ),
    );
  }

  getOrdersByUser(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterCockpit,
  ): Observable<OrderResponse[]> {
    let path: string;
    filters.pageable = pageable;
    filters.pageable.sort = sorting;
    if (filters.email || filters.bookingToken) {
      path = this.filterOrdersRestPath;
    } else {
      delete filters.email;
      delete filters.bookingToken;
      path = this.getOrderbyUserRestPath;
    }
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<OrderResponse[]>(`${restServiceRoot}${path}`, filters),
      ),
    );
  }

  getDishes(
    pageable: Pageable,
  ): Observable<{ pageable: Pageable; content: DishViewList[] }> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<{ pageable: Pageable; content: DishViewList[] }>(
          `${restServiceRoot}${this.getDishesRestPath}`,
          { categories: [], pageable: pageable },
        ),
      ),
    );
  }

  orderComposer(orderList: OrderView[]): OrderView[] {
    const orders: OrderView[] = cloneDeep(orderList);
    map(orders, (o: OrderViewResult) => {
      o.dish.price = this.priceCalculator.getPrice(o);
      o.extras = map(o.extras, 'name').join(', ');
    });
    return orders;
  }

  getTotalPrice(orderLines: OrderView[]): number {
    return this.priceCalculator.getTotalPrice(orderLines);
  }

  changeOrder(booking: any): Observable<BookingResponse[]> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<BookingResponse[]>(
          `${restServiceRoot}${this.changeOrderRestPath}`,
          booking,
        ),
      ),
    );
  }

  getOrder(id: number): Observable<OrderResponseDialog> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.get<OrderResponseDialog>(
          `${restServiceRoot}${this.getOrderRestPath}${id}`,
          {},
        ),
      ),
    );
  }

  changeBooking(booking: BookingView): Observable<BookingResponse[]> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<BookingResponse[]>(
          `${restServiceRoot}${this.changeBookingRestPath}`,
          { booking: booking },
        ),
      ),
    );
  }

  cancelOrder(booking: any) {
    let id = booking.order.id;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<BookingResponse[]>(
          `${restServiceRoot}${this.cancelOrderRestPath}${id}`,
          {},
        ),
      ),
    );
  }

  cancelOrderByUser(booking: any) {
    let id = booking.order.id;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<BookingResponse[]>(
          `${restServiceRoot}${this.cancelOrderByUserRestPath}${id}`,
          {},
        ),
      ),
    );
  }

  cancelReservation(booking: any) {
    let id = booking.booking.id;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.get<BookingResponse[]>(
          `${restServiceRoot}${this.cancelReservationRestPath}${id}`,
          {},
        ),
      ),
    );
  }

  cancelReservationByUser(booking: any) {
    let id = booking.booking.id;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.get<BookingResponse[]>(
          `${restServiceRoot}${this.cancelReservationByUserRestPath}${id}`,
          {},
        ),
      ),
    );
  }

  changePayStatus(order: OrderListView): Observable<OrderResponse> {
    console.log(order.orderPayStatus.payStatus);
    let changePayStatusPath;
    changePayStatusPath =
      this.changeOrderPayStatusRestPath + order.order.id + '/';
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<OrderResponse>(
          `${restServiceRoot}${changePayStatusPath}`,
          {},
        ),
      ),
    );
  }

  advanceOrderStatus(order: OrderListView): Observable<OrderResponse> {
    console.log(order.orderStatus.status);
    let advanceOrderStatusPath;
    advanceOrderStatusPath =
      this.advanceOrderStatusRestPath + order.order.id + '/';
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<OrderResponse>(
          `${restServiceRoot}${advanceOrderStatusPath}`,
          {},
        ),
      ),
    );
  }
}
