import {createActionGroup, emptyProps, props} from '@ngrx/store';
import {Seat, Theater, TheaterRequest} from '@/app/core/models/theater.model';

export const TheaterActions = createActionGroup({
  source: 'Schedule',
  events: {
    // Load all
    'Load Theaters': emptyProps(),
    'Load Theaters Success': props<{ theaters: Theater[] }>(),
    'Load Theaters Failed': props<{ error: any }>(),

    // Load simple
    'Load Theater': props<{ id: number }>(),
    'Load Theater Success': props<{ theater: Theater }>(),
    'Load Theater Failed': props<{ error: any }>(),

    // Create
    'Create Theater': props<{ theater: TheaterRequest }>(),
    'Create Theater Success': props<{ theater: Theater }>(),
    'Create Theater Failed': props<{ error: any }>(),

    // Update
    'Update Theater': props<{ id: number, theater: Theater }>(),
    'Update Theater Success': props<{ theater: Theater }>(),
    'Update Theater Failed': props<{ error: any }>(),

    // Delete
    'Delete Theater': props<{ id: number }>(),
    'Delete Theater Success': props<{ id: number }>(),
    'Delete Theater Failed': props<{ error: any }>(),

    //   UI Actions
    'Selected Theater': props<{ theater: Theater | null }>(),
    'Clear Error': emptyProps(),
    'Clear Selected Theater': emptyProps()
  }
});
