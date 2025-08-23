import {Component, inject, OnInit} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {ScheduleService} from '@/app/core/services/schedule/schedule.service';
import {NzModalService} from 'ng-zorro-antd/modal';
import {CinemaHall} from '@/app/core/models/cinemaHall.model';
import {headerColumns} from '@/app/core/constants/column';
import {MovieService} from '@/app/core/services/movie/movie.service';
import {FormScheduleComponent} from '@/app/modules/admin/components/schedule/form/form.component';
import {DatePipe} from '@angular/common';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzSpaceComponent} from 'ng-zorro-antd/space';
import {NzTableModule} from 'ng-zorro-antd/table';

@Component({
  selector: 'app-schedule',
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
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.css'
})
export class ScheduleComponent implements OnInit {
  private scheduleService = inject(ScheduleService);
  private modalService = inject(NzModalService);
  private movieService = inject(MovieService);
  headerColumn = headerColumns.schedule;
  scheduleList: readonly CinemaHall[] = [];

  ngOnInit() {
    this.scheduleService.getSchedules().subscribe(data => this.scheduleList = data);
  }


  openModal(mode: 'add' | 'edit' | 'view', id?: number) {
    const modal = this.modalService.create({
      nzContent: FormScheduleComponent,
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
            const form = modal.getContentComponent() as FormScheduleComponent;
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
    this.scheduleService.deleteSchedule(id).subscribe(data => {
      console.log(data)
    })
  }
}
