import { Component, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AdminCockpitService } from 'app/cockpit-area/services/admin-cockpit.service';
import { Pageable } from 'app/shared/backend-models/interfaces';
import { UserInfo } from 'app/shared/backend-models/user';
import { UserRole } from 'app/shared/view-models/interfaces';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-admin-user-add',
  templateUrl: './admin-user-add.component.html',
  styleUrls: ['./admin-user-add.component.scss'],
})

export class AdminUserAddComponent implements OnInit {

  // member variable, array for user roles
  userRoles: UserRole[];
  userForm: FormGroup;

  //Regex for a valid email
  REGEXP_EMAIL =
    /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

  userInfo: UserInfo = {
    username: '',
    email: '',
    password: '',
    passwordConfirm: '',
    role: 0,
  };

  // pageable, size set to 10, ADD CONSTANT LATER
  private pageable: Pageable = {
    pageSize: 10,
    pageNumber: 0,
    sort: [],
  };

  constructor(
    private adminCockpitService: AdminCockpitService,
    private dialog: MatDialogRef<AdminUserAddComponent>,
    private title: Title,
  ) {
    this.title.setTitle("Add User");
  }


  ngOnInit(): void {
    this.getUserRoles();
    this.userForm = new FormGroup({

      //Input Fields
      username: new FormControl(this.userInfo.username, [
        Validators.required,
        Validators.minLength(4),
        Validators.pattern(/^\S*$/)
      ]),

      email: new FormControl(this.userInfo.email, [
        Validators.required,
        Validators.pattern(this.REGEXP_EMAIL),
      ]),

      role: new FormControl(this.userInfo.role),

      password: new FormControl(this.userInfo.password, [
        Validators.required,
        Validators.minLength(6)
      ]),

      passwordConfirm: new FormControl(
        this.userInfo.passwordConfirm,
        Validators.required,
      ),
    },
      {
      //Validator, to check if password and passwordConfirm are matching
      validators: (control) => {
        if (control.value.password !== control.value.passwordConfirm) {
          control.get("passwordConfirm").setErrors({ notSame: true });
        }
        return null;
      },
    });
  }

  get f() {
    return this.userForm.controls;
  }

  get username(): AbstractControl {
    return this.userForm.get('username');
  }

  get email(): AbstractControl {
    return this.userForm.get('email');
  }

  get role(): AbstractControl {
    return this.userForm.get('role');
  }

  get password(): AbstractControl {
    return this.userForm.get('password');
  }

  get passwordConfirm(): AbstractControl {
    return this.userForm.get('passwordConfirm');
  }


  // Finalizes the registration of the new user
  // Changed so that the corresponding userRoleID is now passed in this class
  addUserbyAdmin() {
    var userRoleId: number;
    const userRole: string = this.role.value;
    this.userRoles.forEach(function (value) {
      if (value.name === userRole) {
        userRoleId = value.id;
      }
    });

    this.adminCockpitService.register(
      this.username.value,
      this.email.value,
      this.password.value,
      userRoleId,
    );
    this.dialog.close();
  }

  // Method to get User-Roles
  getUserRoles() {
    this.adminCockpitService
      .getUserRoles(this.pageable)
      .subscribe((data: any) => {
        if (!data) {
          console.log('no data found in users');
        }
        {
          this.userRoles = data.content;
          console.log(data.content);
        }
      });
  }
}
