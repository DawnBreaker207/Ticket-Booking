import { TestBed } from '@angular/core/testing';
import { CanActivateChildFn } from '@angular/router';

import { AdminGuard } from './admin.guard';

describe('AdminGuard', () => {
  const executeGuard: CanActivateChildFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => AdminGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
