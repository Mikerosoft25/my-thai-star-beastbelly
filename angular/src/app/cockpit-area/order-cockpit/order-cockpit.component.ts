// View of currently non-archived orders, can be accessed by users of roles 'waiter' and 'manager'

// Imports
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { TranslocoService } from '@ngneat/transloco';
import * as moment from 'moment';
import { Observable, Subscription } from 'rxjs';
import { ConfigService } from '../../core/config/config.service';
import {
  FilterCockpit,
  Pageable,
} from '../../shared/backend-models/interfaces';
import {
  OrderListView,
  OrderStatus,
  Tables,
} from '../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { OrderDialogComponent } from './order-dialog/order-dialog.component';
import {
  InlineEditBookingDate,
  InlineEditBookingTable,
} from './order-dialog/inline-edit/inline-edit.component';
import { Title } from '@angular/platform-browser';
import { ProgressSpinnerMode } from '@angular/material/progress-spinner';
import { timer } from 'rxjs';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';

@Component({
  selector: 'app-cockpit-order-cockpit',
  templateUrl: './order-cockpit.component.html',
  styleUrls: ['./order-cockpit.component.scss'],
})
export class OrderCockpitComponent implements OnInit, OnDestroy {
  subscription: Subscription;
  refreshTable: Observable<number> = timer(0, 5000);

  authAlerts: any;

  // filter attributes which should be displayed as text
  inHouseText: string;
  orderStatusText: string;
  orderPayStatusText: string;

  // progress spinner
  dataload: boolean = true;
  spinnermode: ProgressSpinnerMode = 'indeterminate';

  // Options for owl-datetime
  dateOptions: any = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    second: 'numeric',
  };

  // Declaration of Arrays to import data from post requests
  tables: Tables[] = [];
  orderStatus: OrderStatus[] = [];
  orders: OrderListView[] = [];
  totalOrders: number;
  orderPayStatus: boolean;

  // Translation
  private translocoSubscription = Subscription.EMPTY;

  // Pageable, Sorting and filters for the requests
  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
  };
  private sorting: { property: any; direction: any }[] = [];
  pageSize = 8;
  selectOptionsInhouse: { name: string; label: any }[];
  selectOptionsPayStatus: { name: string; label: any }[];
  pageSizes: number[];
  filters: FilterCockpit = {
    inHouse: undefined,
    bookingDate: undefined,
    name: undefined,
    paid: undefined,
    bookingToken: undefined,
    orderStatusId: undefined,
    orderPayStatusId: undefined,
    tableId: undefined,
    orderStatusIds: [0, 1, 2],
  };

  // Pagination binding
  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  // Table
  columns: { name: String; label: any }[];
  displayedColumns: string[] = [
    'booking.bookingType',
    'booking.name',
    'booking.bookingDate',
    'booking.table.id',
    'orderStatus.status',
    'orderPayStatus.payStatus',
    'editButton',
  ];

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private transloco: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    private configService: ConfigService,
    public editDialog: MatDialog,
    private title: Title,
    private snackBar: SnackBarService,
  ) {
    this.title.setTitle('Orders');
    this.pageSizes = this.configService.getValues().pageSizes;
    this.transloco
      .selectTranslateObject('alerts.authAlerts')
      .subscribe((content: any) => {
        this.authAlerts = content;
      });
  }

  ngOnInit(): void {
    // Default sorting by date
    this.sorting.push({ property: 'booking.bookingDate', direction: 'asc' });
    // Foremost retrieve data by post request to getting data by default sorting, filter and amount
    this.applyFilters();

    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.subscription = this.refreshTable.subscribe(() => {
      this.applyFilters();
    });
  }

  // Get translations according to the browser language
  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit', {}, lang)
      .subscribe((cockpit) => {
        this.columns = [
          {
            name: 'booking.bookingDate',
            label: cockpit.table.reservationDateH,
          },
          { name: 'booking.name', label: cockpit.table.nameH },
          { name: 'booking.bookingToken', label: cockpit.table.bookingTokenH },
          { name: 'orderStatus.status', label: cockpit.table.statusH },
          { name: 'editButton', label: cockpit.table.editButtonH },
          { name: 'booking.table.id', label: cockpit.table.tableH },
          { name: 'orderPayStatus.payStatus', label: cockpit.table.payStatusH },
          { name: 'advanceOrderStatusArrow', label: cockpit.table.arrowH },
          { name: 'booking.bookingType', label: cockpit.table.bookingTypeH },
        ];
        this.selectOptionsInhouse = [
          { name: 'noselection', label: cockpit.selectOptions.noSelection },
          { name: 'inhouse', label: cockpit.selectOptions.inhouse },
          { name: 'delivery', label: cockpit.selectOptions.delivery },
        ];
        this.selectOptionsPayStatus = [
          { name: 'noselection', label: cockpit.selectOptions.noSelection },
          { name: 'paid', label: cockpit.selectOptions.paid },
          { name: 'notpaid', label: cockpit.selectOptions.notpaid },
        ];
      });
    this.getTables();
    this.getOrderStatus();
  }

  applyFilters(): void {
    // Converts the selected booking date from inline edit component to a isoString so that it's matching the format of the backend
    if (this.filters.bookingDate != undefined) {
      this.filters.bookingDate = new Date(
        new Date(this.filters.bookingDate).setHours(12),
      ).toISOString();
    }
    const tempOrderStatusText = this.filters.orderStatusId;
    if (this.filters.orderStatusId) {
      this.filterOrderStatus(this.filters.orderStatusId);
    }
    // Gets data by post request via waiter-cockpit-service
    this.waiterCockpitService
      .getOrders(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.orders = [];
        } else {
          this.orders = data.content;
        }
        this.totalOrders = data['totalElements'];
      });
    setTimeout(() => (this.dataload = false), 500);
    this.filters.orderStatusId = tempOrderStatusText;
  }

  // Clears the selection of filters
  clearFilters(filters: any): void {
    filters.reset();
    this.filters.bookingDate = null;
    this.filters.inHouse = undefined;
    this.filters.paid = null;
    this.inHouseText = null;
    this.orderStatusText = null;
    this.orderPayStatusText = null;
    this.applyFilters();
    this.pagingBar.firstPage();
  }

  // get all tables for selecting in inline-edit
  getTables() {
    this.waiterCockpitService.getTables().subscribe((data: any) => {
      if (!data) {
        console.log('no data found in tables');
      }
      {
        this.tables = data.content;
      }
    });
  }

  // get all tables for selecting in inline-edit
  getOrderStatus() {
    this.waiterCockpitService.getOrderStatus().subscribe((data: any) => {
      if (!data) {
        console.log('no data found in orderstatus');
      }
      {
        this.orderStatus = data.content;
      }
    });
  }

  // Advance Pay Status of respective order to 'paid', irreversible
  changePayStatus(order: OrderListView): void {
    this.waiterCockpitService
      .changePayStatus(order)
      .subscribe((result: any) => {
        this.applyFilters();
      });
  }

  // Advance Status of respective order to next status, irreversible
  advanceOrderStatus(order: OrderListView): void {
    this.waiterCockpitService.advanceOrderStatus(order).subscribe(
      (result: any) => {
        this.applyFilters();
      },
      () => {
        this.snackBar.openSnack(this.authAlerts.noPayment, 1000, 'red');
      },
    );
  }

  // Since this filter is a string it has to be converted into the corresponding string
  filterOrderStatus(event): void {
    this.orderStatus.forEach((status) => {
      if (JSON.stringify(status.status) == JSON.stringify(event)) {
        this.filters.orderStatusId = status.id;
      }
    });
  }

  // Since this filter is a string it has to be converted into the corresponding string
  filterInhouse(event): void {
    switch (JSON.stringify(event)) {
      case JSON.stringify(this.selectOptionsInhouse[0].label):
        this.filters.inHouse = undefined;
        break;
      case JSON.stringify(this.selectOptionsInhouse[1].label):
        this.filters.inHouse = true;
        break;
      case JSON.stringify(this.selectOptionsInhouse[2].label):
        this.filters.inHouse = false;
        break;
    }
  }

  // Since this filter is a string it has to be converted into the corresponding string
  filterOrderPayStatus(event): void {
    switch (JSON.stringify(event)) {
      case JSON.stringify(this.selectOptionsPayStatus[0].label):
        this.filters.paid = undefined;
        break;
      case JSON.stringify(this.selectOptionsPayStatus[1].label):
        this.filters.paid = true;
        break;
      case JSON.stringify(this.selectOptionsPayStatus[2].label):
        this.filters.paid = false;
        break;
    }
  }

  // Pagination in dependence of the user selection
  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
    };
    this.applyFilters();
  }

  // Sorts the data in dependence of the user selection
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

  // opens order dialog window to manage selected order
  selected(selection: OrderListView): void {
    let openendDialog = this.dialog.open(OrderDialogComponent, {
      width: '80%',
      data: selection,
    });

    // Timeout, so that the data is loaded before refreshing the component
    openendDialog.afterClosed().subscribe((result: any) => {
      this.applyFilters();
    });
  }

  // Running process of transloco has to be stopped on destroying the component
  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
    this.subscription.unsubscribe();
  }

  // update selected field in table with pop up by inline-edit-component
  update(event, i) {
    const control = event.target.getAttribute('name');
    switch (control) {
      case 'booking.bookingDate':
        const dialogRefDate = this.editDialog.open(InlineEditBookingDate, {
          width: '15%',
        });
        dialogRefDate.afterClosed().subscribe((result) => {
          const tempDate = Math.floor(result / 1000).toString();
          this.orders[i].booking.bookingDate =
            result != null ? tempDate : this.orders[i].booking.bookingDate;
          this.waiterCockpitService
            .changeBooking(this.orders[i].booking)
            .subscribe(() => {});
        });
        break;
      case 'booking.table.id':
        const dialogRefTable = this.editDialog.open(InlineEditBookingTable, {
          width: '15%',
          data: this.tables,
        });
        dialogRefTable.afterClosed().subscribe((result) => {
          this.orders[i].booking.tableId =
            result != null ? result : this.orders[i].booking.tableId;
          this.waiterCockpitService
            .changeBooking(this.orders[i].booking)
            .subscribe(() => {});
        });
        break;
    }
  }
}
