import { Component, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { NzModalService } from 'ng-zorro-antd/modal';
import { DatePipe } from '@angular/common';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzSpaceComponent } from 'ng-zorro-antd/space';
import { NzTableModule, NzTableQueryParams } from 'ng-zorro-antd/table';
import { Store } from '@ngrx/store';
import { NzAlertComponent } from 'ng-zorro-antd/alert';
import { headerColumns } from '@core/constants/column';
import {
  selectAllTheaters,
  selectPaginationTheater,
  selectTheaterError,
  selectTheaterLoading,
} from '@domain/theater/data-access/theater.selectors';
import { TheaterActions } from '@domain/theater/data-access/theater.actions';
import { FormTheaterComponent } from '@features/admin/theater/form/theater-form.component';
import { LoadingComponent } from '@shared/components/loading/loading.component';

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
    ReactiveFormsModule,
    NzAlertComponent,
    LoadingComponent,
  ],
  providers: [NzModalService],
  templateUrl: './theater.component.html',
  styleUrl: './theater.component.css',
})
export class TheaterComponent {
  private modalService = inject(NzModalService);
  private store = inject(Store);

  readonly headerColumn = headerColumns.theater;
  theaters = this.store.selectSignal(selectAllTheaters);
  pagination = this.store.selectSignal(selectPaginationTheater);
  loading = this.store.selectSignal(selectTheaterLoading);
  error = this.store.selectSignal(selectTheaterError);

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
      nzFooter:
        mode !== 'view'
          ? [
              {
                label: 'Confirm',
                type: 'primary',
                onClick: () => {
                  const form =
                    modal.getContentComponent() as FormTheaterComponent;
                  console.log(form.form.valid);
                  if (form.form.valid) {
                    form.submit();
                    modal.close();
                  }
                },
              },
            ]
          : null,
    });
  }

  onQueryParamsChange(params: NzTableQueryParams) {
    const { pageIndex, pageSize } = params;
    this.store.dispatch(
      TheaterActions.loadTheaters({ page: pageIndex - 1, size: pageSize }),
    );
  }

  onDelete(id: number) {
    this.store.dispatch(TheaterActions.deleteTheater({ id }));
  }
}
