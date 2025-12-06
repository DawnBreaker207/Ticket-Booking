import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TheaterChartComponent } from './theater-chart.component';

describe('TheaterChartComponent', () => {
  let component: TheaterChartComponent;
  let fixture: ComponentFixture<TheaterChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TheaterChartComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TheaterChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
