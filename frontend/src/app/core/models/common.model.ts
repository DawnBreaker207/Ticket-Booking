export interface ApiRes<T> {
  status: string;
  message: string;
  data: T;
}
