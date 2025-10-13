import {Component, inject, OnInit} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {NzModalService} from 'ng-zorro-antd/modal';
import {headerColumns} from '@/app/core/constants/column';
import {DatePipe} from '@angular/common';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzSpaceComponent} from 'ng-zorro-antd/space';
import {NzTableModule} from 'ng-zorro-antd/table';
import {Store} from '@ngrx/store';
import {Theater} from '@/app/core/models/theater.model';
import {selectAllTheaters} from '@/app/core/store/state/theater/theater.selectors';
import {TheaterActions} from '@/app/core/store/state/theater/theater.actions';
import {FormTheaterComponent} from '@/app/modules/admin/components/theater/form/form.component';

@Component({
  selector: 'app-theater',
  imports: [
    NzTableModule,
    NzInputModule,
    NzSelectModule,
    NzButtonModule,
    NzIconModule,
    DatePipe,
    NzSpaceComponent,
    ReactiveFormsModule
  ],
  providers: [NzModalService],
  templateUrl: './theater.component.html',
  styleUrl: './theater.component.css'
})
export class TheaterComponent implements OnInit {
  private modalService = inject(NzModalService);
  private store = inject(Store);
  headerColumn = headerColumns.theater;
  theaterList: readonly Theater[] = [];

  ngOnInit() {
    this.store.select(selectAllTheaters).subscribe(data => {
      this.theaterList = data;
    })
  }

  openModal(mode: 'add' | 'edit' | 'view', id?: number) {
    const modal = this.modalService.create({
      nzContent: FormTheaterComponent,
      nzTitle: undefined,
      nzClosable: true,
      nzData: {
        mode: mode,
        id: id,
      },
      nzWidth: 900,
      nzKeyboard: true,
      nzFooter: mode !== 'view' ? [
        {
          label: 'Confirm',
          type: 'primary',
          onClick: () => {
            const form = modal.getContentComponent() as FormTheaterComponent;
            console.log(form.form.valid)
            if (form.form.valid) {
              form.submit();
              modal.close();
            }
          }
        },
      ] : null
    })
  }

  onDelete(id: number) {
    this.store.dispatch(TheaterActions.deleteTheater({id}))
    this.store.select(selectAllTheaters).subscribe(data => {
      this.theaterList = data;
    })
  }
}
