import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeatVipComponent } from './seat-vip.component';

describe('SeatVipComponent', () => {
  let component: SeatVipComponent;
  let fixture: ComponentFixture<SeatVipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeatVipComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SeatVipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
