import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { FilterCheckboxesComponent } from './filter-checkboxes.component';
import { CoreModule } from '../../../../core/core.module';
import { getTranslocoModule } from '../../../../transloco-testing.module';

describe('FilterCheckboxesComponent', () => {
  let component: FilterCheckboxesComponent;
  let fixture: ComponentFixture<FilterCheckboxesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FilterCheckboxesComponent],
      imports: [CoreModule, getTranslocoModule()],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilterCheckboxesComponent);
    component = fixture.componentInstance;
    component.categoriesValue = {
      drinks: true,
      mainDishes: true,
      starters: true,
      desserts: true,
      noodle: true,
      rice: true,
      curry: true,
      vegan: true,
      vegetarian: true,
      favourites: true,
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
