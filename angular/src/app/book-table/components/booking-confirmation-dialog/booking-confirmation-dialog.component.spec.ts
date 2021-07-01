import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingConfirmationDialogComponent } from './booking-confirmation-dialog.component';
import { DebugElement } from '@angular/core';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { CoreModule } from 'app/core/core.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getTranslocoModule } from 'app/transloco-testing.module';
import { BookTableDialogComponentStub } from '../../../../in-memory-test-data/db-book';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';

const mockDialogRef = {
  close: jasmine.createSpy('close'),
};

describe('Component: Booking Confirmation Dialog', () => {
  // Access to HTML
  let fixture: ComponentFixture<BookingConfirmationDialogComponent>;
  // Access to the component itself
  let component: BookingConfirmationDialogComponent;
  // For testing the HTML related to the Tests#
  let de: DebugElement;
  // The dialog
  let dialog: MatDialog;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BookingConfirmationDialogComponent],
      imports: [CoreModule, BrowserAnimationsModule, getTranslocoModule(), RouterTestingModule],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: mockDialogRef },
      ],
    });

    fixture = TestBed.createComponent(BookingConfirmationDialogComponent);
    component = fixture.componentInstance;
    de = fixture.debugElement;
  });

  it('should create', () => {
    dialog = TestBed.inject(MatDialog);
    component = dialog.open(
      BookingConfirmationDialogComponent,
    ).componentInstance;
    component.data = BookTableDialogComponentStub.data;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should verify dialog name property value', () => {
    component.data = BookTableDialogComponentStub.data;
    fixture.detectChanges();
    expect(component).toBeTruthy();
    const email = de.queryAll(By.css('.emailValue'));
    expect(email[0].nativeElement.textContent).toBe('test@gmail.com');
  });

  it('should close the dialog upon clicking the "close" button', () => {
    const dialogRef = TestBed.inject(MatDialogRef);
    let closeButton = de.query(By.css('button')).nativeElement;
    closeButton.click();
    expect(dialogRef.close).toHaveBeenCalled();
  });

});
