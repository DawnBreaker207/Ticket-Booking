// src/services/cinemaHallService.ts
import instance from '../config/axios'; // your axios instance (with baseURL http://localhost:8888/api/v1)
import type { CinemaHall } from '../types/CinemaHall';

function normalizeItem(raw: any): CinemaHall {
  const id = typeof raw.id === 'number' ? raw.id : Number(raw.id);
  const movieSession = raw.movieSession ?? raw.movie_session ?? raw.session ?? '';
  let movie = raw.movie ?? raw.movieInfo ?? raw.film ?? null;

  if (typeof movie === 'string') {
    try { movie = JSON.parse(movie); } catch { movie = { id: undefined, title: String(movie) } as any; }
  }
  if (!movie || typeof movie !== 'object') {
    movie = { id: undefined, title: String(movie ?? '') } as any;
  }

  // normalize seats if present
  let seats = Array.isArray(raw.seats) ? raw.seats.map((s: any) => ({
    id: s.id,
    cinemaHallId: s.cinemaHallId ?? s.cinema_hall_id ?? id,
    seatNumber: s.seatNumber ?? s.seat_number ?? s.seat ?? '',
    price: typeof s.price === 'number' ? s.price : Number(s.price ?? 0),
    status: s.status ?? 'UNKNOWN',
  })) : [];

  return {
    id,
    movieSession: String(movieSession),
    movie,
    seats,
    // other fields if you want: createdAt/updatedAt etc.
  } as any;
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
   * GET /cinema/:id
   */
  async getById(id: number): Promise<CinemaHall> {
    const res = await instance.get(`/cinema/${encodeURIComponent(String(id))}`);
    const payload = unwrapResponse(res.data ?? res);
    const item = payload?.data ?? payload;
    return normalizeItem(item);
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
