import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormTheaterComponent } from './form.component';

describe('FormTheaterComponent', () => {
  let component: FormTheaterComponent;
  let fixture: ComponentFixture<FormTheaterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormTheaterComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormTheaterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
