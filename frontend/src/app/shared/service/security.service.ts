import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SecurityService {
  disableInspect() {
    // Disable right click
    document.addEventListener('contextmenu', (e) => e.preventDefault());

    // Disable shortcut
    document.addEventListener('keydown', (e: KeyboardEvent) => {
      // Support Mac and Win
      const isCmdOrCtrl = e.ctrlKey || e.metaKey;
      const key = e.key.toLowerCase();

      const forbiddenKeys = ['f12', 'u', 's'];
      const forbiddenInspectKeys = ['i', 'j', 'c'];
      if (
        forbiddenKeys.includes(key) ||
        (isCmdOrCtrl && e.shiftKey && forbiddenInspectKeys.includes(key)) ||
        (isCmdOrCtrl && key === 'u')
      ) {
        console.log('Hello world');
        e.preventDefault();
        e.stopPropagation();
      }
    });
  }
}
