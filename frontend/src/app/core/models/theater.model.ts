import { SeatStatus } from '@/app/core/constants/enum';
import { DateModel, IsDeleted } from '@/app/core/models/common.model';
import { Reservation } from '@/app/core/models/reservation.model';

export interface Theater extends DateModel, IsDeleted {
  id: number;
  name: string;
  location: string;
  capacity: number;
  showtime: Showtime[];
}

export interface TheaterRequest {
  name: string;
  location: string;
  capacity: number;
}

export interface Showtime extends DateModel, IsDeleted {
  id: number;
  movieId: number;
  movieTitle: string;
  moviePosterUrl: string;

  theaterId: number;
  theaterName: string;
  theaterLocation: string;

  showDate: Date;
  showTime: Date;

  price: number;

  totalSeats: number;
  availableSeats: number;
}

export interface ShowtimeRequest {
  movieId: number;
  theaterId: number;
  showDate: Date;
  showtime: Date;
  price: number;
  totalSeats: number;
}

export interface Seat {
  id: number;
  showtimeId: number;
  seatNumber: string;
  status: SeatStatus;
}

export interface SeatRequest {
  id: number;
  showtime: Showtime;
  seatNumber: string;
  status: SeatStatus;
  reservation: Reservation;
}
