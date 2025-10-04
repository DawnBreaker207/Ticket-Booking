import {createActionGroup, props} from '@ngrx/store';

export const CountdownActions = createActionGroup({
  source: 'Countdown',
  events: {
    //   Count down
    'Update Countdown TTL': props<{ ttl: number }>()
  }
});
