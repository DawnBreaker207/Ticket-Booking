import {inject, Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {catchError, map, of, switchMap} from 'rxjs';
import {ScheduleActions} from '@/app/core/store/state/schedule/schedule.actions';
import {ScheduleService} from '@/app/core/services/schedule/schedule.service';

@Injectable()
export class ScheduleEffects {
  private actions$ = inject(Actions);
  private ScheduleService = inject(ScheduleService);

  schedules$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ScheduleActions.loadSchedules),
      switchMap(() =>
        this.ScheduleService.getSchedules()
          .pipe(
            map((schedules) => ScheduleActions.loadSchedulesSuccess({schedules})),
            catchError((err) => of(ScheduleActions.loadSchedulesFailed({error: err})))
          )
      )
    )
  })

  schedule$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(ScheduleActions.loadSchedule),
      switchMap(({scheduleId}) =>
        this.ScheduleService.getSchedule(scheduleId)
          .pipe(
            map((schedule) => ScheduleActions.loadScheduleSuccess({schedule})),
            catchError((err) => of(ScheduleActions.loadScheduleFailed({error: err})))
          )
      )
    )
  })

}
