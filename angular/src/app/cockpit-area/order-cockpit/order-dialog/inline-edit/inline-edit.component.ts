import { Component, Input, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

// Inline edit for booking date column in order-cockpit-component
@Component({
  selector: 'inline-edit-bookingDate',
  styleUrls: ['inline-edit.component.scss'],
  template: `
    <form (ngSubmit)="onSubmit()">
      <div class="mat-subheading-2">Add a Date</div>

      <mat-form-field>
        <input
          style="width: calc(100% - 30px);"
          matInput
          [placeholder]=""
          name="bookingDate"
          [(ngModel)]="bookingDate"
          [owlDateTimeTrigger]="booking1"
          [owlDateTime]="booking1"
        />
        <span [owlDateTimeTrigger]="booking1"
          ><i class="fa fa-calendar"></i
        ></span>
        <owl-date-time [pickerMode]="'dialog'" #booking1></owl-date-time>
        <mat-icon>calendar_today</mat-icon>
      </mat-form-field>

      <div class="actions">
        <button
          mat-button
          mat-dialog-close
          type="button"
          color="primary"
          (click)="onCancel()"
        >
          CANCEL
        </button>
        <button mat-button type="submit" color="primary">SAVE</button>
      </div>
    </form>
  `,
})
export class InlineEditBookingDate {
  // Options for owl-datetime
  options: any = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    second: 'numeric',
  };

  // Getter and Setter for booking date selection
  @Input()
  get value(): string {
    return this._value;
  }
  set value(x: string) {
    this.bookingDate = this._value = x;
  }

  private _value = '';

  // Form model for the input
  bookingDate = '';

  constructor(private dialog: MatDialogRef<InlineEditBookingDate>) {}

  ngOnInit(): void {}
  onSubmit() {
    // thereby it isn't possible to handover an empty variable
    if (this.bookingDate != '') {
      this.dialog.close(this.bookingDate);
    } else {
      this.dialog.close();
    }
  }
  onCancel() {
    this.dialog.close();
  }
}

// Inline edit for table id column in order-cockpit component
@Component({
  selector: 'inline-edit-bookingTable',
  styleUrls: ['inline-edit.component.scss'],
  template: `
    <form (ngSubmit)="onSubmit()">
      <div class="mat-subheading-2">Choose a Table</div>

      <mat-form-field appearance="fill">
        <mat-select name="bookingTable" [(ngModel)]="bookingTable">
          <mat-option *ngFor="let table of data" [value]="table.id">
            {{ table.id }}
          </mat-option>
        </mat-select>
      </mat-form-field>

      <div class="actions">
        <button
          mat-button
          mat-dialog-close
          type="button"
          color="primary"
          (click)="onCancel()"
        >
          CANCEL
        </button>
        <button mat-button type="submit" color="primary">SAVE</button>
      </div>
    </form>
  `,
})
export class InlineEditBookingTable {
  // Getter and Setter for table id selection
  @Input()
  get value(): string {
    return this._value;
  }
  set value(x: string) {
    this.bookingTable = this._value = x;
  }

  private _value = '';
  data;

  // Form model for the input
  bookingTable = '';

  constructor(
    private dialog: MatDialogRef<InlineEditBookingDate>,
    @Inject(MAT_DIALOG_DATA) data,
  ) {
    this.data = data;
  }

  ngOnInit(): void {}

  // thereby it isn't possible to handover an empty variable
  onSubmit() {
    if (this.bookingTable != '') {
      this.dialog.close(this.bookingTable);
    } else {
      this.dialog.close();
    }
  }
  onCancel() {
    this.dialog.close();
  }
}
