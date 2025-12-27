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
    const serverMessage = error.error?.message;
    let message = serverMessage || error.message || 'Unknown error';

    switch (status) {
      case 400:
        title = `${status}:Bad Request`;
        break;
      case 401:
        title = `${status}: Unauthorized`;
        if (!serverMessage) message = 'Unauthorized';
        break;
      case 403:
        type = 'warning';
        title = `${status}: Forbidden`;
        if (!serverMessage) message = 'Forbidden';
        break;
      case 404:
        title = `${status}: Not Found`;
        if (!serverMessage) message = 'Not Found';
        break;
      case 429:
        type = 'warning';
        title = `${status}: Too Many Requests`;
        if (!serverMessage) message = 'Too Many Requests';
        break;
      case 500:
        title = `${status}: Server Error`;
        if (!serverMessage) message = 'Server Error';
        break;
      case 503:
        type = 'warning';
        title = `${status}: Service Unavailable`;
        if (!serverMessage) message = 'Service Unavailable';
        break;
      default:
        title = `Error ${status}`;
        break;
    }
    return { type, title, message };
  }
}
