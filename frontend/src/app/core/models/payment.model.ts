import {PaymentMethod} from '@/app/core/constants/enum';

export interface Payment {
  code: string;
  message: string;
  paymentUrl: string;
}

export interface PaymentRequest {
  reservationId: string;
  amount: number;
  paymentType: PaymentMethod;
}
