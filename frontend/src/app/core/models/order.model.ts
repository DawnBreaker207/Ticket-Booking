import {OrderStatus, PaymentMethod, PaymentStatus} from '@/app/core/constants/enum';

export interface Order {
  orderId: string;
  userId: number;
  cinemaHallId: number;
  orderStatus: OrderStatus;
  paymentMethod: PaymentMethod;
  paymentStatus: PaymentStatus;
  totalAmount: number;
  seats: OrderSeat[];
}

export interface OrderSeat {
  seatId: number;
  price: number
}
