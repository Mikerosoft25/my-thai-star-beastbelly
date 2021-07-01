import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../../core/config/config.service';
import { BookingView, OrderListView, OrderView } from '../../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../../services/waiter-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';

@Component({
  selector: 'app-cockpit-user-order-dialog',
  templateUrl: './order-user-dialog.component.html',
  styleUrls: ['./order-user-dialog.component.scss'],
})
export class OrderUserDialogComponent implements OnInit {
  private fromRow = 0;
  private currentPage = 1;

  pageSize = 4;

  data: any;
  datat: BookingView[] = [];
  columnst: any[];
  displayedColumnsT: string[] = [
    'bookingDate',
    'creationDate',
    'tableId',
    'orderStatus',
    'orderPayStatus'
  ];

  datao: OrderView[] = [];
  columnso: any[];
  displayedColumnsO: string[] = [
    'dish.name',
    'orderLine.comment',
    'extras',
    'orderLine.amount',
    'dish.price',
  ];

  pageSizes: number[];
  filteredData: OrderView[] = this.datao;
  totalPrice: number;

  authAlerts: any;

  constructor(
    public snackBar: SnackBarService,
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: OrderListView,
    private dialog: MatDialogRef<OrderUserDialogComponent>,
    private configService: ConfigService,
  ) {
    this.data = dialogData;
    console.log(this.data.orderStatus.status);
    this.pageSizes = this.configService.getValues().pageSizesDialog;
    this.translocoService
    .selectTranslateObject('alerts.authAlerts')
    .subscribe((content: any) => {
      this.authAlerts = content;
    });
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });

    this.totalPrice = this.waiterCockpitService.getTotalPrice(
      this.data.orderLines,
    );
    this.datao = this.waiterCockpitService.orderComposer(this.data.orderLines);
    this.datat.push(this.data);
    this.filter();
    }

  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columnst = [
          { name: 'bookingDate', label: cockpitTable.reservationDateH },
          { name: 'creationDate', label: cockpitTable.creationDateH },
          { name: 'tableId', label: cockpitTable.tableH },
          { name: 'orderStatus', label: cockpitTable.statusH },
          { name: 'orderPayStatus', label: cockpitTable.payStatusH },
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
        ];
      });
  }

  page(pagingEvent: PageEvent): void {
    this.currentPage = pagingEvent.pageIndex + 1;
    this.pageSize = pagingEvent.pageSize;
    this.fromRow = pagingEvent.pageSize * pagingEvent.pageIndex;
    this.filter();
  }

  filter(): void {
    let newData: any[] = this.datao;
    newData = newData.slice(this.fromRow, this.currentPage * this.pageSize);
    setTimeout(() => (this.filteredData = newData));
  }

  cancelOrderByUser() {
    this.waiterCockpitService
      .cancelOrderByUser(this.datat[0])
      .subscribe(
        (data: any)=> {
          this.datat[0] = data;
          this.snackBar.openSnack(this.authAlerts.orderDeleteSuccess, 4000, 'green');
          this.dialog.close();
        },
        ()=>{
          this.snackBar.openSnack(this.authAlerts.orderDeleteFail, 4000, 'red');
        }
      );

  }
}
