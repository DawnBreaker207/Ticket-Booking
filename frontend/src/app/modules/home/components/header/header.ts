import {Component} from '@angular/core';
import {UpperCasePipe} from '@angular/common';
import {RouterLink} from '@angular/router';
import {NzButtonComponent} from 'ng-zorro-antd/button';
import {NzMenuDirective} from 'ng-zorro-antd/menu';

@Component({
  selector: 'app-header',
  imports: [
    UpperCasePipe,
    RouterLink,
    NzButtonComponent,
    NzMenuDirective,

  ],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent {
  headers = [
    {name: "Lịch chiếu rạp", path: ""},
    {name: "Phim", path: "/home"},
    {name: " Rạp", path: ""},
    {name: "Giá vé", path: ""},
    {name: "Tin mới và ưu đãi", path: ""},
    {name: "Nhượng quyền", path: ""},
    {name: "Thành viên", path: "/login"}


  ];
}
