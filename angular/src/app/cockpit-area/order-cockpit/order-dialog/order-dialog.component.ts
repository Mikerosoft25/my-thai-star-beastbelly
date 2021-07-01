import { Component, Inject, OnInit } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import {
  OrderListView,
  OrderStatus,
  OrderView,
  OrderPayStatus,
  Tables,
  WaiterBookingView,
  OrderResponseDialog,
} from '../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { OrderAddComponent } from './order-add/order-add.component';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';

@Component({
  selector: 'app-cockpit-order-dialog',
  templateUrl: './order-dialog.component.html',
  styleUrls: ['./order-dialog.component.scss'],
})
export class OrderDialogComponent implements OnInit {
  // Options for owl-datetime
  dateOptions: any = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    second: 'numeric',
  };

  private fromRow = 0;
  private currentPage = 1;

  // booking form
  bookingInfo: WaiterBookingView;
  commentIndex: number;
  orderInfo: OrderView;
  pageSize = 4;
  date: string;
  pageSizes: number[];

  // Declaration of Arrays to import data from post requests
  orderStatus: OrderStatus[] = [];
  orderPayStatus: OrderPayStatus[] = [];
  tables: Tables[] = [];
  data: OrderListView;
  datat: OrderListView[] = [];
  datao: OrderView[] = [];
  filteredData: OrderView[] = this.datao;
  totalPrice: number;

  // Table
  columnst: { name: string; label: any }[];
  displayedColumnsT: string[] = [
    'bookingDate',
    'creationDate',
    'name',
    'email',
    'tableId',
    'orderStatus',
    'orderPayStatus',
  ];
  columnso: {
    name: string;
    label: any;
    numeric?: boolean;
    format?: (v: number) => string;
  }[];
  displayedColumnsO: string[] = [
    'dish.name',
    'orderLine.comment',
    'extras',
    'orderLine.amount',
    'dish.price',
    'deleteButton',
  ];

  constructor(
    public snackBar: SnackBarService,
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: OrderListView,
    private dialog: MatDialogRef<OrderDialogComponent>,
    public editDialog: MatDialog,
    private configService: ConfigService,
  ) {
    this.data = dialogData;
    this.pageSizes = this.configService.getValues().pageSizesDialog;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });
    this.totalPrice = this.waiterCockpitService.getTotalPrice(
      this.data.orderLines,
    );
    // Foremost retrieve data by post request to getting data by default sorting, filter and amount
    this.datao = this.waiterCockpitService.orderComposer(this.data.orderLines);
    this.datat.push(this.data);
    this.filter();
    this.getOrderPayStatus();
    this.date = this.data.booking.bookingDate;
  }

  // Translations according to the browser language
  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columnst = [
          { name: 'bookingDate', label: cockpitTable.reservationDateH },
          { name: 'creationDate', label: cockpitTable.creationDateH },
          { name: 'name', label: cockpitTable.ownerH },
          { name: 'email', label: cockpitTable.emailH },
          { name: 'tableId', label: cockpitTable.tableH },
          { name: 'orderPayStatus', label: cockpitTable.payStatusH },
          { name: 'orderStatus', label: cockpitTable.statusH },
        ];
      });

    this.translocoService
      .selectTranslateObject('cockpit.orders.dialogTable', {}, lang)
      .subscribe((cockpitDialogTable) => {
        this.columnso = [
          { name: 'dish.name', label: cockpitDialogTable.dishH },
          { name: 'orderLine.comment', label: cockpitDialogTable.commentsH },
          { name: 'extras', label: cockpitDialogTable.extrasH },
          { name: 'orderLine.amount', label: cockpitDialogTable.quantityH },
          {
            name: 'dish.price',
            label: cockpitDialogTable.priceH,
            numeric: true,
            format: (v: number) => v.toFixed(2),
          },
          { name: 'deleteButton', label: cockpitDialogTable.deleteButtonH },
        ];
      });
  }

  // Pagination for orders table
  page(pagingEvent: PageEvent): void {
    this.currentPage = pagingEvent.pageIndex + 1;
    this.pageSize = pagingEvent.pageSize;
    this.fromRow = pagingEvent.pageSize * pagingEvent.pageIndex;
    this.filter();
  }

  // Retrieve data in dependence of pagination
  filter(): void {
    let newData: OrderView[] = this.datao;
    newData = newData.slice(this.fromRow, this.currentPage * this.pageSize);
    setTimeout(() => (this.filteredData = newData));
  }

  // Post data to booking and order table
  applyChanges() {
    this.waiterCockpitService
      .changeOrder(this.datat[0])
      .subscribe((data: any) => {
        if (data) {
          this.datat[0] = data;
          this.snackBar.openSnack('Order correctly edited', 4000, 'green');
        } else {
          this.snackBar.openSnack('Something went wrong', 4000, 'red');
        }
      });

    this.dialog.close();
  }

  // Cancel the current order
  cancelOrder() {
    this.waiterCockpitService
      .cancelOrder(this.datat[0])
      .subscribe((data: any) => {
        if (data) {
          this.datat[0] = data;
          this.snackBar.openSnack('Order has been cancelled', 4000, 'green');
          this.dialog.close();
        } else {
          this.snackBar.openSnack('Something went wrong', 4000, 'red');
          this.dialog.close();
        }
      });
  }

  // Post request for order pay status from current reservation
  getOrderPayStatus() {
    this.waiterCockpitService.getOrderPayStatus().subscribe((data: any) => {
      if (!data) {
        console.log('no data found in orderpaystatus');
      }
      {
        this.orderPayStatus = data.content;
      }
    });
  }

  // Deletes an order when the user clicks the bin button in a row
  delete(order: OrderView, index: number): void {
    // The orderlines has a row 'deleted' which has to be set to true
    const orderId = order.orderLine.id;
    this.datat[0].orderLines.forEach((element) => {
      if (element.orderLine.id == orderId) {
        element.orderLine.deleted = true;
      }
    });
    // For now the total price calculation is hardcoded here
    this.totalPrice -= order.dish.price;
    this.filteredData = this.filteredData.filter((value, key) => {
      return value.orderLine.id != order.orderLine.id;
    });
  }

  // Creates dialog to insert orders (post request from orderAddComponent)
  insertOrder(): void {
    const dialogRef = this.editDialog.open(OrderAddComponent, {
      width: '100%',
      height: '80%',
      data: this.datat[0],
    });
    // As the user finished adding orders, the data need to be refreshed
    dialogRef.afterClosed().subscribe(() => {
      var currentOrder: OrderView[];
      this.waiterCockpitService
        .getOrder(this.datat[0].booking.id)
        .subscribe((responseOrder: OrderResponseDialog) => {
          this.datao = this.waiterCockpitService.orderComposer(
            responseOrder.orderLines,
          );
          this.filter();
          this.datat[0] = responseOrder;
        });

      this.totalPrice = this.waiterCockpitService.getTotalPrice(
        this.datat[0].orderLines,
      );
    });
  }
}
