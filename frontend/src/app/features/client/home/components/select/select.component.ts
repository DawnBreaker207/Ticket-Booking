import { Component, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { AsyncPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { combineLatest, map } from 'rxjs';
import {
  selectAllTheaters,
  selectSelectedTheaterId,
} from '@domain/theater/data-access/theater.selectors';
import { TheaterActions } from '@domain/theater/data-access/theater.actions';
import { Theater } from '@domain/theater/models/theater.model';

@Component({
  selector: 'app-theater-select',
  imports: [
    NzSelectModule,
    AsyncPipe,
    FormsModule,
    NzIconDirective,
    NzDropDownModule,
  ],
  templateUrl: './select.component.html',
  styleUrl: './select.component.css',
})
export class SelectShowtimeComponent {
  private store = inject(Store);

  theaters$ = this.store.select(selectAllTheaters);
  selectedTheaterId$ = this.store.select(selectSelectedTheaterId);

  selectTheaterName$ = combineLatest([
    this.theaters$,
    this.selectedTheaterId$,
  ]).pipe(
    map(([theaters, id]) => {
      const found = theaters.find((t: Theater) => t.id === id);
      return found ? found.name : 'Chọn rạp chiếu';
    }),
  );

  onSelect(theaterId: number) {
    this.store.dispatch(
      TheaterActions.setSelectedTheaterId({ theaterId: theaterId }),
    );
  }
}
