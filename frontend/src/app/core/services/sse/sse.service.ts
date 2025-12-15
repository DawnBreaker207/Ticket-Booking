import { inject, Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class SseService {
  URL = `${environment.apiUrl}/notification/subscribe/showtime`;
  private zone = inject(NgZone);

  connect(id: string | number, userId: number) {
    return new Observable((observer) => {
      // Seat channel

      const source = new EventSource(`${this.URL}/${id}?clientId=${userId}`);
      source.addEventListener('SEAT_STATE_INIT', (event: MessageEvent) => {
        this.zone.run(() => {
          const data = JSON.parse(event.data);
          observer.next({ event: 'SEAT_STATE_INIT', data });
        });
      });
      source.addEventListener('SEAT_HOLD', (event: MessageEvent) => {
        this.zone.run(() => {
          const data = JSON.parse(event.data);
          observer.next({ event: 'SEAT_HOLD', data });
        });
      });
      source.addEventListener('SEAT_RELEASE', (event: MessageEvent) => {
        this.zone.run(() => {
          const data = JSON.parse(event.data);
          observer.next({ event: 'SEAT_RELEASE', data });
        });
      });

      // ✅ Optional: thêm handler fallback cho các event khác
      source.onmessage = (event) => {
        this.zone.run(() => {
          observer.next({
            event: 'message',
            data: JSON.parse(event.data),
          });
        });
      };

      source.onerror = (error) => {
        console.error('Seat SSE error', error);
        observer.error(error);
        source?.close();
        setTimeout(() => this.connect(id, userId), 1000);
      };

      // Clean up
      return () => {
        source?.close();
      };
    });
  }
}
