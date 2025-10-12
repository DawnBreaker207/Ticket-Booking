import {DateModel, IsDeleted} from '@/app/core/models/common.model';

export interface Movie extends DateModel, IsDeleted {
  id: number;
  title: string;
  poster: string;
  overview: string;
  duration: number;
  genres: String[];
  releaseDate: Date;
  language: string;
  imdbId: string;
  filmId: string;
}

export interface MovieRequest {
  title: string;
  poster: string;
  overview: string;
  duration: number;
  genres: String[];
  releaseDate: Date;
  language: string;
  imdbId: string;
  filmId: string;
}

export interface ApiMovie {
  id: number;
  Title: String;
  Year: string;
  Genre: string;
  imdbId?: string;
  Poster?: string;
}
