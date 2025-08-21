import {Component, inject} from '@angular/core';
import {Router} from '@angular/router';
import {NzButtonComponent} from "ng-zorro-antd/button";
import {NzResultComponent, NzResultExtraDirective} from "ng-zorro-antd/result";
import {SvgIconComponent} from "angular-svg-icon";

@Component({
  selector: 'app-error-500',
    imports: [
        NzButtonComponent,
        NzResultComponent,
        NzResultExtraDirective,
        SvgIconComponent
    ],
  templateUrl: './error-500.component.html',
  styleUrl: './error-500.component.css'
})
export class Error500Component {
  private router = inject(Router);

  goToHomePage() {
    this.router.navigate(['/']);
  }
}
