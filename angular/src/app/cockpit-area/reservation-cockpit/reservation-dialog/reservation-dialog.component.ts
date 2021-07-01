import { Component, OnInit, Inject } from '@angular/core';
import {
  FriendsInvite,
  ReservationView,
} from '../../../shared/view-models/interfaces';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { WaiterCockpitService } from 'app/cockpit-area/services/waiter-cockpit.service';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { ReservationCockpitComponent } from '../reservation-cockpit.component';

@Component({
  selector: 'app-cockpit-reservation-dialog',
  templateUrl: './reservation-dialog.component.html',
  styleUrls: ['./reservation-dialog.component.scss'],
})
export class ReservationDialogComponent implements OnInit {
  // Declaration of Arrays to import data from post requests
  datao: FriendsInvite[] = [];
  data: any;
  datat: ReservationView[] = [];
  authAlerts: any;

  // Table
  columnso: any[] = [
    { name: 'email', label: 'Guest email' },
    { name: 'accepted', label: 'Acceptances and declines' },
  ];
  displayedColumnsO: string[] = ['email', 'accepted'];
  columnst: any[];
  displayedColumnsT: string[] = [
    'bookingDate',
    'creationDate',
    'name',
    'email',
    'tableId',
  ];

  // Pageable, Sorting and filters for the requests
  pageSizes: number[];
  filteredData: any[] = this.datao;
  fromRow = 0;
  currentPage = 1;
  pageSize = 4;

  constructor(
    public snackBar: SnackBarService,
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    public transloco: TranslocoService,
    private dialog: MatDialogRef<ReservationCockpitComponent>,
    private configService: ConfigService,
  ) {
    this.data = dialogData;
    this.pageSizes = configService.getValues().pageSizesDialog;
    this.transloco
    .selectTranslateObject('alerts.authAlerts')
    .subscribe((content: any) => {
      this.authAlerts = content;
    });
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });
    // Foremost retrieve data by post request to getting data by default sorting, filter and amount
    this.datat.push(this.data);
    this.datao = this.data.invitedGuests;
    this.filter();
  }

  // Get translations according to the browser language
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
          { name: 'booking.name', label: cockpitReservationTable.ownerH },
          { name: 'booking.email', label: cockpitReservationTable.emailH },
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

  // Pagination in dependence of the user selection
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

  cancelReservation() {
    this.waiterCockpitService
      .cancelReservation(this.datat[0])
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
