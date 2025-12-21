import { Component, inject, OnInit } from '@angular/core';
import { NzTableModule, NzTableQueryParams } from 'ng-zorro-antd/table';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSwitchModule } from 'ng-zorro-antd/switch';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { DatePipe } from '@angular/common';
import { NzTagComponent } from 'ng-zorro-antd/tag';
import { FormsModule } from '@angular/forms';
import { IsDeletedPipe } from '@core/pipes/is-deleted.pipe';
import { headerColumns } from '@core/constants/column';
import { LoadingComponent } from '@shared/components/loading/loading.component';
import { UserStore } from '@domain/user/data-access/user.store';

@Component({
  selector: 'app-user',
  imports: [
    NzTableModule,
    NzInputModule,
    NzSwitchModule,
    NzIconModule,
    NzSpinModule,
    DatePipe,
    IsDeletedPipe,
    NzTagComponent,
    FormsModule,
    IsDeletedPipe,
    LoadingComponent,
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
})
export class UserComponent implements OnInit {
  readonly userStore = inject(UserStore);

  headerColumns = headerColumns.user;
  pageIndex = 1;
  pageSize = 10;

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userStore.loadUsers({ page: this.pageIndex - 1, size: this.pageSize });
  }

  updateStatus(id: number, status: boolean) {
    this.userStore.updateUserStatus({ id: id, isDeleted: status });
  }

  onQueryParamsChange(params: NzTableQueryParams) {
    const { pageIndex, pageSize } = params;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
    this.loadUsers();
  }
}
