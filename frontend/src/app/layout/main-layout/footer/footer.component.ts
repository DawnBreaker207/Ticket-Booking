import { Component } from '@angular/core';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { NzFooterComponent } from 'ng-zorro-antd/layout';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-footer',
  imports: [
    NzIconModule,
    NzMenuDirective,
    NzMenuDirective,
    NzFooterComponent,
    TranslatePipe,
  ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css',
})
export class FooterComponent {
  protected readonly date = new Date();
}
