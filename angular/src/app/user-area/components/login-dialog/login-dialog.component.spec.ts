import { TestBed, async, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { CoreModule } from '../../../core/core.module';
import * as fromRoot from '../../store/reducers';
import { UserAreaModule } from '../../user-area.module';
import { LoginDialogComponent } from './login-dialog.component';
import { RouterTestingModule } from '@angular/router/testing';
import { getTranslocoModule } from '../../../transloco-testing.module';
import { UserAreaService } from 'app/user-area/services/user-area.service';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from 'app/shared/common/test-utils';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { config } from '../../../core/config/config';
import { provideMockStore } from '@ngrx/store/testing';

const mockDialogRef = {
  close: jasmine.createSpy('close'),
};

const userAreaServiceSpy = jasmine.createSpyObj('UserAreaService', [
  'register'
]);

describe('LoginDialogComponent', () => {
  let component: LoginDialogComponent;
  let fixture: ComponentFixture<LoginDialogComponent>;
  let el: DebugElement;
  let dialog: MatDialog;
  let SnackBarService: SnackBarService;

  beforeEach(async (() => {
    const initialState = { config };
    TestBed.configureTestingModule({
      declarations: [LoginDialogComponent],
      providers: [
        { provide: UserAreaService, useValue: userAreaServiceSpy },
        { provide: MatDialogRef, useValue: mockDialogRef },
        { provide: MAT_DIALOG_DATA, useValue: {} },
        provideMockStore({ initialState }),
      ],
      imports: [
        CoreModule,
        RouterTestingModule,
        getTranslocoModule(),
        BrowserAnimationsModule,
        UserAreaModule,
        EffectsModule.forRoot([]),
        StoreModule.forRoot(fromRoot.reducers, {
          runtimeChecks: {
            strictStateImmutability: true,
            strictActionImmutability: true,
          },
        }),
      ],
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(LoginDialogComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        fixture.detectChanges();
      });
  }));

  /*beforeEach(() => {
    dialog = TestBed.inject(MatDialog);
    component = dialog.open(LoginDialogComponent).componentInstance;
  });*/

  it('should create', () => {
    dialog = TestBed.inject(MatDialog);
    component = dialog.open(LoginDialogComponent).componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should close the login dialog upon clicking the "cancel" button', () => {
    const dialogRef = TestBed.inject(MatDialogRef);
    let closeButton = el.query(By.css('.cancelLogin'));
    click(closeButton);
    expect(dialogRef.close).toHaveBeenCalled();
  });

  it('should close the sign up dialog upon clicking the "cancel" button', fakeAsync(() => {
    const dialogRef = TestBed.inject(MatDialogRef);

    // change the tab to the sign up form
    const tabs = el.queryAll(By.css('.mat-tab-label'));
    click(tabs[1]);
    fixture.detectChanges();
    tick();

    let closeButton = el.query(By.css('.cancelSignUp'));
    click(closeButton);
    expect(dialogRef.close).toHaveBeenCalled();
  }));

});

