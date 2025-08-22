export interface Movie {
    id: string;
    title: string;
    overview?: string;
    genre: string[];
    duration: number;
    language?: string;
    releaseDate?: string;
    poster?: string;
}

export interface ApiMovie {
    id: number;
    Title: string;
    Year: string;
    Genre: string;
    imdbID?: string;
    Poster?: string;
}