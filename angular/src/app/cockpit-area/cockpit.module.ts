import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '../core/core.module';

import { WaiterCockpitService } from './services/waiter-cockpit.service';
import { WindowService } from '../core/window/window.service';
import { PredictionService } from './services/prediction.service';
import { ClusteringService } from './services/clustering.service';

import { ReservationCockpitComponent } from './reservation-cockpit/reservation-cockpit.component';
import { OrderCockpitComponent } from './order-cockpit/order-cockpit.component';
import { OrderDialogComponent } from './order-cockpit/order-dialog/order-dialog.component';
import { ReservationDialogComponent } from './reservation-cockpit/reservation-dialog/reservation-dialog.component';
import { HttpClientModule } from '@angular/common/http';
import { PredictionCockpitComponent } from './prediction-cockpit/prediction-cockpit.component';
import { ClusteringCockpitComponent } from './clustering-cockpit/clustering-cockpit.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslocoRootModule } from '../transloco-root.module';
import { OrderAddComponent } from './order-cockpit/order-dialog/order-add/order-add.component';
import { AdminCockpitComponent } from './admin-cockpit/admin-cockpit.component';
import { AdminUserManageComponent } from './admin-cockpit/admin-user-manage/admin-user-manage.component';
import { AdminUserAddComponent } from './admin-cockpit/admin-user-add/admin-user-add.component';
import { AdminUserDeleteComponent } from './admin-cockpit/admin-user-manage/admin-user-delete.component';

import {
  InlineEditBookingDate,
  InlineEditBookingTable,
} from './order-cockpit/order-dialog/inline-edit/inline-edit.component';
import { CdkTableModule } from '@angular/cdk/table';
import { OrderArchivedComponent } from './order-archived/order-archived.component';
import { OrderArchivedDialogComponent } from './order-archived/order-archived-dialog/order-archived-dialog.component';
import { AdminPasswordResetComponent } from './admin-cockpit/admin-user-manage/admin-password-reset/admin-password-reset.component';
import { AdminPasswordResetConfirmComponent } from './admin-cockpit/admin-user-manage/admin-password-reset/admin-passwort-reset-confirm.component';
import { AdminPasswordInvalidTokenComponent } from './admin-cockpit/admin-user-manage/admin-password-reset/admin-password-reset-invalidtoken.component';
import { ReservationUserCockpitComponent } from './reservation-cockpit/reservation-user-cockpit/reservation-user-cockpit.component';
import { ReservationUserDialogComponent } from './reservation-cockpit/reservation-user-cockpit/reservation-user-dialog/reservation-user-dialog.component';
import { OrderUserCockpitComponent } from './order-cockpit/order-user/order-user-cockpit.component';
import { OrderUserDialogComponent } from './order-cockpit/order-user/order-user-dialog/order-user-dialog.component';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    TranslocoRootModule,
    CoreModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    WaiterCockpitService,
    WindowService,
    PredictionService,
    ClusteringService,
  ],
  declarations: [
    ReservationCockpitComponent,
    OrderCockpitComponent,
    ReservationDialogComponent,
    ReservationUserCockpitComponent,
    ReservationUserDialogComponent,
    OrderDialogComponent,
    PredictionCockpitComponent,
    ClusteringCockpitComponent,
    OrderAddComponent,
    OrderUserCockpitComponent,
    OrderUserDialogComponent,
    AdminCockpitComponent,
    AdminUserManageComponent,
    AdminUserAddComponent,
    AdminPasswordResetComponent,
    AdminPasswordResetConfirmComponent,
    AdminPasswordInvalidTokenComponent,
    AdminUserDeleteComponent,
    InlineEditBookingDate,
    InlineEditBookingTable,
    OrderArchivedComponent,
    OrderArchivedDialogComponent,
  ],
  exports: [
    ReservationCockpitComponent,
    OrderCockpitComponent,
    PredictionCockpitComponent,
    ClusteringCockpitComponent,
    CdkTableModule,
  ],
  entryComponents: [
    ReservationDialogComponent,
    OrderDialogComponent,
    PredictionCockpitComponent,
    ClusteringCockpitComponent,
  ],
})
export class WaiterCockpitModule {}
