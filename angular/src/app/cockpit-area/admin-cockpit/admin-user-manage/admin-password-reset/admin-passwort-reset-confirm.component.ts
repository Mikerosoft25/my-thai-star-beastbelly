import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { TranslocoService } from '@ngneat/transloco';
import { SnackService } from 'app/user-area/services/snack-bar.service';
import { Subscription } from 'rxjs';
import { AuthService } from './../../../../core/authentication/auth.service';
import { UserListView, UserRole } from 'app/shared/view-models/interfaces';
import { UserInfo } from 'app/shared/backend-models/user';
import { AdminCockpitService } from 'app/cockpit-area/services/admin-cockpit.service';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA, } from '@angular/material/dialog';
import { async } from '@angular/core/testing';
import { getUserName } from 'app/user-area/store';
import { ActivatedRoute, Router } from '@angular/router';
import { UserAreaService } from 'app/user-area/services/user-area.service';
import { Title } from '@angular/platform-browser';



@Component({
  selector: 'app-admin-password-reset-confirm',
  template:`
  <mat-card id="card" style="text-align: center; margin: 5%; margin-left: 31%; margin-right: 30%;" >

	<mat-card-title>
		<img src="./../../../../../assets/images/reset1.gif" alt="reset-header" style="padding-top: 5%;">
	</mat-card-title>
  <br/>
  <br/>
  <br/>
  <br/>
  `,
  styleUrls: ['./admin-password-reset.component.scss']
})

export class AdminPasswordResetConfirmComponent{

  constructor(
    private route: ActivatedRoute,
    private adminCockpitService: AdminCockpitService,
    private router: Router,
    private title : Title,
    ){
      this.title.setTitle("Reset Password");
      setTimeout( () => this.router.navigate(['/']), 7000);
    }

}
