import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderArchivedDialogComponent } from './order-archived-dialog.component';

describe('OrderArchivedDialogComponent', () => {
  let component: OrderArchivedDialogComponent;
  let fixture: ComponentFixture<OrderArchivedDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderArchivedDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderArchivedDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
