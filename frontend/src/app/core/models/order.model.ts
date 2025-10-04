import {OrderStatus, PaymentMethod, PaymentStatus} from '@/app/core/constants/enum';
import {DateModel, FilterDate, FilterQuery, FilterSort} from '@/app/core/models/common.model';
import {CinemaSeats} from '@/app/core/models/cinemaHall.model';

export interface Order extends DateModel {
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
  seat: CinemaSeats
}

export interface OrderFilter extends FilterQuery, FilterSort, FilterDate {
  orderStatus: OrderStatus;
  paymentMethod: PaymentMethod;
  paymentStatus: PaymentStatus;
  totalAmount: number;
}
