import {SeatStatus} from '@/app/core/constants/enum';

export interface CinemaHall {
  movieSession: string;
  orderTime: string;
  seatCodes: string[];
  seatStatus: SeatStatus;
}
