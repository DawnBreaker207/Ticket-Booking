import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  URL = `${environment.apiUrl}/report`;
  private http = inject(HttpClient);

  downloadReport(type: string) {
    return this.http.get(`${this.URL}/export/${type}`, {
      responseType: 'blob',
    });
  }
}
