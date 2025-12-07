import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { NzResultModule } from 'ng-zorro-antd/result';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-error-404',
  imports: [CommonModule, NzResultModule, NzButtonModule, NzIconModule],
  templateUrl: './error-404.component.html',
  styleUrl: './error-404.component.css',
})
export class Error404Component {
  private router = inject(Router);

  goToHomePage() {
    this.router.navigate(['/']);
  }
}
