import {createActionGroup, props} from '@ngrx/store';
import {Movie} from '@/app/core/models/movie.model';

export const MovieActions = createActionGroup({
  source: 'Movie',
  events: {
    'Load Movies': props<{ movies: Movie[] }>(),
    'Load Movies Success': props<{ movies: Movie[] }>(),
    'Load Movies Failed': props<{ error: any }>(),

    'Load Movie': props<{ movie: Movie }>(),
    'Load Movie Success': props<{ movie: Movie }>(),
    'Load Movie Failed': props<{ error: any }>(),
  }
});
