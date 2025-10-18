import { Component, effect, input, signal } from '@angular/core';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { isObservable } from 'rxjs';

@Component({
  selector: 'app-loading',
  imports: [NzSpinModule],
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.css',
})
export class LoadingComponent {
  tip = input<string>('Loading...');
  size = input<'small' | 'default' | 'large'>('default');
  delay = input<number>();
  loading = signal(false);
  asyncData = input<Promise<any> | import('rxjs').Observable<any> | null>(null);

  constructor() {
    effect(() => {
      const data = this.asyncData();
      const delayMs = this.delay() ?? 0;

      if (!data) {
        this.loading.set(false);
        return;
      }

      this.loading.set(false);
      setTimeout(() => {
        this.loading.set(true);

        if (isObservable(data)) {
          data.subscribe({
            next: () => {},
            error: () => this.loading.set(false),
            complete: () => this.loading.set(false),
          });
        } else if (data instanceof Promise) {
          data.finally(() => this.loading.set(false));
        }
      }, delayMs);
    });
  }
}
