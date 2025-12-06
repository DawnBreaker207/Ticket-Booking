import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TheaterModalComponent } from './theater.component';

describe('TheaterModalComponent', () => {
  let component: TheaterModalComponent;
  let fixture: ComponentFixture<TheaterModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TheaterModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TheaterModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
