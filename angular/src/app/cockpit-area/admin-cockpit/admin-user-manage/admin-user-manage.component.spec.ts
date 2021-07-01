import {
  async,
  ComponentFixture,
  fakeAsync,
  flush,
  TestBed,
  tick,
} from '@angular/core/testing';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ConfigService } from 'app/core/config/config.service';
import { CoreModule } from 'app/core/core.module';
import { config } from '../../../core/config/config';
import { AdminUserManageComponent } from './admin-user-manage.component';
import { Store } from '@ngrx/store';
import { State } from '../../../store';
import { AdminCockpitService } from '../../services/admin-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { DebugElement } from '@angular/core';
import { adminUserManageStub } from 'in-memory-test-data/db-user-manage';
import { getTranslocoModule } from 'app/transloco-testing.module';
import { provideMockStore } from '@ngrx/store/testing';
import { of } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { WaiterCockpitModule } from 'app/cockpit-area/cockpit.module';
import { By } from '@angular/platform-browser';
import { UserAreaService } from 'app/user-area/services/user-area.service';
import { time } from 'console';

// const mockDialog = {
//   open: jasmine.createSpy('open').and.returnValue({
//     afterClosed: () => of(true),
//   }),
// };

// Access to translations
const translocoServiceStub = {
  selectTranslateObject: of({
    title: 'Manage user',
    image: 'Default image for user page',
    changeName: 'Change the username',
    nameRequired: 'nameRequired',
    changeMail: 'Change the mail address',
    emailRequired: 'Please insert an e-mail-address',
    emailPattern: 'E-mail-address not valid',
    password: 'Password',
    tooShortPassword: 'Your Password must contain at least 6 characters.',
    role: 'Role',
    noChangeRole: 'Role (cannot be altered)',
    delete: 'Delete',
    resetPassword: 'Reset Password',
    agree: 'Yes',
  } as any),
};

// Stub of Admin Cockpit Service with method 'getUsers()'

// const adminCockpitServiceStub = {
//   getUsers: jasmine.createSpy('getUsers').and.returnValue(of(adminCockpitStub)),
// };

// Setup Admin Cockpit Component

class TestBedSetUp {
  static loadAdminCockpitServiceStud(adminUserManageStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [AdminUserManageComponent],
      imports: [
        CoreModule,
        BrowserAnimationsModule,
        getTranslocoModule(),
        RouterTestingModule,
        WaiterCockpitModule,
      ],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: adminUserManageStub },
        AdminCockpitService,
        UserAreaService,
        TranslocoService,
        ConfigService,
        provideMockStore({ initialState }),
      ],
    });
  }
}

// Test section

describe('AdminUserManageComponent', () => {
  let component: AdminUserManageComponent;
  let fixture: ComponentFixture<AdminUserManageComponent>;
  let store: Store<State>;
  let initialState;
  let adminCockpitService: AdminCockpitService;
  let userAreaService: UserAreaService;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  // Initializations before compiling the component

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadAdminCockpitServiceStud(adminUserManageStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(AdminUserManageComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        adminCockpitService = TestBed.inject(AdminCockpitService);
        userAreaService = TestBed.inject(UserAreaService);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should create component', fakeAsync(() => {
    expect(component).toBeTruthy();
  }));

  it('form data should correspond to dialog data', fakeAsync(() => {
    fixture.detectChanges();
    const username = el.query(By.css('.username'));
    const email = el.query(By.css('.email'));
    tick(500);
    expect(email.nativeElement.textContent).toBe('admin@mail.com');
    expect(username.nativeElement.textContent).toBe('james007');
    expect(component).toBeTruthy();
    flush();
  }));
});
