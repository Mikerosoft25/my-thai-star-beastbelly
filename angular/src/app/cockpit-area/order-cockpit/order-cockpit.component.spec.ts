import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { ConfigService } from '../../core/config/config.service';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { OrderCockpitComponent } from './order-cockpit.component';
import { config } from '../../core/config/config';
import {
  TestBed,
  ComponentFixture,
  fakeAsync,
  tick,
  async,
  discardPeriodicTasks,
  flush,
} from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslocoService } from '@ngneat/transloco';
import { of } from 'rxjs/internal/observable/of';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getTranslocoModule } from '../../transloco-testing.module';
import { CoreModule } from '../../core/core.module';
import { PageEvent } from '@angular/material/paginator';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from 'app/shared/common/test-utils';
import { ascSortOrder } from '../../../in-memory-test-data/db-order-asc-sort';
import { orderData } from '../../../in-memory-test-data/db-order';
import { tableData } from '../../../in-memory-test-data/db-table';
import { statusData } from '../../../in-memory-test-data/db-status';
import { OWL_DTPICKER_SCROLL_STRATEGY_PROVIDER_FACTORY } from '@busacca/ng-pick-datetime/lib/date-time/date-time-picker.component';
import { OrderDialogComponent } from './order-dialog/order-dialog.component';
import {
  InlineEditBookingDate,
  InlineEditBookingTable,
} from './order-dialog/inline-edit/inline-edit.component';
import { contentTracing } from 'electron';

// mock order dialog
const mockDialog = {
  open: jasmine.createSpy('open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

// mock table header english translations
const translocoServiceStub = {
  selectTranslateObject: of({
    bookingTypeH: "Type",
    nameH: "Name",
    reservationDateH: "Reservation Date",
    tableH: "Table",
    statusH: "Status",
    payStatusH: "Pay Status",
    editButtonH: "Manage",
    // filter headers
    noSelection: "no Selection",
    inhouse: "Inhouse order",
    delivery: "Delivery",
    paid: "Paid",
    notpaid: "Pending",
    // FormFields Placeholders
    inHouse: "In-House",
    orderStatus: "Order Status",
    bookingDate: "Reservation Date",
    name: "Name",
    bookingToken: "Reference number",
    table: "Table number",
    orderPayStatus: "Pay Status",
  } as any),
};

// mock service to return several predefined orders from getOrder-method
const waiterCockpitServiceStub = {
  getOrders: jasmine.createSpy('getOrders').and.returnValue(of(orderData)),
}

// mock service to return several predefined orders sorted ascendingly from getOrder-method
const waiterCockpitServiceSortStub = {
  getOrders: jasmine.createSpy('getOrders').and.returnValue(of(ascSortOrder)),
};

class TestBedSetUp {
  static loadWaiterCockpitServiceStud(waiterCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [
        OrderCockpitComponent,
      ],
      providers: [
        // uses mockDialog instead of real dialog
        { provide: MatDialog, useValue: mockDialog },
        // uses waiterCockpitStub instead of real WaiterCockpitService
        { provide: WaiterCockpitService, useValue: waiterCockpitStub },
        TranslocoService,
        ConfigService,
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
      ],
    });
  }
}

describe('OrderCockpitComponent', () => {
  // declarations
  let component: OrderCockpitComponent;
  let fixture: ComponentFixture<OrderCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let waiterCockpitService: WaiterCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    // stub that returns several predefined orders is given as argument
    TestBedSetUp.loadWaiterCockpitServiceStud(waiterCockpitServiceStub)
      .compileComponents()
      .then(() => {
        // initializations
        fixture = TestBed.createComponent(OrderCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should create component', fakeAsync(() => {
    expect(component).toBeTruthy();
  }));


  it('should verify content and total records of orders', fakeAsync(() => {
    spyOn(translocoService, 'selectTranslateObject').and.returnValue(
      translocoServiceStub.selectTranslateObject
    );

    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);

    tick();
    // fixture.detectChanges();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
    flush();
  }));

  it('should go to next page of orders', () => {
    component.page({
      pageSize: 100,
      pageIndex: 2,
      length: 50,
    });
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  });

  it('should clear form and reset', fakeAsync(() => {
    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);
    tick();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
    flush();
  }));

  /*it('should open OrderDialogComponent dialog on click of manage button', fakeAsync(() => {
    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);

    //fixture.detectChanges();

    const editButton = el.queryAll(By.css('.openOrderDialog'));
    click(editButton[0]);
    tick();
    expect(dialog.open).toHaveBeenCalled();
  }));*/

  it('should filter the order table on click of submit', fakeAsync(() => {

    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);

    const submit = el.query(By.css('.orderApplyFilters'));
    click(submit);
    tick();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
    flush();
  }));
});

describe('TestingOrderCockpitComponentWithSortOrderData', () => {
  let component: OrderCockpitComponent;
  let fixture: ComponentFixture<OrderCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let waiterCockpitService: WaiterCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadWaiterCockpitServiceStud(waiterCockpitServiceSortStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(OrderCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should sort records of orders', () => {
    component.sort({
      active: 'Reservation Date',
      direction: 'asc',
    });
    expect(component.orders).toEqual(ascSortOrder.content);
    expect(component.totalOrders).toBe(8);
  });
});
