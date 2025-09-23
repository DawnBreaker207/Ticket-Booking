// src/types/CinemaHall.ts
import type { Movie } from './Movie';

// Các trạng thái ghế phổ biến trong API của bạn
export type SeatStatus = 'AVAILABLE' | 'RESERVED' | 'BOOKED' | 'UNKNOWN' | string;

export interface Seat {
  id: number;
  cinemaHallId?: number;
  seatNumber: string;
  price: number;
  status: SeatStatus;

}

export interface CinemaHall {
  id: number;
  movieSession: string;
  movie: Movie;
  // seats có thể không tồn tại trên một số response, nên khai báo optional
  seats?: Seat[];

}
