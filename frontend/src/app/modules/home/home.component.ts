import { Component } from '@angular/core';
import { FooterComponent } from '@/app/modules/home/components/footer/footer.component';
import { HeaderComponent } from '@/app/modules/home/components/header/header.component';
import {
  NzContentComponent,
  NzFooterComponent,
  NzLayoutComponent,
} from 'ng-zorro-antd/layout';
import { RouterOutlet } from '@angular/router';
import { NzMenuModule } from 'ng-zorro-antd/menu';

@Component({
  selector: 'app-home',
  imports: [
    FooterComponent,
    HeaderComponent,
    RouterOutlet,
    NzContentComponent,
    NzFooterComponent,
    NzLayoutComponent,
    NzMenuModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {}
