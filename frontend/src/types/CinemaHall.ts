// src/types/CinemaHall.ts
import type { Movie } from './Movie';

export type SeatStatus = 'AVAILABLE' | 'RESERVED' | 'BOOKED' | string;

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
  seats?: Seat[];

}
