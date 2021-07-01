import { DebugElement } from '@angular/core';
import {
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
  async,
} from '@angular/core/testing';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Observable, of } from 'rxjs';
import { Store, State } from '@ngrx/store';
import { By } from '@angular/platform-browser';
import { click } from 'app/shared/common/test-utils';
import { CoreModule } from '../../../core/core.module';
import { SnackBarService } from '../../../core/snack-bar/snack-bar.service';
import { BackendType, config } from '../../../core/config/config';
import {
  emailValidator,
  EmailValidatorDirective,
} from '../../../shared/directives/email-validator.directive';
import { getTranslocoModule } from '../../../transloco-testing.module';
import { BookTableService } from '../../services/book-table.service';
import { BookTableComponent } from './book-table.component';
import { AuthService } from 'app/core/authentication/auth.service';
import { userInfo } from 'os';
import { adminCockpitStub } from 'in-memory-test-data/db-user';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import * as fromApp from 'app/store/reducers';
import { RouterTestingModule } from "@angular/router/testing";
import { AppRoutingModule } from 'app/app-routing.module';
import { RouterModule } from '@angular/router';

const mockDialog = {
  open: jasmine.createSpy('open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

const mockDialogRef = {
  afterClosed: () => of(true),
};

const mockAuthServiceFalse = {
  isLogged: () => of(false),
};

const mockAuthServiceTrue = {
  isLogged: () => of(true),
  getRole: () => of('Customer')
};

const mockBookTableService = {
  getUserDetails: jasmine.createSpy('getUserDetails').and.returnValue(of(adminCockpitStub.content[3])),
};

// as far as I can tell not needed but here in case it turns out I was wrong
const mockConfig = {
  version: 'dev',
  backendType: BackendType.REST,
  restPathRoot: 'http://localhost:8081/mythaistar/',
  restServiceRoot: 'http://localhost:8081/mythaistar/services/rest/',
  loadExternalConfig: false, // load external configuration on /config endpoint
  pageSizes: [8, 16, 24],
  pageSizesDialog: [4, 8, 12],
  roles: [
    { name: 'CUSTOMER', permission: 0 },
    { name: 'WAITER', permission: 1 },
    { name: 'MANAGER', permission: 2 },
  ],
  langs: [
    { label: 'English', value: 'en' },
    { label: 'Deutsch', value: 'de' },
    { label: 'Español', value: 'es' },
    { label: 'Català', value: 'ca' },
    { label: 'Français', value: 'fr' },
    { label: 'Nederlands', value: 'nl' },
    { label: 'हिन्दी', value: 'hi' },
    { label: 'Polski', value: 'pl' },
    { label: 'Русский', value: 'ru' },
    { label: 'български', value: 'bg' },
  ],
  enablePrediction: false,
  enableClustering: false,
  loaded: true,
};


describe('BookTableComponent when User is not logged in', () => {
  let component: BookTableComponent;
  let fixture: ComponentFixture<BookTableComponent>;
  let el: DebugElement;
  let dialog: MatDialog;
  let snackBarService: SnackBarService;
  let mockStore: MockStore<fromApp.State>;

  beforeEach(async(() => {
    const initialState = { config };
    snackBarService = jasmine.createSpyObj('SnackBarService', ['openSnack']);
    TestBed.configureTestingModule({
      declarations: [BookTableComponent, EmailValidatorDirective],
      providers: [
        { provide: BookTableService, useValue: mockBookTableService },
        { provide: SnackBarService, useValue: snackBarService },
        { provide: MatDialog, useValue: mockDialog },
        { provide: MatDialogRef, useValue: mockDialogRef },
        { provide: AuthService, useValue: mockAuthServiceFalse },
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
        RouterModule,
        AppRoutingModule
      ],
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(BookTableComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        dialog = TestBed.inject(MatDialog);
        fixture.detectChanges();
      });
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Email should validate (easy)', () => {
    expect(emailValidator(new FormControl('bad@email').value)).toEqual(false);
    expect(emailValidator(new FormControl('good@email.com').value)).toEqual(
      true,
    );
  });

  it('should show Book Table Dialog', fakeAsync(() => {
    const bookSubmition = el.query(By.css('.bookTableSubmit'));
    click(bookSubmition);
    fixture.detectChanges();
    tick();
    expect(dialog.open).toHaveBeenCalled();
  }));

  it('should show invitation Dialog', fakeAsync(() => {
    const tabs = el.queryAll(By.css('.mat-tab-label'));
    click(tabs[1]);
    fixture.detectChanges();
    tick();
    const inviteSubmition = el.query(By.css('.inviteFriendsSubmit'));
    click(inviteSubmition);
    expect(dialog.open).toHaveBeenCalled();
  }));

  it('should verify invitationModel by removing guest', fakeAsync(() => {
    const tabs = el.queryAll(By.css('.mat-tab-label'));
    click(tabs[1]);
    fixture.detectChanges();
    tick();
    component.invitationModel = ['test1@gmail.com', 'test2@gmail.com'];
    fixture.detectChanges();
    component.removeInvite('test1@gmail.com');
    expect(component.invitationModel.length).toBe(1);
  }));

});

describe('BookTableComponent when User is logged in', () => {
  let component: BookTableComponent;
  let fixture: ComponentFixture<BookTableComponent>;
  let el: DebugElement;
  let dialog: MatDialog;
  let snackBarService: SnackBarService;

  beforeEach(async(() => {
    const initialState = { config };
    snackBarService = jasmine.createSpyObj('SnackBarService', ['openSnack']);
    TestBed.configureTestingModule({
      declarations: [BookTableComponent, EmailValidatorDirective],
      providers: [
        { provide: BookTableService, useValue: mockBookTableService },
        { provide: SnackBarService, useValue: snackBarService },
        { provide: MatDialog, useValue: mockDialog },
        { provide: MatDialogRef, useValue: mockDialogRef },
        { provide: AuthService, useValue: mockAuthServiceTrue },
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
        RouterModule,
        AppRoutingModule
      ],
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(BookTableComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        dialog = TestBed.inject(MatDialog);
        fixture.detectChanges();
      });
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get user details if the user is logged in', fakeAsync(() => {
    expect(component.bookTableService.getUserDetails).toHaveBeenCalled();
    tick();
  }));

});