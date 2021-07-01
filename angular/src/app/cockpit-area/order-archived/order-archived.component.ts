// View of archived orders, can be accessed by users of roles 'admin', ('manager') and 'waiter'

// Imports
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { TranslocoService } from '@ngneat/transloco';
import { ConfigService } from 'app/core/config/config.service';
import { Pageable, FilterCockpit } from 'app/shared/backend-models/interfaces';
import { Sort } from '@angular/material/sort';
import { OrderListView, Tables } from 'app/shared/view-models/interfaces';
import * as moment from 'moment';
import { Observable, Subscription, timer } from 'rxjs';
import { OrderDialogComponent } from '../order-cockpit/order-dialog/order-dialog.component';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { OrderArchivedDialogComponent } from './order-archived-dialog/order-archived-dialog.component';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-order-archived',
  templateUrl: './order-archived.component.html',
  styleUrls: ['./order-archived.component.scss'],
})
export class OrderArchivedComponent implements OnInit {
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
  tables: Tables[] = [];

  columns: { name: String; label: any }[];

  displayedColumns: string[] = [
    'booking.bookingType',
    'booking.bookingDate',
    'booking.name',
    'booking.table.id',
    'orderStatus.status',
    'orderPayStatus.payStatus',
  ];

  selectOptionsInhouse: { name: string; label: any }[];
  inHouseText: string;
  pageSizes: number[];

  filters: FilterCockpit = {
    bookingDate: undefined,
    inHouse: undefined,
    name: undefined,
    tableId: undefined,
    orderPayStatusId: undefined,
    orderStatusId: undefined,
    bookingToken: undefined,
    orderStatusIds: [3, 4],
  };

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    private configService: ConfigService,
    private title: Title,
  ) {
    this.title.setTitle('Archived Orders');
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.applyFilters();
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.getTables();
    this.subscription = this.refreshTable.subscribe(() => {
      this.applyFilters();
    });
  }

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
          { name: 'booking.table.id', label: cockpit.table.tableH },
          { name: 'orderStatus.status', label: cockpit.table.statusH },
          { name: 'editButton', label: cockpit.table.editButtonH },
          { name: 'orderPayStatus.payStatus', label: cockpit.table.payStatusH },
          { name: 'booking.bookingType', label: cockpit.table.bookingTypeH },
        ];
        this.selectOptionsInhouse = [
          { name: 'noselection', label: cockpit.selectOptions.noSelection },
          { name: 'inhouse', label: cockpit.selectOptions.inhouse },
          { name: 'delivery', label: cockpit.selectOptions.delivery },
        ];
      });
  }

  applyFilters(): void {
    if (this.filters.bookingDate != undefined) {
      this.filters.bookingDate = new Date(
        new Date(this.filters.bookingDate).setHours(12),
      ).toISOString();
    }
    this.waiterCockpitService
      .getOrders(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.orders = [];
        } else {
          this.orders = data.content;
          this.totalOrders = data.totalElements;
        }
      });
  }

  clearFilters(filters: any): void {
    filters.reset();
    this.inHouseText = undefined;
    this.filters.bookingDate = null;
    this.filters.inHouse = null;
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

  // Opens the order-archived dialog component for the selected order
  selected(selection: OrderListView): void {
    let openendDialog = this.dialog.open(OrderArchivedDialogComponent, {
      width: '80%',
      data: selection,
    });
    openendDialog.afterClosed().subscribe((result: any) => {
      this.applyFilters();
    });
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
    this.subscription.unsubscribe();
  }

  // Gets all tables from the server for the table-ID dropdown in the filter area
  getTables() {
    this.waiterCockpitService.getTables().subscribe((data: any) => {
      if (!data) {
        console.log('no data found in tables');
      }
      {
        console.log(data.content);
        this.tables = data.content;
      }
    });
  }

  // Filtering by booking type
  filterInhouse(event) {
    switch (this.inHouseText) {
      case this.selectOptionsInhouse[0].label:
        this.filters.inHouse = undefined;
        break;
      case this.selectOptionsInhouse[1].label:
        this.filters.inHouse = true;
        break;
      case this.selectOptionsInhouse[2].label:
        this.filters.inHouse = false;
        break;
    }
  }
}
