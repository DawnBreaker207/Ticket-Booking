import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FormShowtimeComponent} from './form.component';

describe('FormComponent', () => {
  let component: FormShowtimeComponent;
  let fixture: ComponentFixture<FormShowtimeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormShowtimeComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormShowtimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
