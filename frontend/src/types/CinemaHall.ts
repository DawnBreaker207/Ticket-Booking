// src/types/CinemaHall.ts
import type { Movie } from './Movie';

export interface CinemaHall {
  id: number;
  movieSession: string; 
  movie: Movie; 
}
