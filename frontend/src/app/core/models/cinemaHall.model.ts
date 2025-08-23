import {SeatStatus} from '@/app/core/constants/enum';
import {Movie} from '@/app/core/models/movie.model';

export interface CinemaHall {
  id: number,
  movie: Movie;
  movieSession: string;
  seatCodes: string[];
  seatStatus: SeatStatus;
}
