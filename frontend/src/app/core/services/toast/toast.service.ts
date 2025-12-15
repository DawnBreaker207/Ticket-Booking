import { inject, Injectable } from '@angular/core';
import { NzNotificationService } from 'ng-zorro-antd/notification';

type notificationInfo = {
  type: 'success' | 'info' | 'warning' | 'error';
  title: string;
  message: string;
  position:
    | 'top'
    | 'bottom'
    | 'topLeft'
    | 'topRight'
    | 'bottomLeft'
    | 'bottomRight';
};

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private notification = inject(NzNotificationService);

  createNotification(info: Partial<notificationInfo> = {}) {
    const {
      type = 'info',
      title = 'title',
      message = 'message',
      position = 'topRight',
    } = info;
    this.notification.create(type, title, message, {
      nzPlacement: position,
      nzDuration: 2500,
    });
  }
}
