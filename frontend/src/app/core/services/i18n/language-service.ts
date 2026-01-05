import { inject, Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root',
})
export class LanguageService {
  private translate = inject(TranslateService);

  constructor() {
    this.translate.addLangs(['vi', 'en']);
    this.translate.setFallbackLang('vi');
    this.translate.use('vi');
  }
}
