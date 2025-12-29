import { Pipe, PipeTransform } from '@angular/core';
import {
  PaymentMethod,
  PaymentStatus,
  ReservationStatus,
} from '@core/constants/enum';

type AllStatus = ReservationStatus | PaymentStatus | PaymentMethod;

@Pipe({
  name: 'tags',
})
export class StatusTagsPipe implements PipeTransform {
  private statusColors: Record<AllStatus, string> = {
    CONFIRMED: 'green',
    CANCELED: 'red',

    MOMO: 'purple',
    VNPAY: 'blue',
    ZALOPAY: 'cyan',

    PENDING: 'yellow',
    PAID: 'green',
  };

  transform(status: string): { status: string; color: string } {
    const color = this.statusColors[status as AllStatus] || 'gray';
    return { color, status };
  }
}
