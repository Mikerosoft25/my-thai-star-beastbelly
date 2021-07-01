import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TranslocoService } from '@ngneat/transloco';
import { BookingView } from 'app/shared/view-models/interfaces';

@Component({
  selector: 'app-booking-confirmation-dialog',
  templateUrl: './booking-confirmation-dialog.component.html',
  styleUrls: ['./booking-confirmation-dialog.component.scss'],
})
export class BookingConfirmationDialogComponent implements OnInit {
  data: any;

  constructor(
    private dialog: MatDialogRef<BookingConfirmationDialogComponent>,
    private translocoService: TranslocoService,
    private router: Router,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
  ) {
    this.data = dialogData;
    console.log(this.data);
  }

  ngOnInit(): void {}

  refreshPage() {
    setTimeout(() => {
      window.location.reload();
      localStorage.setItem('sendOrder', '1');
    }, 500);
  }
}
