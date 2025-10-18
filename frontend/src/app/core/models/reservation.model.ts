import { ReservationStatus } from '@/app/core/constants/enum';
import {
  DateModel,
  FilterDate,
  FilterQuery,
  FilterSort,
  IsDeleted,
} from '@/app/core/models/common.model';
import { User } from '@/app/core/models/user.model';
import { Seat, Showtime } from '@/app/core/models/theater.model';

export interface Reservation extends DateModel, IsDeleted {
  id: string;
  user: User;
  showtime: Showtime;
  reservationStatus: ReservationStatus;
  totalAmount: number;
  seats: Seat[];
}

export interface ReservationInitRequest {
  reservationId?: string;
  userId: number;
  showtimeId: number;
  theaterId: number;
}
export interface ReservationRequest {
  reservationId: string;
  userId: number;
  showtimeId: number;
  seatIds: number[];
}

export interface ReservationFilter extends FilterQuery, FilterSort, FilterDate {
  userId: number;
  reservationStatus: ReservationStatus;
  totalAmount: number;
}
