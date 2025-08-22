import {Component} from '@angular/core';
import {UpperCasePipe} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-header',
  imports: [
    UpperCasePipe,
    RouterLink
  ],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent {
  headers = [
    {name: "Lịch chiếu rạp", path: ""},
    {name: "Phim", path: ""},
    {name: " Rạp", path: ""},
    {name: "Giá vé", path: ""},
    {name: "Tin mới và ưu đãi", path: ""},
    {name: "Nhượng quyền", path: ""},
    {name: "Thành viên", path: "/home/login"}


  ];
}
