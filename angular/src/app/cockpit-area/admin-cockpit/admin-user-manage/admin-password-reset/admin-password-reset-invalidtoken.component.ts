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
  selector: 'app-admin-password-reset-invalidtoken',
  template:`
  <mat-card id="card" style="text-align: center; margin: 5%; margin-left: 31%; margin-right: 30%;" >

	<mat-card-title>
		<img src="./../../../../../assets/images/reset-header-fail.png" alt="reset-header-fail" style="padding-top: 5%;">
	</mat-card-title>
  <mat-card-content >
    <p style="text-align: left; margin-top: 4%; margin-left: 8%; margin-right: 10%; font-size: large;">
      <b style="font-size: x-large;">
        {{'admin.resetPassword.invalidToken.text1' | transloco}}
      </b><br/><br/>

      {{'admin.resetPassword.invalidToken.text2' | transloco}}<br/><br/>
      <b>{{'admin.resetPassword.invalidToken.hint' | transloco}}</b>: {{'admin.resetPassword.invalidToken.hintText' | transloco}}<br/><br/><br/>

    </p>
    <span style="text-align: center; font-size: medium"><a href="/" >{{'admin.resetPassword.invalidToken.redirectText1' | transloco}} {{counter.sec}} {{'admin.resetPassword.invalidToken.redirectText2' | transloco}}</a></span>
  </mat-card-content>

  <br/>
  <br/>

  `,
  styleUrls: ['./admin-password-reset.component.scss']
})

export class AdminPasswordInvalidTokenComponent implements OnInit{

  counter: { min: number, sec: number }

  constructor(
    private route: ActivatedRoute,
    private adminCockpitService: AdminCockpitService,
    private router: Router,
    private title : Title,
    ){
      this.title.setTitle("Reset Password");
    }

  ngOnInit(): void {
    this.startTimer();
    setTimeout(()=>this.router.navigate(['/']), 29500);

  }

  startTimer() {
    this.counter = { min: 0, sec: 30 }
    let intervalId = setInterval(() => {
      if (this.counter.sec - 1 == -1) {
        this.counter.min -= 1;
        this.counter.sec = 59
      }
      else this.counter.sec -= 1
      if (this.counter.min === 0 && this.counter.sec == 0) clearInterval(intervalId)
    }, 1000)
  }
}
