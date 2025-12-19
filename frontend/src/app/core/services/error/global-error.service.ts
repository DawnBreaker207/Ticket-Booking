import { inject, Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastService } from '@core/services/toast/toast.service';

@Injectable({
  providedIn: 'root',
})
export class GlobalErrorService {
  private toast = inject(ToastService);

  readonly RETRY_CONFIG = {
    maxRetries: 3,
    retryableStatus: [429, 503],
    baseDelayMs: 1000,
  };

  handleError(err: HttpErrorResponse, skipNotification: boolean = false) {
    if (skipNotification) return;
    const { title, message, type } = this.parseError(err);
    this.toast.createNotification({
      type: type as 'error' | 'warning',
      title,
      message,
    });
  }

  shouldRetry(status: number) {
    return this.RETRY_CONFIG.retryableStatus.includes(status);
  }

  getRetryDelay(retryCount: number) {
    return Math.pow(2, retryCount) * this.RETRY_CONFIG.baseDelayMs;
  }

  private parseError(error: HttpErrorResponse) {
    const status = error.status;
    let type = 'error';
    let title = `Error ${status}`;
    let message = error.error?.message || error.message || 'Unknown error';

    switch (status) {
      case 400:
        title = `${status}:Bad Request`;
        break;
      case 401:
        title = `${status}: Unauthorized`;
        message = 'Unauthorized';
        break;
      case 403:
        type = 'warning';
        title = `${status}: Forbidden`;
        message = 'Forbidden';
        break;
      case 404:
        title = `${status}: Not Found`;
        message = 'Not Found';
        break;
      case 429:
        type = 'warning';
        title = `${status}: Too Many Requests`;
        message = 'Too Many Requests';
        break;
      case 500:
        title = `${status}: Server Error`;
        message = 'Server Error';
        break;
      case 503:
        type = 'warning';
        title = `${status}: Service Unavailable`;
        message = 'Service Unavailable';
        break;
      default:
        title = `Error ${status}`;
        break;
    }
    return { type, title, message };
  }
}
