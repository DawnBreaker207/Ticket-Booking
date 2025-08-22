import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {ToastService} from '@/app/shared/services/toast.service';
import {catchError, delay, mergeMap, of, throwError} from 'rxjs';

export const ErrorInterceptor: HttpInterceptorFn = (req, next) => {
  const toast = inject(ToastService);
  let retryCount = 0;
  const maxRetry = 3;
  const retryStatus = [429, 503];
  const handleRequest = () => {


    return next(req).pipe(
      catchError((error: HttpErrorResponse) => {
        switch (error.status) {
          case 400:
            toast.createNotification({
              type: 'error',
              title: `${error.status}:Bad Request`,
              message: 'Bad Request'
            });
            break;
          case 401:
            toast.createNotification({
              type: 'error',
              title: `${error.status}: Unauthorized`,
              message: 'Unauthorized'
            });
            break;
          case 403:
            toast.createNotification({
              type: 'warning',
              title: `${error.status}: Forbidden`,
              message: 'Forbidden'
            });
            break;
          case 404:
            toast.createNotification({
              type: 'error',
              title: `${error.status}: Not Found`,
              message: 'Not Found'
            });
            break;
          case 500:
            toast.createNotification({
              type: 'error',
              title: `${error.status}: Server Error`,
              message: 'Server Error'
            });
            break;
          case 429:
            toast.createNotification({
              type: 'warning',
              title: `${error.status}: Too Many Requests`,
              message: 'Too Many Requests'
            });
            break;
          case 503:
            toast.createNotification({
              type: 'warning',
              title: `${error.status}: Service Unavailable`,
              message: 'Service Unavailable'
            });
            break;
          default:
            toast.createNotification({
              type: 'error',
              title: `Error ${error.status}`,
              message: 'Error'
            });
        }


        if (retryCount < maxRetry && retryStatus.includes(error.status)) {
          retryCount++;
          const backoff = Math.pow(2, retryCount) * 1000;
          console.log(`Retry #${retryCount} for ${error.status} after ${backoff}ms`);
          return of(error).pipe(
            delay(backoff),
            mergeMap(() => next(req))
          )
        }
        return throwError(() => error);


      })
    );
  }
  return handleRequest();
};
