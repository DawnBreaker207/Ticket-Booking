import {Component} from '@angular/core';
import {UpperCasePipe} from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [
    UpperCasePipe
  ],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class HeaderComponent {
  headers: string[] = ["Lịch chiếu rạp", "Phim", "Rạp", "Giá vé", "Tin mới và ưu đãi", "Nhượng quyền", "Thành viên"];
}
