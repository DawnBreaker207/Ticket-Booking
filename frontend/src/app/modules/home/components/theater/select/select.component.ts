import {Component, inject} from '@angular/core';
import {Store} from '@ngrx/store';
import {selectAllTheaters, selectSelectedTheaterId} from '@/app/core/store/state/theater/theater.selectors';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {AsyncPipe} from '@angular/common';
import {TheaterActions} from '@/app/core/store/state/theater/theater.actions';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-theater-select',
  imports: [NzSelectModule, AsyncPipe, FormsModule],
  templateUrl: './select.component.html',
  styleUrl: './select.component.css',
})
export class SelectShowtimeComponent {
  private store = inject(Store);

  theaters$ = this.store.select(selectAllTheaters);
  selectedTheaterId$ = this.store.select(selectSelectedTheaterId);

  onSelect(theaterId: number) {
    this.store.dispatch(TheaterActions.setSelectedTheaterId({theaterId: theaterId}));
  }
}
