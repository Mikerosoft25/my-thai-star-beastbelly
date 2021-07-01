import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TranslocoService } from '@ngneat/transloco';
import { AuthService } from 'app/core/authentication/auth.service';
import { ConfigService } from 'app/core/config/config.service';
import { FilterAdmin } from 'app/shared/backend-models/filters';
import {
  Pageable,
  Sort,
  FilterCockpit,
} from 'app/shared/backend-models/interfaces';
import {
  OrderResponse,
  UserListView,
  UserResponse,
  UserRolesResponse,
} from 'app/shared/view-models/interfaces';
import { SnackService } from 'app/user-area/services/snack-bar.service';
import { EventEmitter } from 'events';
import { Observable } from 'rxjs';
import { exhaustMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
@Injectable()
export class AdminCockpitService {
  private readonly getUsersRestPath: string = 'usermanagement/v1/user/search';
  private readonly filterUsersRestPath: string =
    'usermanagement/v1/user/search';

  // path to get the roles of users
  private readonly getUserRolesRestPath: string =
    'usermanagement/v1/userrole/search';

  private readonly restServiceRoot$: Observable<string> =
    this.config.getRestServiceRoot();
  private readonly registerRestPath: string =
    'usermanagement/v1/user/registerbyadmin';

  private readonly applyUserChangesRestPath: string =
    'usermanagement/v1/user/changeuser';

  private readonly deleteUserRestPath: string = 'usermanagement/v1/user/';

  private readonly resetPasswordPath: string =
    'usermanagement/v1/user/resetPassword';

  private readonly changePasswordPath: string =
    'usermanagement/v1/user/changePassword';

  private readonly getUsernamePasswordResetPath: string =
    'usermanagement/v1/user/resettoken/';

  authAlerts: any;

  constructor(
    private config: ConfigService,
    public snackBar: SnackService,
    public transloco: TranslocoService,
    private http: HttpClient,
    public authService: AuthService,
    private router: Router,
  ) {
    this.transloco
      .selectTranslateObject('alerts.authAlerts')
      .subscribe((content: any) => {
        this.authAlerts = content;
      });
  }

  getUsers(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterAdmin,
  ): Observable<UserResponse[]> {
    let path: string;
    filters.pageable = pageable;
    filters.pageable.sort = sorting;
    if (
      filters.username ||
      filters.email ||
      filters.date ||
      filters.userRoleId
    ) {
      path = this.filterUsersRestPath;
    } else {
      delete filters.username;
      delete filters.email;
      delete filters.date;
      path = this.getUsersRestPath;
    }
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<UserResponse[]>(`${restServiceRoot}${path}`, filters),
      ),
    );
  }

  // added roleID, please remove if something goes wrong
  register(
    username: string,
    email: string,
    password: string,
    userRoleId: number,
  ): void {
    this.restServiceRoot$
      .pipe(
        exhaustMap((restServiceRoot) =>
          this.http.post(`${restServiceRoot}${this.registerRestPath}`, {
            username,
            email,
            password,
            userRoleId,
          }),
        ),
      )
      .subscribe(
        () => {
          this.snackBar.success(this.authAlerts.addUserSuccess);
        },
        () => {
          this.snackBar.fail(this.authAlerts.addUserFail);
        },
      );
  }

  applyUserChanges(
    id: number,
    username: string,
    email: string,
    password: string,
  ): void {
    if (password == '') {
      this.restServiceRoot$
        .pipe(
          exhaustMap((restServiceRoot) =>
            this.http.post(
              `${restServiceRoot}${this.applyUserChangesRestPath}`,
              {
                id,
                username,
                email,
              },
            ),
          ),
        )
        .subscribe(
          () => {
            this.snackBar.success(this.authAlerts.changeSuccess);
          },
          () => {
            this.snackBar.fail(this.authAlerts.changeFail);
          },
        );
    } else {
      this.restServiceRoot$
        .pipe(
          exhaustMap((restServiceRoot) =>
            this.http.post(
              `${restServiceRoot}${this.applyUserChangesRestPath}`,
              {
                id,
                username,
                email,
                password,
              },
            ),
          ),
        )
        .subscribe(
          () => {
            this.snackBar.success(this.authAlerts.changeSuccess);
          },
          () => {
            this.snackBar.fail(this.authAlerts.changeFail);
          },
        );
    }
  }

  deleteUser(id: number): void {
    this.restServiceRoot$
      .pipe(
        exhaustMap((restServiceRoot) =>
          this.http.delete(`${restServiceRoot}${this.deleteUserRestPath}${id}`),
        ),
      )
      .subscribe(
        () => {
          this.snackBar.success(this.authAlerts.deleteSuccess);
        },
        () => {
          this.snackBar.fail(this.authAlerts.deleteFail);
        },
      );
  }

  resetPassword(id: number, mail: string): void {
    this.restServiceRoot$
      .pipe(
        exhaustMap((restServiceRoot) =>
          this.http.post(`${restServiceRoot}${this.resetPasswordPath}`, { id }),
        ),
      )
      .subscribe(
        () => {
          this.snackBar.success(this.authAlerts.resetSuccess + `: ${mail}`);
        },
        () => {
          this.snackBar.fail(this.authAlerts.resetFail);
        },
      );
  }

  changePasswordLink(token: string, password: string): void {
    this.restServiceRoot$
      .pipe(
        exhaustMap((restServiceRoot) =>
          this.http.post(`${restServiceRoot}${this.changePasswordPath}`, {
            token,
            password,
          }),
        ),
      )
      .subscribe(
        () => {
          this.router.navigate(['/password-reset-confirm']);
        },
        () => {
          this.router.navigate(['/password-reset-invalidToken']);
        },
      );
  }

  getUserRoles(pageable: Pageable): Observable<UserRolesResponse> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<UserRolesResponse>(
          `${restServiceRoot}${this.getUserRolesRestPath}`,
          { pageable: pageable },
        ),
      ),
    );
  }

  getUsernamePasswordReset(token: string): Observable<UserResponse> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.get<UserResponse>(
          `${restServiceRoot}${this.getUsernamePasswordResetPath}${token}`,
        ),
      ),
    );
  }
}
