import { Component } from '@angular/core';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzMenuDirective } from 'ng-zorro-antd/menu';
import { NzFooterComponent } from 'ng-zorro-antd/layout';

@Component({
  selector: 'app-footer',
  imports: [NzIconModule, NzMenuDirective, NzMenuDirective, NzFooterComponent],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css',
})
export class FooterComponent {
  protected readonly date = new Date();
}
