export type OrderStatus = 'CREATED' | 'CONFIRMED' | 'CANCELED'

export type PaymentMethod = 'CASH' | 'MOMO' | 'VNPAY' | 'ZALOPAY'

export type PaymentStatus = 'PENDING' | 'PAID' | 'CANCELED'


export type SeatStatus = 'AVAILABLE' | 'BOOKED' | 'SELECTED'

export enum Role {
  USER = 'USER',
  MODERATOR = 'MODERATOR',
  ADMIN = 'ADMIN',
}
