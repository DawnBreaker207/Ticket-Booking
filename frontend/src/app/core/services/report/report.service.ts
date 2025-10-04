import {inject, Injectable} from '@angular/core';
import {environment} from '@/environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  URL = `${environment.apiUrl}/export`;
  private http = inject(HttpClient);

  downloadReport(type: string) {
    return this.http.get(`${this.URL}/report/${type}`, {responseType: "blob"})

  }
}
