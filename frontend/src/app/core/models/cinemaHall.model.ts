import {SeatStatus} from '@/app/core/constants/enum';
import {Movie} from '@/app/core/models/movie.model';

export interface CinemaHall {
  id: number,
  movie: Movie;
  movieSession: string;
  seats: CinemaSeats[]
}

export interface CinemaSeats {
  cinemaHallId: number,
  id: number,
  price: number
  seatNumber: string,
  status: SeatStatus
}
