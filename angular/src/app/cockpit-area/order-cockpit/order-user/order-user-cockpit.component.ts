import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { Title } from '@angular/platform-browser';
import { TranslocoService } from '@ngneat/transloco';
import * as moment from 'moment';
import { Observable, Subscription, timer } from 'rxjs';
import { ConfigService } from '../../../core/config/config.service';
import {
  FilterCockpit,
  Pageable,
} from '../../../shared/backend-models/interfaces';
import { OrderListView } from '../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { OrderDialogComponent } from './../order-dialog/order-dialog.component';
import { OrderUserDialogComponent } from './order-user-dialog/order-user-dialog.component';

@Component({
  selector: 'app-cockpit-user-order-cockpit',
  templateUrl: './order-user-cockpit.component.html',
  styleUrls: ['./order-user-cockpit.component.scss'],
})
export class OrderUserCockpitComponent implements OnInit, OnDestroy {
  subscription: Subscription;
  refreshTable: Observable<number> = timer(0, 5000);

  private translocoSubscription = Subscription.EMPTY;

  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };
  private sorting: { property: any; direction: any }[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  orders: OrderListView[] = [];
  totalOrders: number;

  columns: any[];

  displayedColumns: string[] = [
    'booking.bookingType',
    'booking.bookingDate',
    'booking.bookingToken',
    'booking.table.id',
    'orderStatus.status',
    'orderPayStatus.payStatus',
  ];

  pageSizes: number[];

  filters: FilterCockpit = {
    bookingDate: undefined,
    email: undefined,
    bookingToken: undefined,
  };

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    private configService: ConfigService,
    private title : Title,
  ) {
    this.title.setTitle("My Orders");
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.sorting.push({ property: 'booking.bookingDate', direction: 'asc' });
    this.applyFilters();

    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.subscription = this.refreshTable.subscribe(() => {
      this.applyFilters();
    });
  }

  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columns = [
          { name: 'booking.bookingDate', label: cockpitTable.reservationDateH },
          { name: 'booking.bookingToken', label: cockpitTable.bookingTokenH },
          { name: 'booking.table.id', label: cockpitTable.tableH },
          { name: 'orderStatus.status', label: cockpitTable.statusH },
          { name: 'orderPayStatus.payStatus', label: cockpitTable.payStatusH },
          { name: 'booking.bookingType', label: cockpitTable.bookingTypeH },
        ];
      });
  }

  // TODO: Should canceled orders be removed from the view (like in the waiter cockpit)?
  applyFilters(): void {
    this.waiterCockpitService
      .getOrdersByUser(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.orders = [];
        } else {
          this.orders = data.content;
        }
        this.totalOrders = data.totalElements;
      });
  }

  clearFilters(filters: any): void {
    filters.reset();
    this.applyFilters();
    this.pagingBar.firstPage();
  }

  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
    };
    this.applyFilters();
  }

  sort(sortEvent: Sort): void {
    this.sorting = [];
    if (sortEvent.direction) {
      this.sorting.push({
        property: sortEvent.active,
        direction: '' + sortEvent.direction,
      });
    }
    this.applyFilters();
  }

  selected(selection: OrderListView): void {
    let openendDialog = this.dialog.open(OrderUserDialogComponent, {
      width: '80%',
      data: selection,
    });
    openendDialog.afterClosed().subscribe(() => {
      this.ngOnInit();
    });
  }


  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
    this.subscription.unsubscribe();
  }
}
