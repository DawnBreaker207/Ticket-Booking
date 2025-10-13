import {Component, inject, OnInit} from '@angular/core';
import {NzModalService} from 'ng-zorro-antd/modal';
import {Store} from '@ngrx/store';
import {headerColumns} from '@/app/core/constants/column';
import {Showtime} from '@/app/core/models/theater.model';
import {DatePipe} from '@angular/common';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzIconDirective} from 'ng-zorro-antd/icon';
import {NzInputDirective, NzInputGroupComponent} from 'ng-zorro-antd/input';
import {NzOptionComponent, NzSelectComponent} from 'ng-zorro-antd/select';
import {NzSpaceComponent} from 'ng-zorro-antd/space';
import {
  NzTableCellDirective,
  NzTableComponent,
  NzTbodyComponent,
  NzTheadComponent,
  NzThMeasureDirective,
  NzTrDirective
} from 'ng-zorro-antd/table';
import {NzWaveDirective} from 'ng-zorro-antd/core/wave';
import {selectAllShowtimes} from '@/app/core/store/state/showtime/showtime.selectors';
import {FormShowtimeComponent} from '@/app/modules/admin/components/showtime/form/form.component';
import {ShowtimeActions} from '@/app/core/store/state/showtime/showtime.actions';

@Component({
  selector: 'app-showtime',
  imports: [
    DatePipe,
    NzButtonComponent,
    NzIconDirective,
    NzInputDirective,
    NzInputGroupComponent,
    NzOptionComponent,
    NzSelectComponent,
    NzSpaceComponent,
    NzTableCellDirective,
    NzTableComponent,
    NzTbodyComponent,
    NzThMeasureDirective,
    NzTheadComponent,
    NzTrDirective,
    NzWaveDirective
  ],
  templateUrl: './showtime.component.html',
  styleUrl: './showtime.component.css'
})
export class ShowtimeComponent implements OnInit{
  private modalService = inject(NzModalService);
  private store = inject(Store);
  headerColumn = headerColumns.showtime;
  showtimeList: readonly Showtime[] = [];

  ngOnInit() {
    this.store.select(selectAllShowtimes).subscribe(data => {
      this.showtimeList = data;
    })
  }


  openModal(mode: 'add' | 'edit' | 'view', id?: number) {
    const modal = this.modalService.create({
      nzContent: FormShowtimeComponent,
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
            const form = modal.getContentComponent() as FormShowtimeComponent;
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
    this.store.dispatch(ShowtimeActions.deleteShowtime({id}))
    this.store.select(selectAllShowtimes).subscribe(data => {
      this.showtimeList = data;
    })
  }
}
