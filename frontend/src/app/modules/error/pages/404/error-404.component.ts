import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { NzResultModule } from 'ng-zorro-antd/result';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { AngularSvgIconModule } from 'angular-svg-icon';

@Component({
  selector: 'app-error-404',
  imports: [NzResultModule, NzButtonComponent, AngularSvgIconModule],
  templateUrl: './error-404.component.html',
  styleUrl: './error-404.component.css',
})
export class Error404Component {
  private router = inject(Router);

  goToHomePage() {
    this.router.navigate(['/']);
  }
}
