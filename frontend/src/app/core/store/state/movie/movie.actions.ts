import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Movie, MovieRequest } from '@/app/core/models/movie.model';

export const MovieActions = createActionGroup({
  source: 'Movie',
  events: {
    // API
    // Load all
    'Load Movies': emptyProps(),
    'Load Movies Success': props<{ movies: Movie[] }>(),
    'Load Movies Failed': props<{ error: any }>(),

    // Search
    'Search Movies': props<{ search: string }>(),
    'Search Movies Success': props<{ movies: Movie[] }>(),
    'Search Movies Failed': props<{ error: any }>(),

    // Load simple
    'Load Movie': props<{ id: number }>(),
    'Load Movie Success': props<{ movie: Movie }>(),
    'Load Movie Failed': props<{ error: any }>(),

    // Create
    'Create Movie': props<{ movie: MovieRequest }>(),
    'Create Movie Success': props<{ movie: Movie }>(),
    'Create Movie Failed': props<{ error: any }>(),

    // Update
    'Update Movie': props<{ id: number; movie: MovieRequest }>(),
    'Update Movie Success': props<{ movie: Movie }>(),
    'Update Movie Failed': props<{ error: any }>(),

    // Delete
    'Delete Movie': props<{ id: number }>(),
    'Delete Movie Success': props<{ id: number }>(),
    'Delete Movie Failed': props<{ error: any }>(),

    //   UI Actions
    'Selected Movie': props<{ movie: Movie | null }>(),
    'Clear Error': emptyProps(),
    'Clear Selected Movie': emptyProps(),
  },
});
