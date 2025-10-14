export interface ApiRes<T> {
  status: string;
  message: string;
  data: T;
}

export interface DateModel {
  createdAt: Date;
  updatedAt: Date;
}

export interface FilterQuery {
  query?: string;
}

export interface FilterSort {
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
  page?: number;
}

export interface FilterDate {
  dateFrom?: string;
  dateTo?: string;
}

export interface IsDeleted {
  isDeleted: boolean;
}
