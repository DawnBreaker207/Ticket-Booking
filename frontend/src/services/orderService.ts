// src/services/orderService.ts
import instance from "../config/axios";

export interface InitOrderPayload {
  userId: number;
  cinemaHallId: number;
  orderStatus?: string; 
}

export interface InitOrderResponse {
  code?: number;
  message?: string;
  data?: any; 
  [k: string]: any;
}

// payload cho seatHold
export interface SeatHoldItem {
  seat: { id?: number };
  price: string | number;
}
export interface SeatHoldPayload {
  orderId: string;
  userId?: number;
  cinemaHallId?: number;
  seats: SeatHoldItem[];
}

export interface SeatHoldResponse {
  code?: number;
  message?: string;
  data?: any;
  [k: string]: any;
}

export interface VnPayData {
  code?: string;
  message?: string;
  paymentUrl?: string;
  [k: string]: any;
}

export interface VnPayResponse {
  code?: number;
  message?: string;
  data?: VnPayData | any;
  [k: string]: any;
}
export interface VnPayResult {
  raw: VnPayResponse;
  paymentUrl?: string;
}

export interface ConfirmOrderPayload {
  orderId: string;
  userId?: number;
  cinemaHallId?: number;
  orderStatus?: string; // e.g. "CONFIRMED"
  paymentMethod?: string; // e.g. "CASH" | "CARD" | ...
  paymentStatus?: string; // e.g. "PENDING" | "PAID"
  totalAmount?: string | number; // backend example uses string
  [k: string]: any;
}

export interface ConfirmOrderResponse {
  code?: number;
  message?: string;
  data?: any;
  [k: string]: any;
}

export const orderService = {
  initOrder: async (payload: InitOrderPayload): Promise<InitOrderResponse> => {
    const res = await instance.post("/order/init", payload);
    return res.data;
  },

  seatHold: async (payload: SeatHoldPayload): Promise<SeatHoldResponse> => {
    const res = await instance.post("/order/seatHold", payload);
    return res.data;
  },

    /**
   *
   * @param amount số (ví dụ 100000)
   * @param bankCode tùy chọn (ví dụ 'NCB')
   * @param txnRef
   */
vnpay: async (
  amount: number | string,
  bankCode: string = "NCB",
  txnRef?: string
): Promise<VnPayResult> => {
  const params: Record<string, any> = { amount };
  if (bankCode) params.bankCode = bankCode;
  if (txnRef) params.vnp_TxnRef = txnRef; 

  const res = await instance.get("/payment/vnpay", { params });
  const raw: VnPayResponse = res.data;

  let paymentUrl: string | undefined =
    (raw?.data && (raw.data as any).paymentUrl) ??
    (raw as any).paymentUrl ??
    (raw?.data && (raw.data as any).url) ??
    undefined;

  if (paymentUrl && typeof paymentUrl !== "string") {
    paymentUrl = String(paymentUrl);
  }

  return { raw, paymentUrl };
  
},
  confirmOrder: async (
    payload: ConfirmOrderPayload
  ): Promise<ConfirmOrderResponse> => {
    const res = await instance.post("/order/confirm", payload);
    return res.data;
  },
};
