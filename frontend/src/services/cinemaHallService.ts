// src/services/cinemaHallService.ts
import instance from '../config/axios'; // your axios instance (with baseURL http://localhost:8888/api/v1)
import type { CinemaHall } from '../types/CinemaHall';

/**
 * Normalize server item to client CinemaHall shape.
 * - ensures id is number
 * - ensures movieSession is string
 * - ensures movie is object (if server sends JSON string, try parse)
 */
function normalizeItem(raw: any): CinemaHall {
  const id = typeof raw.id === 'number' ? raw.id : Number(raw.id);
  const movieSession = raw.movieSession ?? raw.movie_session ?? raw.session ?? '';
  let movie = raw.movie ?? raw.movieInfo ?? raw.film ?? null;

  if (typeof movie === 'string') {
    try {
      movie = JSON.parse(movie);
    } catch {
      // keep string if cannot parse
      movie = { id: undefined, title: String(movie) } as any;
    }
  }

  // make sure movie is at least an object with id and title
  if (!movie || typeof movie !== 'object') {
    movie = { id: undefined, title: String(movie ?? '') } as any;
  }

  return {
    id,
    movieSession: String(movieSession),
    movie,
  };
}

function unwrapResponse<T = any>(res: any): T {
  // If API uses wrapper { code, message, data }
  if (res && typeof res === 'object') {
    if ('data' in res) return res.data as T;
    return res as T;
  }
  return res as T;
}

export const cinemaHallService = {
  /**
   * GET /cinema
   * optional params: { q?: string, page?: number, ... }
   */
  async getAll(params?: Record<string, any>): Promise<CinemaHall[]> {
    const res = await instance.get('/cinema', { params });
    const payload = unwrapResponse(res.data ?? res);
    const list = Array.isArray(payload) ? payload : (payload?.data ?? payload);
    if (!Array.isArray(list)) return [];
    return list.map(normalizeItem);
  },

  /**
   * POST /cinema
   * payload: Omit<CinemaHall, 'id'>
   */
  async create(payload: Omit<CinemaHall, 'id'>): Promise<CinemaHall> {
    const res = await instance.post('/cinema', payload);
    const movieRaw = unwrapResponse(res.data ?? res);
    // API might return created object or wrapper { data: {...} }
    const created = movieRaw?.data ?? movieRaw;
    return normalizeItem(created);
  },

  /**
   * PUT /cinema/:id
   */
  async update(id: number, payload: Omit<CinemaHall, 'id'>): Promise<CinemaHall> {
    const res = await instance.put(`/cinema/${encodeURIComponent(String(id))}`, payload);
    const movieRaw = unwrapResponse(res.data ?? res);
    const updated = movieRaw?.data ?? movieRaw;
    return normalizeItem(updated);
  },

  /**
   * DELETE /cinema/:id
   */
  async remove(id: number): Promise<void> {
    await instance.delete(`/cinema/${encodeURIComponent(String(id))}`);
  }
};

export default cinemaHallService;
