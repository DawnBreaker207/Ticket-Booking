import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormMovieAPIComponent } from './api-form.component';

describe('FormMovieAPIComponent', () => {
  let component: FormMovieAPIComponent;
  let fixture: ComponentFixture<FormMovieAPIComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormMovieAPIComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormMovieAPIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
