import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookTableComponent } from './book-table/container/book-table/book-table.component';
import { InviteFriendComponent } from './book-table/container/book-table/invite-friend/invite-friend.component';
import { AdminCockpitComponent } from './cockpit-area/admin-cockpit/admin-cockpit.component';
import { AdminUserAddComponent } from './cockpit-area/admin-cockpit/admin-user-add/admin-user-add.component';
import { AdminPasswordInvalidTokenComponent } from './cockpit-area/admin-cockpit/admin-user-manage/admin-password-reset/admin-password-reset-invalidtoken.component';
import { AdminPasswordResetComponent } from './cockpit-area/admin-cockpit/admin-user-manage/admin-password-reset/admin-password-reset.component';
import { AdminPasswordResetConfirmComponent } from './cockpit-area/admin-cockpit/admin-user-manage/admin-password-reset/admin-passwort-reset-confirm.component';
import { OrderArchivedComponent } from './cockpit-area/order-archived/order-archived.component';
import { OrderCockpitComponent } from './cockpit-area/order-cockpit/order-cockpit.component';
import { OrderUserCockpitComponent } from './cockpit-area/order-cockpit/order-user/order-user-cockpit.component';
import { ReservationCockpitComponent } from './cockpit-area/reservation-cockpit/reservation-cockpit.component';
import { ReservationUserCockpitComponent } from './cockpit-area/reservation-cockpit/reservation-user-cockpit/reservation-user-cockpit.component';
import { AuthGuardService } from './core/authentication/auth-guard.service';
import { NotFoundComponent } from './core/not-found/not-found.component';
import { NotSupportedComponent } from './core/not-supported/not-supported.component';
import { EmailConfirmationsComponent } from './email-confirmations/container/email-confirmations/email-confirmations.component';
import { HomeComponent } from './home/container/home/home.component';
import { MenuComponent } from './menu/container/menu.component';

const appRoutes: Routes = [
  { path: 'restaurant', component: HomeComponent, pathMatch: 'full' },
  { path: 'menu', component: MenuComponent },
  { path: 'bookTable', component: BookTableComponent },
  { path: 'invite', component: InviteFriendComponent },
  { path: 'booking/:action/:token', component: EmailConfirmationsComponent },
  {
    path: 'orders',
    component: OrderCockpitComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'reservations',
    component: ReservationCockpitComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'reservations-user',
    component: ReservationUserCockpitComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'orders-user',
    component: OrderUserCockpitComponent,
    canActivate: [AuthGuardService],
  },

  {
    path: 'admin',
    component: AdminCockpitComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'admin/add',
    component: AdminUserAddComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'orderarchived',
    component: OrderArchivedComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'password-reset',
    component: AdminPasswordResetComponent,
    //canActivate: [AuthGuardService],
  },
  {
    path: 'password-reset-invalidToken',
    component: AdminPasswordInvalidTokenComponent,
  },
  {
    path: 'password-reset-confirm',
    component: AdminPasswordResetConfirmComponent,
  },
  {
    path: 'prediction',
    component: NotSupportedComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'clustering',
    component: NotSupportedComponent,
    canActivate: [AuthGuardService],
  },
  { path: '', redirectTo: '/restaurant', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true, relativeLinkResolution: 'legacy' }, // <-- debugging purposes only
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
