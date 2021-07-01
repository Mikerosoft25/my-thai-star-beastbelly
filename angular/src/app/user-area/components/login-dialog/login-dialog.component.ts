import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { AbstractControl, FormArray, FormControl, FormGroup } from '@angular/forms';
import { UserAreaService } from '../../services/user-area.service';

@Component({
  selector: 'app-public-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.scss'],
})
export class LoginDialogComponent {

  constructor(private dialog: MatDialogRef<LoginDialogComponent>, private userAreaService : UserAreaService) { }

  logInSubmit(formValue: FormGroup): void {
    this.dialog.close(formValue);
  }

  signInSubmit(formValue: FormGroup): void {
    // Since there is no backend implementation for the signup below code is used to close dialog box after submission.
    // Once the implementationis done uncomment the below code and remove the existing one.
    // this.dialog.close(formValue);
    //const formControlFields = {name: 'userRoleId', control: new FormControl(null, [])};
    //formValue.addControl(formControlFields.name, formControlFields.control);
    // formValue.controls.forEach(element => {
      
    // });
    this.userAreaService.register(formValue['username'], formValue["email"], formValue["password"]);
    //this.dialog.close(formValue);
  }

  closeLoginDialog(): void {
    this.dialog.close();
  }
}
