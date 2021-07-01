import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AdminCockpitService } from 'app/cockpit-area/services/admin-cockpit.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
@Component({
  selector: 'app-admin-password-reset',
  templateUrl: './admin-password-reset.component.html',
  styleUrls: ['./admin-password-reset.component.scss']
})

export class AdminPasswordResetComponent implements OnInit{

  hide = true;
  setOnGif : boolean = false;
  token: string;
  samePassword : boolean = false;
  @ViewChild('input') password: ElementRef ;
  @ViewChild('inputConfirm') passwordConfirm: ElementRef ;
  username : string = '';
  tooShortPassword : boolean = false;

  constructor(
    private route: ActivatedRoute,
    private adminCockpitService: AdminCockpitService,
    private router:Router,
    private title : Title,
    )
    {
      this.title.setTitle("Reset Password");
    }

  ngOnInit() {
    this.setOnGif = false;
    this.token= this.route.snapshot.queryParamMap.get('token');
    // this.adminCockpitService.getUsernamePasswordReset(this.token).subscribe (
    //   (result:any) => this.username = result.username);

      this.adminCockpitService.getUsernamePasswordReset(this.token).subscribe(
        (result:any) => {
          this.username = result.username;
        },
        error => {
          this.router.navigate(['/password-reset-invalidToken']);
        }
      );
  }


  // Checks whether the entered password is valid (long enough)
  checkPassword(){
    let password : string = this.password.nativeElement.value;
    let passwordConfirm : string = this.passwordConfirm.nativeElement.value;

    if(password.length < 6 || password == ''){
      this.tooShortPassword = true;
      this.samePassword = password!=passwordConfirm ? true : false;
    }
    else if(password != passwordConfirm){
      this.samePassword = password != passwordConfirm;
      this.tooShortPassword = password.length < 6 || password=='' ? true : false;

    }else if (password===passwordConfirm){
      this.adminCockpitService.changePasswordLink(this.token, password);
    }
  }

  // Changes the password if the token is still valid
  changePassword(){
      this.adminCockpitService.changePasswordLink(this.token, this.password.nativeElement.value);
  }

  // Checks whether the token is still valid
  checkToken(){
    if(this.token == null || this.username == ''){
      this.router.navigate(['/password-reset-invalidToken']);
    }
  }

}








