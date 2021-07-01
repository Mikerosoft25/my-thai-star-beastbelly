import {
  async,
  ComponentFixture,
  discardPeriodicTasks,
  fakeAsync,
  flush,
  TestBed,
  tick,
} from '@angular/core/testing';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from 'app/core/core.module';
import { getTranslocoModule } from 'app/transloco-testing.module';
import { config } from '../../core/config/config';

import { AdminCockpitComponent } from './admin-cockpit.component';
import { DebugElement } from '@angular/core';
import { AdminCockpitService } from '../services/admin-cockpit.service';
import { ConfigService } from 'app/core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { provideMockStore } from '@ngrx/store/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { adminCockpitStub } from 'in-memory-test-data/db-user';
import { userRolesStub } from 'in-memory-test-data/db-roles';
import { of } from 'rxjs';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { By } from '@angular/platform-browser';
import { click } from 'app/shared/common/test-utils';

const mockDialog = {
  open: jasmine.createSpy('open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

// Access to translations
const translocoServiceStub = {
  selectTranslateObject: of({
    usernameH: 'Username',
    emailH: 'Email',
    dateH: 'Date',
    roleH: 'Role',
    editButtonH: 'Managa',
  } as any),
};

// Stub of Admin Cockpit Service with method 'getUsers()'

const adminCockpitServiceStub = {
  getUsers: jasmine.createSpy('getUsers').and.returnValue(of(adminCockpitStub)),
  getUserRoles: jasmine.createSpy('getUserRoles').and.returnValue(of(userRolesStub)),
};

// Setup Admin Cockpit Component

class TestBedSetUp {
  static loadAdminCockpitServiceStud(adminCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [AdminCockpitComponent],
      imports: [
        CoreModule,
        BrowserAnimationsModule,
        getTranslocoModule(),
        RouterTestingModule,
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialog, useValue: mockDialog },
        { provide: AdminCockpitService, useValue: adminCockpitServiceStub },
        ConfigService,
        TranslocoService,
        provideMockStore({ initialState }),
      ],
    });
  }
}

// Test section

describe('AdminCockpitComponent', () => {
  let component: AdminCockpitComponent;
  let fixture: ComponentFixture<AdminCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let adminCockpitService: AdminCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  // Initializations before compiling the component

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadAdminCockpitServiceStud(adminCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(AdminCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        adminCockpitService = TestBed.inject(AdminCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  // Verify component is create

  it('should create component', fakeAsync(() => {
    expect(component).toBeTruthy();
  }));

  // Verify the expected data and the amount of them is loaded

  it('verify content and total records of users', fakeAsync(() => {
    spyOn(translocoService, 'selectTranslateObject').and.returnValue(
      translocoServiceStub.selectTranslateObject,
    );
    fixture.detectChanges();
    tick(500);
    expect(component.users).toEqual(adminCockpitStub.content);
    expect(component.totalUsers).toBe(13);
    flush();
  }));

  // Test for pagination

  it('should go to next page of users', fakeAsync(() => {
    component.page({
      pageSize: 100,
      pageIndex: 2,
      length: 50,
    });
    expect(component.users).toEqual(adminCockpitStub.content);
    expect(component.totalUsers).toBe(13);
    flush();
  }));

  // Verify that clear filter is clearing and reset the form

  it('should clear form and reset', fakeAsync(() => {
    const clearFilter = el.query(By.css('.userClearFilters'));
    click(clearFilter);
    fixture.detectChanges();
    tick(500);
    expect(component.users).toEqual(adminCockpitStub.content);
    expect(component.totalUsers).toBe(13);
    flush();
  }));

  // Verify that the AdminUserManage dialog will open when clicking on the manage button

  it('should open AdminUserManage dialog on click on manage button', fakeAsync(() => {
    fixture.detectChanges();
    const editButton = el.queryAll(By.css('.openUserManage'));
    editButton[0].triggerEventHandler('click', null);
    tick(500);
    discardPeriodicTasks();
    expect(dialog.open).toHaveBeenCalled();
    flush();
  }));

  // Verify that the table is filtered when applying filters

  it('should filter the users table on click of submit', fakeAsync(() => {
    fixture.detectChanges();
    const submit = el.query(By.css('.orderApplyFilters'));
    click(submit);
    tick(500);
    expect(component.users).toEqual(adminCockpitStub.content);
    expect(component.totalUsers).toBe(13);
    flush();
  }));
});
