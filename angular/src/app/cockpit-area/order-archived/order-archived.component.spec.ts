import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { ConfigService } from '../../core/config/config.service';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { config } from '../../core/config/config';
import {
  TestBed,
  ComponentFixture,
  fakeAsync,
  tick,
  async,
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
import { orderArchivedData } from '../../../in-memory-test-data/db-order-archived';
import { tableData } from '../../../in-memory-test-data/db-table';
import { statusData } from '../../../in-memory-test-data/db-status';
import { OWL_DTPICKER_SCROLL_STRATEGY_PROVIDER_FACTORY } from '@busacca/ng-pick-datetime/lib/date-time/date-time-picker.component';
import { OrderArchivedDialogComponent } from './order-archived-dialog/order-archived-dialog.component';
import { contentTracing } from 'electron';
import { OrderArchivedComponent } from './order-archived.component';

const mockDialog = {
  open: jasmine.createSpy('open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

const waiterCockpitServiceStub = {
  getOrders: jasmine.createSpy('getOrders').and.returnValue(of(orderArchivedData)),
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
  } as any),
};

class TestBedSetUp {
  static loadWaiterCockpitServiceStud(waiterCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [
        OrderArchivedComponent,
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {}},
        // uses mockDialog instead of real dialog
        { provide: OrderArchivedDialogComponent, useValue: mockDialog },
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
  let component: OrderArchivedComponent;
  let fixture: ComponentFixture<OrderArchivedComponent>;
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
        fixture = TestBed.createComponent(OrderArchivedComponent);
        component= fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should create component and verify content and total records of orders', fakeAsync(() => {
    spyOn(translocoService, 'selectTranslateObject').and.callThrough;

    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);

    tick();
    //fixture.detectChanges();
    expect(component).toBeTruthy();
    expect(component.orders).toEqual(orderArchivedData.content);
    expect(component.totalOrders).toBe(8);
  }));

  it('should go to next page of orders', () => {
    component.page({
      pageSize: 100,
      pageIndex: 2,
      length: 50,
    });
    expect(component.orders).toEqual(orderArchivedData.content);
    expect(component.totalOrders).toBe(8);
  });

  it('should clear form and reset', fakeAsync(() => {
    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);
    // fixture.detectChanges();
    tick();
    expect(component.orders).toEqual(orderArchivedData.content);
    expect(component.totalOrders).toBe(8);
  }));

  /*
  it('should open OrderDialogComponent dialog on click of manage button', fakeAsync(() => {
    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);
    // fixture.detectChanges();
    const editButton = el.queryAll(By.css('.mat-row'));
    click(editButton[0]);
    tick();
    expect(dialog.open).toHaveBeenCalled();
  }));
  */

  it('should filter the order table on click of submit', fakeAsync(() => {

    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);

    // fixture.detectChanges();
    const submit = el.query(By.css('.orderApplyFilters'));
    click(submit);
    tick();
    expect(component.orders).toEqual(orderArchivedData.content);
    expect(component.totalOrders).toBe(8);
  }));
});
