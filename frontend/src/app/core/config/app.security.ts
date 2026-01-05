import { inject, isDevMode, provideAppInitializer } from '@angular/core';
import { SecurityService } from '@shared/service/security.service';

export function provideAppSecurity() {
  return [
    provideAppInitializer(() => {
      const securityService = inject(SecurityService);
      if (!isDevMode()) {
        securityService.disableInspect();
      }
    }),
  ];
}
