import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { BookingInfo } from 'app/shared/backend-models/interfaces';
import { UserListView } from 'app/shared/view-models/interfaces';
import { map } from 'lodash';
import { Observable } from 'rxjs';
import { exhaustMap, subscribeOn } from 'rxjs/operators';
import { ConfigService } from '../../core/config/config.service';
import { Booking } from '../models/booking.model';

@Injectable()
export class BookTableService {
  private readonly booktableRestPath: string = 'bookingmanagement/v1/booking';

  private readonly restServiceRoot$: Observable<
    string
  > = this.config.getRestServiceRoot();


  private readonly getBookingByGuestTokenPath: string =
    'bookingmanagement/v1/bookingbyguesttoken/';

  private readonly invitedGuestAcceptPath: string =
    'bookingmanagement/v1/invitedguest/accept/';

  private readonly invitedGuestDeclinePath: string =
    'bookingmanagement/v1/invitedguest/decline/';

  private readonly getUserDetailsPath: string =
    'usermanagement/v1/userdetails';


  constructor(private http: HttpClient, private config: ConfigService) {}

  postBooking(bookInfo: BookingInfo): Observable<any> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post(`${restServiceRoot}${this.booktableRestPath}`, bookInfo),
      )
    );

  }

  getBookingByGuestToken(
    token : string
    ) : Observable<any> {
      return this.restServiceRoot$
      .pipe(
        exhaustMap((restServiceRoot) =>
          this.http.get<any>(`${restServiceRoot}${this.getBookingByGuestTokenPath}${token}`),
        ),
      )
  }

  invitedGuestDecline(
    token : string
  ) : Observable<any>{
    return this.restServiceRoot$
      .pipe(
        exhaustMap((restServiceRoot) =>
          this.http.get<any>(`${restServiceRoot}${this.invitedGuestDeclinePath}${token}`),
        ),
      )
  }

  invitedGuestAccept(
    token : string
  ){
    return this.restServiceRoot$
      .pipe(
        exhaustMap((restServiceRoot) =>
          this.http.get(`${restServiceRoot}${this.invitedGuestAcceptPath}${token}`),
        ),
      )
  }

  getUserDetails(
  ): Observable<UserListView>{
    return this.restServiceRoot$
      .pipe(
        exhaustMap((restServiceRoot) =>
          this.http.get<UserListView>(`${restServiceRoot}${this.getUserDetailsPath}`),
        ),
      )
  }

  composeBooking(invitationData: any, type: number): Booking {
    const composedBooking: Booking = {
      booking: {
        bookingDate: invitationData.bookingDate,
        name: invitationData.name,
        email: invitationData.email,
        bookingType: type,
      },
    };

    if (type) {
      composedBooking.invitedGuests = map(
        invitationData.invitedGuests,
        (email: string) => ({ email }),
      );
    } else {
      composedBooking.booking.assistants = invitationData.assistants;
    }

    return composedBooking;
  }
}
