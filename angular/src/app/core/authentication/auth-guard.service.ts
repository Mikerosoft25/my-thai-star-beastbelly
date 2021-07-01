import { Injectable, OnDestroy } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { TranslocoService } from '@ngneat/transloco';
import { Store } from '@ngrx/store';
import * as fromApp from 'app/store/reducers';
import * as fromAuth from 'app/user-area/store/selectors/auth.selectors';
import { combineLatest, from, Observable, Subscription } from 'rxjs';
import { map } from 'rxjs/operators';
import * as fromRoot from '../../store';
import { initialState } from '../config/store/reducers/config.reducer';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { AuthService } from './auth.service';

@Injectable()
export class AuthGuardService implements CanActivate, OnDestroy {
  private translocoSubscription = Subscription.EMPTY;
  constructor(
    public snackBar: SnackBarService,
    private authService: AuthService,
    private translocoService: TranslocoService,
    private router: Router,
    private store: Store<fromApp.State>,
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): Observable<boolean> {
    return combineLatest([
      this.store.select(fromAuth.getRole),
      this.store.select(fromAuth.getLogged),
    ]).pipe(
      map(([role, logged]) => {
        if (
          (state.url === '/orders' ||
            state.url === '/orderarchived' ||
            state.url === '/reservations' ||
            state.url === '/prediction' ||
            state.url === '/clustering') &&
          role === 'MANAGER' &&
          logged
        ) {
          return true;
        }

        if (
          (state.url === '/orders' ||
            state.url === '/reservations' ||
            state.url === '/orderarchived') &&
          role === 'WAITER' &&
          logged
        ) {
          return true;
        }

        if (
          (state.url === '/reservations-user' ||
            state.url === '/orders-user') &&
          role === 'CUSTOMER' &&
          logged
        ) {
          return true;
        }

        if (
          (state.url === '/admin/add' ||
            state.url === '/orderarchived' ||
            state.url == '/admin') &&
          role === 'ADMIN' &&
          logged
        ) {
          return true;
        }

        // if (
        //   (state.url === '/admin/manage' || state.url === '/admin/add') &&
        //   role === 'ADMIN' &&
        //   logged
        // ) {
        //   return true;
        // }

        if (!logged && !localStorage.getItem('stayLogged')) {
          this.translocoSubscription = this.translocoService
            .selectTranslate('alerts.accessError')
            .subscribe((alert) => this.snackBar.openSnack(alert, 4000, 'red'));
        }

        if (this.router.url === '/') {
          fromRoot.go({ path: ['/restaurant'] });
        }

        return false;
      }),
    );
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }
}
