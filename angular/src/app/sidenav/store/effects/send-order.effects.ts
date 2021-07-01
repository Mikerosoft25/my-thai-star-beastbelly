import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, exhaustMap, map, switchMap, tap } from 'rxjs/operators';
import { SnackBarService } from '../../../core/snack-bar/snack-bar.service';
import { SidenavService } from '../../services/sidenav.service';
import * as orderActions from '../actions/order.actions';
import * as sendOrderActions from '../actions/send-order.actions';
import { TranslocoService } from '@ngneat/transloco';
import { Router } from '@angular/router';

@Injectable()
export class SendOrderEffects {
  authAlerts: any;

  sendOrders$ = createEffect(() =>
    this.actions$.pipe(
      ofType(sendOrderActions.sendOrders),
      map((tokenData) => tokenData.token),
      switchMap((token: any) => {
        return this.sidenavService.sendOrders(token).pipe(
          map((res: any) => {
            return sendOrderActions.sendOrdersSuccess();
          }),
          catchError((error) => {
            return of(sendOrderActions.sendOrdersFail({ error }));
          }),
        );
      }),
    ),
  );

  sendOrdersSuccess = createEffect(() =>
    this.actions$.pipe(
      ofType(sendOrderActions.sendOrdersSuccess),
      map(() => {
        localStorage.removeItem('bookingToken');
        this.snackBar.openSnack(
          this.authAlerts.orderSendSuccess,
          4000,
          'green',
        );
        return orderActions.clearOrders();
      }),
    ),
  );

  sendOrdersFail = createEffect(
    () =>
      this.actions$.pipe(
        ofType(sendOrderActions.sendOrdersFail),
        map((errorData) => errorData.error),
        tap((error) => {
          this.snackBar.openSnack(this.authAlerts.orderSendFail, 4000, 'red');
        }),
      ),
    { dispatch: false },
  );

  constructor(
    private actions$: Actions,
    private sidenavService: SidenavService,
    private snackBar: SnackBarService,
    private transloco: TranslocoService,
    private router: Router,
  ) {
    this.transloco
      .selectTranslateObject('alerts.authAlerts')
      .subscribe((content: any) => {
        this.authAlerts = content;
      });
  }
}
