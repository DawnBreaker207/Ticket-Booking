export interface ApiRes<T> {
  code: string;
  message: string;
  data: T;
}

export interface Pagination {
  pageNumber: number;
  pageSize: number;
  totalElements: number;
}

export interface ResponsePage<T> {
  content: T;
  pagination: Pagination;
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
  size?: number;
  page?: number;
}

export interface FilterDate {
  startDate?: string;
  endDate?: string;
}

export interface IsDeleted {
  isDeleted: boolean;
}
