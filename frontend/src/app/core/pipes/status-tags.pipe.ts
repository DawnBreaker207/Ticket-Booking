import {Pipe, PipeTransform} from '@angular/core';
import {OrderStatus, PaymentMethod, PaymentStatus} from '@/app/core/constants/enum';


type AllStatus = OrderStatus | PaymentStatus | PaymentMethod

@Pipe({
  name: 'tags'
})
export class StatusTagsPipe implements PipeTransform {
  private statusColors: Record<AllStatus, string> = {
    CREATED: 'blue',
    CONFIRMED: 'green',
    CANCELLED: 'red',

    CASH: 'lime',
    MOMO: 'purple',
    VNPAY: 'blue',
    ZALOPAY: 'cyan',

    PENDING: 'yellow',
    PAID: 'green',
  }

  transform(status: string): { status: string; color: string } {
    const color = this.statusColors[status as AllStatus] || 'gray'
    return {color, status};
  }

}
