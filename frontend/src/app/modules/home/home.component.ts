import {Component} from '@angular/core';
import {FooterComponent} from "@/app/modules/home/components/footer/footer";
import {HeaderComponent} from "@/app/modules/home/components/header/header";
import {NzContentComponent, NzFooterComponent, NzHeaderComponent, NzLayoutComponent} from "ng-zorro-antd/layout";
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [
    FooterComponent,
    HeaderComponent,
    RouterOutlet,
    NzContentComponent,
    NzFooterComponent,
    NzHeaderComponent,
    NzLayoutComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
