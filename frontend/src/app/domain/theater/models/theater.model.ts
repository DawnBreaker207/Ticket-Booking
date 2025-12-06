import { DateModel, IsDeleted } from '@core/models/common.model';
import { Showtime } from '@domain/showtime/models/showtime.model';

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
