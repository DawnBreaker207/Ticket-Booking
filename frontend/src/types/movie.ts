export interface Movie {
    id: number;
    title: string;
    overview?: string;
    genres: string[];
    duration: number;
    language?: string;
    releaseDate?: string;
    poster?: string;
    imdbId?: string;
    filmId?: string;
}

export interface ApiMovie {
    id: number;
    Title: string;
    Year: string;
    Genre: string;
    imdbID?: string;
    Poster?: string;
}