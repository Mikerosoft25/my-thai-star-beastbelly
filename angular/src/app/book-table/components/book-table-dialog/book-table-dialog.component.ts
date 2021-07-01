import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import * as moment from 'moment';
import * as fromApp from '../../../store/reducers';
import * as bookTableActions from '../../store/actions/book-table.actions';
import { BookingInfo } from '../../../shared/backend-models/interfaces';
import { BookingConfirmationDialogComponent } from '../booking-confirmation-dialog/booking-confirmation-dialog.component';
import { BookTableService } from '../../services/book-table.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-public-book-table-dialog',
  templateUrl: './book-table-dialog.component.html',
  styleUrls: ['./book-table-dialog.component.scss'],
})
export class BookTableDialogComponent implements OnInit {
  data: any;
  date: string;

  constructor(
    private store: Store<fromApp.State>,
    private dialog: MatDialogRef<BookTableDialogComponent>,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private router: Router,
  ) {
    this.data = dialogData;
  }

  ngOnInit(): void {
    this.date = moment(this.data.bookingDate).format('LLL');
  }

  sendBooking(): void {
    this.store.dispatch(bookTableActions.bookTable({ booking: this.data }));
    this.dialog.close();
  }
}
