import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookTableService } from 'app/book-table/services/book-table.service';
import { StringifyOptions } from 'querystring';
import {MatIconModule} from '@angular/material/icon';
import { AdminCockpitService } from 'app/cockpit-area/services/admin-cockpit.service';
import * as moment from 'moment';
import { count } from 'rxjs/operators';
import { typeSourceSpan } from '@angular/compiler';
import { throwMatDuplicatedDrawerError } from '@angular/material/sidenav';

@Component({
  selector: 'app-invite-friend',
  templateUrl: './invite-friend.component.html',
  styleUrls: ['./invite-friend.component.scss']
})

export class InviteFriendComponent implements OnInit{

  token: string;
  name: string;
  email: string;
  assistants: string;
  bookingDate: string;
  expirationDate: string;
  countdown: string;
  showAgree = false;
  showDecline = false;
  showBasic = true;
  showFailure = false;
  showFailureTime = false;


  constructor(
    private route: ActivatedRoute,
    private bookTableService: BookTableService,
    private router: Router,
    )
    {
    }

  ngOnInit() {
    //get the Token of the url
    this.token= this.route.snapshot.queryParamMap.get('token');

    //get details of the booking
    this.bookTableService.getBookingByGuestToken(this.token).subscribe(
      (bookingInfo : any) => {
        this.name = bookingInfo.name;
        this.email = bookingInfo.email;
        this.assistants = bookingInfo.assistants;
        this.bookingDate = bookingInfo.bookingDate;
        this.expirationDate = bookingInfo.expirationDate;

        let time = new Date(moment.unix(+this.expirationDate).format('lll')).getTime();

        //create countdown for the remaining time to accept this invite
        let x = setInterval(()=>{
          var now = new Date().getTime();
          var distance = time - now;
          var days = Math.floor(distance/(1000*60*60*24));
          var hours = Math.floor((distance% (1000*60*60*24)) / (1000*60*60))
          var minutes = Math.floor((distance % (1000*60*60))/ (1000*60));
          var seconds = Math.floor((distance % (1000*60)) / 1000);
          this.countdown = days + "d " + hours + "h " + minutes + "m " + seconds + "s ";

          //if the time is up
          if(seconds < 0){
            this.showBasic = false;
            this.showFailureTime = true;
          }
        })
      },
      ()=> {
        //if the token is invalid
        this.showBasic = false;
        this.showFailure = true;
        setTimeout(()=> this.router.navigate(['/']), 8000);
      }
    );
  }

  //if the user press the button to accept the invite
  accept(){
    this.bookTableService.invitedGuestAccept(this.token).subscribe(
      ()=>{
        //show the agreed-Screen, disable the invite-screen
        this.showBasic = false;
        this.showAgree = true;
        setTimeout(()=> this.router.navigate(['/']), 8000);
      },
      ()=>{
        //show the declined-Screen, disable the invite-screen
        this.showBasic = false;
        this.showFailure = true;
        setTimeout(()=> this.router.navigate(['/']), 4000);
      }
    );
  }

  //if the user press the button to decline the invite
  decline(){
    this.bookTableService.invitedGuestDecline(this.token).subscribe(
      	() => {
          this.showBasic = false;
          this.showDecline = true;

          setTimeout(()=> this.router.navigate(['/']), 4000);
        },
        () => {
          this.showBasic = false;
          this.showFailure = true;
          setTimeout(()=> this.router.navigate(['/']), 4000);

        }
    );
  }
}

