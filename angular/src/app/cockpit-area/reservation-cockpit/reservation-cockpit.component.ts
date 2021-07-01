import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { ReservationView } from '../../shared/view-models/interfaces';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent, MatPaginator } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { ReservationDialogComponent } from './reservation-dialog/reservation-dialog.component';
import { Pageable } from '../../shared/backend-models/filters';
import * as moment from 'moment';
import { ConfigService } from '../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { Observable, Subscription, timer } from 'rxjs';
import {
  FilterCockpit,
  FilterReservations,
} from 'app/shared/backend-models/interfaces';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-cockpit-reservation-cockpit',
  templateUrl: './reservation-cockpit.component.html',
  styleUrls: ['./reservation-cockpit.component.scss'],
})
export class ReservationCockpitComponent implements OnInit, OnDestroy {
  subscription: Subscription;
  refreshTable: Observable<number> = timer(0, 5000);

  private translocoSubscription = Subscription.EMPTY;

  // Filter, Sorting and Pageable
  filters: FilterReservations = {
    bookingDate: undefined,
    name: undefined,
    bookingToken: undefined,
  };
  private sorting: { property: any; direction: any }[] = [];
  pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
  };
  pageSizes: number[];

  // Pagination binding
  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  // Declaration of Arrays to import data from post requests
  reservations: ReservationView[] = [];
  totalReservations: number;

  // Table
  columns: { name: string; label: any }[];
  displayedColumns: string[] = [
    'bookingDate',
    'name',
    'email',
    'table.id',
    'bookingToken',
  ];

  constructor(
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    private dialog: MatDialog,
    private configService: ConfigService,
    private title: Title,
  ) {
    this.title.setTitle('Reservations');
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    // Foremost retrieve data by post request to getting data by default sorting, filter and amount
    this.applyFilters();
    this.subscription = this.refreshTable.subscribe(() => {
      this.applyFilters();
    });
  }

  // Get translations according to the browser languag
  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columns = [
          { name: 'booking.bookingDate', label: cockpitTable.reservationDateH },
          { name: 'booking.name', label: cockpitTable.nameH },
          { name: 'booking.email', label: cockpitTable.emailH },
          { name: 'booking.table.id', label: cockpitTable.tableH },
          { name: 'booking.bookingToken', label: cockpitTable.bookingTokenH },
        ];
      });
  }

  filter(): void {
    this.pageable.pageNumber = 0;
    this.applyFilters();
  }

  applyFilters(): void {
    // Converts the selected booking date from inline edit component to a isoString so that it's matching the format of the backend
    if (this.filters.bookingDate != undefined) {
      this.filters.bookingDate = new Date(
        new Date(this.filters.bookingDate).setHours(12),
      ).toISOString();
    }
    this.waiterCockpitService
      .getReservations(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.reservations = [];
        } else {
          this.reservations = data.content;
        }
        this.totalReservations = data.totalElements;
      });
  }

  // Clears the selection of filters
  clearFilters(filters: any): void {
    filters.reset();
    this.filters.bookingDate = null;
    this.applyFilters();
    this.pagingBar.firstPage();
  }
  // Pagination in dependence of the user selection
  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
      // total: 1,
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
  // opens reservation dialog window to manage selected order
  selected(selection: ReservationView): void {
    let openendDialog = this.dialog.open(ReservationDialogComponent, {
      width: '80%',
      data: selection,
    });
    openendDialog.afterClosed().subscribe((result: any) => {
      this.applyFilters();
    });
  }

  // Running process of transloco has to be stopped on destroying the component
  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
    this.subscription.unsubscribe();
  }
}
