import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslocoService } from '@ngneat/transloco';
import { provideMockStore } from '@ngrx/store/testing';
import { AdminCockpitService } from 'app/cockpit-area/services/admin-cockpit.service';
import { ConfigService } from 'app/core/config/config.service';
import { CoreModule } from 'app/core/core.module';
import { getTranslocoModule } from 'app/transloco-testing.module';
import { config } from '../../../core/config/config';

import { AdminUserAddComponent } from './admin-user-add.component';

describe('AdminUserAddComponent', () => {
  let component: AdminUserAddComponent;
  let fixture: ComponentFixture<AdminUserAddComponent>;

  beforeEach(async () => {
    const initialState = { config };
    await TestBed.configureTestingModule({
      declarations: [AdminUserAddComponent],
      imports: [
        CoreModule,
        BrowserAnimationsModule,
        getTranslocoModule(),
        RouterTestingModule,
      ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        AdminCockpitService ,
        MatDialogRef,
        ConfigService,
        TranslocoService,
        provideMockStore({ initialState }),
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminUserAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
