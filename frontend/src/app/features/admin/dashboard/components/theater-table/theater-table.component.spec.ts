import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TheaterTableComponent } from './theater-table.component';

describe('TheaterTableComponent', () => {
  let component: TheaterTableComponent;
  let fixture: ComponentFixture<TheaterTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TheaterTableComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TheaterTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
