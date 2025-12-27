import { inject, Injectable } from '@angular/core';
import { environment } from '@env/environment.development';
import { HttpClient, HttpContext } from '@angular/common/http';
import { SKIP_AUTH, USE_HEADER } from '@core/constants/http-context.tokens';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UploadService {
  URL = `${environment.apiUrl}/cloudinary`;
  private http = inject(HttpClient);

  uploadAssets(file: File) {
    const formData = new FormData();
    formData.append('image', file);
    return this.http
      .post<{ secure_url: string }>(`${this.URL}/upload`, formData, {
        context: new HttpContext().set(SKIP_AUTH, true).set(USE_HEADER, false),
      })
      .pipe(map((res) => res.secure_url));
  }
}
