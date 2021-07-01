import { FormControl, FormGroup, Validators } from '@angular/forms';


export class BookFormControl extends FormControl {
  label: string;
  modelProperity: string;

  constructor(label: string, properity: string, value: any, validator: any){
    super(value, validator);
    this.label = label;
    this.modelProperity = properity;
  }
}

export class BookFormGroup extends FormGroup {
  constructor(bookingDate, bookingTable, bookingStatus){
    super({
      bookingDate: new BookFormControl("BookingDate", "bookingDate", bookingDate, Validators.required),
      bookingTable: new BookFormControl("BookingTable", "bookingTable", bookingTable, Validators.required),
      bookingStatus: new BookFormControl("BookingStatus", "bookingStatus", bookingStatus, Validators.required)
    });
  }
  getBookFormControls(): BookFormControl[] {
    return Object.keys(this.controls)
    .map(k => this.controls[k] as BookFormControl);
  }
}
