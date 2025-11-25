import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Movie, MovieRequest } from '@/app/core/models/movie.model';
import { Pagination, ResponsePage } from '@/app/core/models/common.model';

export const MovieActions = createActionGroup({
  source: 'Movie',
  events: {
    // API
    // Load all
    'Load Movies': props<{ page: number; size: number }>(),
    'Load Movies Success': props<{ movies: Movie[]; pagination: Pagination }>(),
    'Load Movies Failed': props<{ error: any }>(),

    // Search
    'Search Movies': props<{ search: string }>(),
    'Search Movies Success': props<{ page: ResponsePage<Movie[]> }>(),
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
