// src/types/CinemaHall.ts
import type { Movie } from './movie';

export interface CinemaHall {
  id: number;
  movieSession: string; 
  movie: Movie; 
}
