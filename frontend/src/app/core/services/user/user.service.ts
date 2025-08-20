import {inject, Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {environment} from '@/environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  URL = `${environment.ApiUrl}/user`;
  private http = inject(HttpClient);

  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      console.log(`${operation} failed: ${error}`);
      return of(result as T);
    }
  }
}
