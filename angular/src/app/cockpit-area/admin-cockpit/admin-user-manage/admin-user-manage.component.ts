import {
  Component,
  ElementRef,
  Inject,
  OnInit,
  ViewChild,
} from '@angular/core';
import { TranslocoService } from '@ngneat/transloco';
import { SnackService } from 'app/user-area/services/snack-bar.service';
import { AuthService } from './../../../core/authentication/auth.service';

// UserListView Interface
import { UserListView } from 'app/shared/view-models/interfaces';
import { AdminCockpitService } from 'app/cockpit-area/services/admin-cockpit.service';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { AdminUserDeleteComponent } from './admin-user-delete.component';
import { Title } from '@angular/platform-browser';
import { UserAreaService } from 'app/user-area/services/user-area.service';
@Component({
  selector: 'app-admin-user-manage',
  templateUrl: './admin-user-manage.component.html',
  styleUrls: ['./admin-user-manage.component.scss'],
})
export class AdminUserManageComponent implements OnInit {
  canChangePassword = false;
  hide = true;
  user: UserListView;
  @ViewChild('passwordInput') passwordInput: ElementRef;
  currentAdmin: string;

  userForm: FormGroup;

  authAlerts: any;
  tooShortPassword: boolean = false;
  checkPassword: boolean;

  REGEXP_EMAIL =
    /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

  constructor(
    public snackBar: SnackService,
    private dialogDelete: MatDialog,
    private auth: AuthService,
    private transloco: TranslocoService,
    private adminCockpitService: AdminCockpitService,
    private dialog: MatDialogRef<AdminUserManageComponent>,
    @Inject(MAT_DIALOG_DATA) dialogData: UserListView,
    private title: Title,
    private userAreaService: UserAreaService,
  ) {
    this.user = dialogData;
    this.transloco
      .selectTranslateObject('alerts.authAlerts')
      .subscribe((content: any) => {
        this.authAlerts = content;
      });
    this.title.setTitle('Manage User');
  }

  ngOnInit(): void {
    this.userForm = new FormGroup({
      //Input Fields
      //Validators.required test whether the entries are valid
      username: new FormControl(this.user.username, [
        Validators.required,
        Validators.minLength(4),
        Validators.pattern(/^\S*$/),
      ]),
      email: new FormControl(this.user.email, [
        Validators.required,
        Validators.pattern(this.REGEXP_EMAIL),
      ]),
    });

    //to check if selected user is the logged admin
    this.auth.getUser().subscribe((currentUser) => {
      this.currentAdmin = currentUser;
    });

    //Disable button to reset password with for logged admin
    this.checkPassword = this.user.username == this.currentAdmin ? true : false;
  }

  get username(): AbstractControl {
    return this.userForm.get('username');
  }
  get email(): AbstractControl {
    return this.userForm.get('email');
  }

  // Apply changes to selected user
  applyUserChanges() {
    this.auth.getUser().subscribe((currentUser) => {
      this.currentAdmin = currentUser;
    });

    let password: string = this.passwordInput.nativeElement.value;

    if (password.length < 6 && password.length > 0) {
      this.tooShortPassword = true;
    } else {
      this.adminCockpitService.applyUserChanges(
        this.user.id,
        this.username.value,
        this.email.value,
        password,
      );
      this.dialog.close();
    }
    //If an admin changes his own username, he will log out
    if (this.currentAdmin === this.user.username) {
      if (this.user.username !== this.username.value) {
        this.userAreaService.logout();
      }
    }
  }

  //Open Dialog for User Delete
  openDeleteUser() {
    let openendDialog = this.dialogDelete.open(AdminUserDeleteComponent, {
      width: '500px',
      height: '400px',
      data: this.user,
    });

    openendDialog.afterClosed().subscribe((result: any) => {
      this.dialog.close();
    });
  }

  //disable button to delete the logged admin
  noAdminDelete() {
    this.auth.getUser().subscribe((currentUser) => {
      this.currentAdmin = currentUser;
    });
    return this.user.username === this.currentAdmin;
  }

  //send mail to selected user to reset password
  resetPassword() {
    this.adminCockpitService.resetPassword(this.user.id, this.user.email);
    this.dialog.close();
  }
}
