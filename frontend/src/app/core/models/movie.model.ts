export interface Movie {
  id: number;
  title: string;
  poster: string;
  overview: string;
  duration: number;
  genres: String[];
  releaseDate: Date;
  imdbId: string;
  filmId: string;
}
export interface ApiMovie{
  id: number;
  Title: String;
  Year: string;
  Genre: string;
  imdbId?: string;
  Poster?: string;
}
