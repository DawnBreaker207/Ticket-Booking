// src/services/movie.ts
import instance from '../config/axios';
import type { Movie } from '../types/Movie';
import moment from 'moment';

function normalizeGenres(m: any): string[] {
    if (!m) return [];
    if (Array.isArray(m.genres)) return m.genres.map((g: any) => (typeof g === 'string' ? g : g?.name)).filter(Boolean);
    return [];
}

function toMovie(m: any): Movie {
    let rawDate = m.release_date ?? m.releaseDate ?? m.release;
    let formattedDate: string | undefined;
    const imdbId = m.imdbId ?? m.imdb_id ?? m.imdbID ?? undefined;
    const filmId = m.filmId ?? m.film_id ?? m.filmid ?? undefined;
    if (rawDate) {
        const mm = moment(rawDate);
        if (mm.isValid()) formattedDate = mm.format('DD/MM/YYYY');
        else formattedDate = String(rawDate);
    }
    return {
        id: Number(m.id),
        title: m.title,
        overview: m.overview,
        genres: normalizeGenres(m),
        duration: typeof m.duration === 'number' ? m.duration : (Number(m.duration) || 0),
        language: m.language ?? m.originalLanguage ?? m.original_language,
        releaseDate: formattedDate,
        poster: m.poster ?? m.poster_path,
        imdbId: imdbId ?? undefined,
        filmId: filmId ?? undefined,
    };
}
function formatReleaseForApi(release?: string): string | undefined {
    if (!release) return undefined;
    const formats = [
        'DD/MM/YYYY', 'DD-MM-YYYY',
        'YYYY-MM-DD', 'YYYY/MM/DD',
        'MM/DD/YYYY', 'MM-DD-YYYY',
    ];
    let mm = moment(release, formats, true);
    if (mm.isValid()) return mm.format('YYYY-MM-DD');
    mm = moment(release);
    if (mm.isValid()) return mm.format('YYYY-MM-DD');
    return release;
}

export const movieService = {
    async getMovies(title?: string): Promise<Movie[]> {
        const params = title ? { title } : undefined;
        const res = await instance.get('/movie', { params });
        const list = res.data && res.data.data ? res.data.data : [];
        return list.map(toMovie);
    },

    async createMovie(payload: Omit<Movie, 'id'>): Promise<Movie> {
        const formatted = formatReleaseForApi(payload.releaseDate);
        const body = { ...payload, releaseDate: formatted ?? payload.releaseDate };
        const res = await instance.post('/movie', body);
        const movie = res.data && res.data.data ? res.data.data : res.data;
        return toMovie(movie);
    },

    async updateMovie(id: number, payload: Omit<Movie, 'id'>): Promise<Movie> {
        const formatted = formatReleaseForApi(payload.releaseDate);
        const body = { ...payload, id: Number(id), releaseDate: formatted ?? payload.releaseDate };
        const res = await instance.put(`/movie/${encodeURIComponent(String(id))}`, body);
        const movie = res.data && res.data.data ? res.data.data : res.data;
        return toMovie(movie);
    },

    async deleteMovie(id: number): Promise<void> {
        await instance.delete(`/movie/${encodeURIComponent(String(id))}`);
    },
};

export default movieService;
