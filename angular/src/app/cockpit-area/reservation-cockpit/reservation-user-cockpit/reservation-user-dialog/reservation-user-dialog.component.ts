import { Component, OnInit, Inject } from '@angular/core';
import {
  FriendsInvite,
  ReservationView,
} from '../../../../shared/view-models/interfaces';
import { MAT_DIALOG_DATA , MatDialogRef } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { WaiterCockpitService } from 'app/cockpit-area/services/waiter-cockpit.service';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { ReservationUserCockpitComponent } from '../reservation-user-cockpit.component';

@Component({
  selector: 'app-cockpit-reservation-user-dialog',
  templateUrl: './reservation-user-dialog.component.html',
  styleUrls: ['./reservation-user-dialog.component.scss'],
})

export class ReservationUserDialogComponent implements OnInit {
  datao: FriendsInvite[] = [];
  fromRow = 0;
  currentPage = 1;
  pageSize = 4;

  authAlerts: any;

  data: any;
  columnso: any[] = [
    { name: 'email', label: 'Guest email' },
    { name: 'accepted', label: 'Acceptances and declines' },
  ];
  displayedColumnsO: string[] = ['email', 'accepted'];
  pageSizes: number[];
  datat: ReservationView[] = [];
  columnst: any[];
  displayedColumnsT: string[] = [
    'bookingDate',
    'creationDate',
    //'name',
    //'email',
    'tableId',
  ];

  filteredData: any[] = this.datao;

  constructor(
    public snackBar: SnackBarService,
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    private dialog: MatDialogRef<ReservationUserCockpitComponent>,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
  ) {
    this.data = dialogData;
    this.pageSizes = configService.getValues().pageSizesDialog;
    this.translocoService
    .selectTranslateObject('alerts.authAlerts')
    .subscribe((content: any) => {
      this.authAlerts = content;
    });
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });

    this.datat.push(this.data);
    this.datao = this.data.invitedGuests;
    this.filter();
  }

  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitReservationTable) => {
        this.columnst = [
          {
            name: 'booking.bookingDate',
            label: cockpitReservationTable.reservationDateH,
          },
          {
            name: 'booking.creationDate',
            label: cockpitReservationTable.creationDateH,
          },
          //{ name: 'booking.name', label: cockpitReservationTable.ownerH },
          //{ name: 'booking.email', label: cockpitReservationTable.emailH },
          { name: 'booking.tableId', label: cockpitReservationTable.tableH },
        ];
      });

    this.translocoService
      .selectTranslateObject('cockpit.reservations.dialogTable', {}, lang)
      .subscribe((cockpitReservationDialogTable) => {
        this.columnso = [
          { name: 'email', label: cockpitReservationDialogTable.guestEmailH },
          {
            name: 'accepted',
            label: cockpitReservationDialogTable.acceptanceH,
          },
        ];

        if (this.data.booking.assistants) {
          this.columnst.push({
            name: 'booking.assistants',
            label: cockpitReservationDialogTable.assistantsH,
          });
          this.displayedColumnsT.push('assistants');
        }
      });
  }

  page(pagingEvent: PageEvent): void {
    this.fromRow = pagingEvent.pageSize * pagingEvent.pageIndex;
    this.currentPage = pagingEvent.pageIndex + 1;
    this.pageSize = pagingEvent.pageSize;
    this.filter();
  }

  filter(): void {
    let newData: any[] = this.datao;
    newData = newData.slice(this.fromRow, this.currentPage * this.pageSize);
    setTimeout(() => (this.filteredData = newData));
  }

  cancelReservationByUser() {
    this.waiterCockpitService
      .cancelReservationByUser(this.datat[0])
      .subscribe(
        (data: any)=> {
          this.datat[0] = data;
          this.snackBar.openSnack(this.authAlerts.reservationDeleteSuccess, 4000, 'green');
          this.dialog.close();
        },
        ()=>{
          this.snackBar.openSnack(this.authAlerts.reservationDeleteFail, 4000, 'red');
        }
      );
  }
}
