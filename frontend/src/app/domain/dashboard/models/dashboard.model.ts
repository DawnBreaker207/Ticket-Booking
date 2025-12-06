export interface DashboardQuery {
  from: Date;
  to: Date;
  movieId: number;
  theaterId: number;
}

export interface DashboardMetrics {
  totalRevenue: number;
  ticketsSold: number;
  activeTheaters: number;
  seatUtilization: number;
}

export interface PaymentDistribution {
  method: string;
  amount: number;
}

export interface TopMovie {
  movieName: string;
  ticketSold: number;
  revenue: number;
}

export interface TopTheater {
  theaterName: string;
  ticketsSold: number;
  totalRevenue: number;
}

export interface RevenuePoint {
  date: Date;

  revenue: number;
}
