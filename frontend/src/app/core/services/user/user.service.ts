import {inject, Injectable} from '@angular/core';
import {catchError, map, Observable, of} from 'rxjs';
import {environment} from '@/environments/environment';
import {HttpClient} from '@angular/common/http';
import {ApiRes} from '@/app/core/models/common.model';
import {User} from '@/app/core/models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  URL = `${environment.apiUrl}/user`;
  private http = inject(HttpClient);

  getALl() {
    return this.http.get<ApiRes<User>>(`${this.URL}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<User>('Get users')),
    );
  }

  getById(id: number) {
    return this.http.get<ApiRes<User>>(`${this.URL}/${id}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<User>('Get users')),
    );
  }

  getByEmail(email: string) {
    return this.http.get<ApiRes<User>>(`${this.URL}/email/${email}`).pipe(
      map((res) => res.data),
      catchError(this.handleError<User>('Get users')),
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    };
  }
}
