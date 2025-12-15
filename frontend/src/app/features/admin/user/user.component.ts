import { Component, inject, OnInit, signal } from '@angular/core';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSwitchModule } from 'ng-zorro-antd/switch';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { DatePipe } from '@angular/common';
import { NzTagComponent } from 'ng-zorro-antd/tag';
import { FormsModule } from '@angular/forms';
import { IsDeletedPipe } from '@core/pipes/is-deleted.pipe';
import { UserService } from '@domain/user/data-access/user.service';
import { User } from '@domain/user/models/user.model';
import { Pagination } from '@core/models/common.model';
import { headerColumns } from '@core/constants/column';

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
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
})
export class UserComponent implements OnInit {
  private userService = inject(UserService);
  userList: readonly User[] = [];
  pagination: Pagination | null = null;
  headerColumns = headerColumns.user;
  value = true;
  pageIndex = 1;
  pageSize = 10;
  isLoading = signal<boolean>(true);

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAll({ page: 0, size: 10 }).subscribe({
      next: (res) => {
        if (res) {
          this.isLoading.set(false);
          this.userList = res.content;
          this.pagination = res.pagination;
        }
      },
      error: () => {
        this.isLoading.set(true);
      },
    });
  }

  updateStatus(id: number, status: boolean) {
    this.isLoading.set(true);
    this.userService.updateStatus(id, status).subscribe(() => {
      this.isLoading.set(false);
      this.loadUsers();
    });
  }
}
