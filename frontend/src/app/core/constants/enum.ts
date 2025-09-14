export type OrderStatus = 'CREATED' | 'CONFIRMED' | 'CANCELLED'

export type PaymentMethod = 'CASH' | 'MOMO' | 'VNPAY' | 'ZALOPAY'

export type PaymentStatus = 'PENDING' | 'PAID' | 'CANCELLED'


export type SeatStatus = 'AVAILABLE' | 'BOOKED' | 'SELECTED'

export enum Role {
  USER = 'USER',
  MODERATOR = 'MODERATOR',
  ADMIN = 'ADMIN',
}
