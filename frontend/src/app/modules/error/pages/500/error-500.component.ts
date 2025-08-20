import {Component, inject} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-error-500',
  imports: [],
  templateUrl: './error-500.component.html',
  styleUrl: './error-500.component.css'
})
export class Error500Component {
  private router = inject(Router);

  goToHomePage() {
    this.router.navigate(['/']);
  }
}
