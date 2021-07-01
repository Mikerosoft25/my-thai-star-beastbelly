import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TranslocoService } from '@ngneat/transloco';
import { WaiterCockpitService } from 'app/cockpit-area/services/waiter-cockpit.service';
import { Pageable } from 'app/shared/backend-models/interfaces';
import {
  DishViewList,
  OrderListView,
  OrderView,
} from 'app/shared/view-models/interfaces';
import { OrderDialogComponent } from '../order-dialog.component';

@Component({
  selector: 'app-order-add',
  templateUrl: './order-add.component.html',
  styleUrls: ['./order-add.component.scss'],
})
export class OrderAddComponent implements OnInit {
  private pageable: Pageable = {
    pageSize: 10000,
    pageNumber: 0,
    sort: [],
  };

  // Declaration of Arrays to import data from post requests
  orderAddDialog: MatDialogRef<OrderDialogComponent>;
  booking: OrderListView;
  dishes: DishViewList[];
  ordersToAdd: OrderView[] = [];

  // Table
  columns: {
    name: string;
    label: any;
    numeric?: boolean;
    format?: (v: number) => string;
  }[];
  displayedColumns: string[] = [
    'dish.name',
    'extras',
    'amount',
    'comment',
    'check',
  ];

  constructor(
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    @Inject(MAT_DIALOG_DATA) dialogData: OrderListView,
    private dialog: MatDialogRef<OrderDialogComponent>,
  ) {
    // Get data from previous selection in order-cokpit-component
    this.booking = dialogData;
    this.orderAddDialog = dialog;
  }

  ngOnInit(): void {
    this.getDishes();
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });
    this.ordersToAdd = this.booking.orderLines;
  }

  // Get translations according to the browser language
  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.orders.dialogTable', {}, lang)
      .subscribe((cockpitDialogTable) => {
        this.columns = [
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
          { name: 'check', label: cockpitDialogTable.checkH },
        ];
      });
  }

  // Get all dishes
  getDishes() {
    this.waiterCockpitService
      .getDishes(this.pageable)
      .subscribe((data: any) => {
        if (!data) {
          console.log('no data found in tables');
        }
        {
          this.dishes = data.content;
          // init amount and comment field to prepare orderline
          this.dishes.forEach(function (dish) {
            dish.orderLine = { amount: 0, comment: '' };
          });
        }
      });
  }

  //add order to temporary array for adding them to the system
  toggleOrder(event, element) {
    // had to add some arguments to object for post request
    if (element.orderLine.amount < 1) {
      element.orderLine.amount = 1;
    }
    delete element.categories;
    delete element.image;
    element.order = null;
    element.orderLine.dishId = element.dish.id;
    element.orderLine.modificationCounter = null;
    element.orderLine.orderId = this.booking.order.id;
    element.orderLine.deleted = null;

    if (event.checked) {
      this.ordersToAdd.push(element);
    } else {
      this.ordersToAdd = this.ordersToAdd.filter(
        (order) => order.dish.id != element.dish.id,
      );
    }
  }

  // change object arguments as their changed in html
  changeComment(element, event) {
    element.orderLine.comment = event.target.value;
  }
  // change object arguments as their changed in html
  changeAmount(element, event) {
    element.orderLine.amount = Number(event.target.value);
  }

  addExtra(element, value, event) {
    if (event.checked) {
      // value.change = true;
      // element.extras.filter((extra) => {
      //   return extra.id != value.id;
      // });
      // element.extras.push(value);
      element.extras.forEach((extra) => {
        if (extra.id == value.id) {
          extra.change = true;
        }
      });
    }
  }

  // Post request with waiter-cockpit-service to add orders to selected reservation and close dialog
  addOrders() {
    this.ordersToAdd.forEach((order) => {
      order.extras.forEach((extra, index) => {
        if (extra.change != true) {
          order.extras.splice(index, 1);
        } else {
          delete order.extras[index].change;
        }
      });
    });
    this.booking.orderLines = this.ordersToAdd;
    this.waiterCockpitService.changeOrder(this.booking).subscribe(() => {
      this.orderAddDialog.close();
    });
  }
}
