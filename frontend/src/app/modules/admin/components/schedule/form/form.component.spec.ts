import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FormScheduleComponent} from './form.component';

describe('FormScheduleComponent', () => {
  let component: FormScheduleComponent;
  let fixture: ComponentFixture<FormScheduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormScheduleComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
