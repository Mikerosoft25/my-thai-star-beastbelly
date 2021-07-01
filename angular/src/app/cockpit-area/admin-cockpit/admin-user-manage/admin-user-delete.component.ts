import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';

import { Subscription } from 'rxjs';
import { AuthService } from './../../../core/authentication/auth.service';

// UserListView Interface
import { UserListView, UserRole } from 'app/shared/view-models/interfaces';
import { AdminCockpitService } from 'app/cockpit-area/services/admin-cockpit.service';

import { MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA, } from '@angular/material/dialog';


@Component({
  selector: 'app-admin-user-delete',
  template:
  `<h3 mat-dialog-title>
      {{'admin.usermanage.deleteUser.title' | transloco}}: {{this.user.username}}
  </h3>
  <mat-divider></mat-divider>

  <div class="center"><img src="./../../../../assets/images/deleteuser.png" alt="Lösche Nutzer"></div>

  <div class="center">{{'admin.usermanage.deleteUser.areYouSure' | transloco}} <br><br> {{'admin.usermanage.deleteUser.deleteQuestion' | transloco}}</div>
  <br>
  <div class="center">
    <button mat-raised-button (click)="close()">{{ 'admin.usermanage.deleteUser.disagree' | transloco }}</button>
    &nbsp;&nbsp;&nbsp;
    <button mat-raised-button color="warn" (click)="accept()">{{ 'admin.usermanage.deleteUser.agree' | transloco }}</button>
  </div>
  `,
  styleUrls: ['./admin-user-manage.component.scss']
})

export class AdminUserDeleteComponent{

  user: UserListView;

  constructor(
    public auth: AuthService,
    private adminCockpitService: AdminCockpitService,
    private dialog: MatDialogRef<AdminUserDeleteComponent>,
    @Inject(MAT_DIALOG_DATA) dialogData: UserListView,
    ) {
      this.user = dialogData;
    }

  close(){
    this.dialog.close();
  }

  accept(){
    this.adminCockpitService.deleteUser(this.user.id);
    this.dialog.close();
  }


}
