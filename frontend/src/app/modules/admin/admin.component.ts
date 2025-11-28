import { Component, inject, OnInit } from '@angular/core';
import {
  NzContentComponent,
  NzFooterComponent,
  NzHeaderComponent,
  NzLayoutModule,
  NzSiderComponent,
} from 'ng-zorro-antd/layout';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  RouterLink,
  RouterOutlet,
} from '@angular/router';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { ProfileMenuComponent } from '@/app/shared/components/profile-menu/profile-menu.component';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { NzBadgeModule } from 'ng-zorro-antd/badge';
import { NzBreadCrumbModule } from 'ng-zorro-antd/breadcrumb';
import { filter } from 'rxjs';

interface Breadcrumb {
  label: string;
  url: string;
}

@Component({
  selector: 'app-admin',
  imports: [
    NzContentComponent,
    NzHeaderComponent,
    NzIconModule,
    NzLayoutModule,
    NzMenuModule,
    NzSiderComponent,
    RouterLink,
    RouterOutlet,
    NzFooterComponent,
    NzAvatarModule,
    ProfileMenuComponent,
    NzDropDownModule,
    NzBadgeModule,
    NzBreadCrumbModule,
  ],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',
})
export class AdminComponent implements OnInit {
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  ngOnInit() {
    // Tạo breadcrumb lần đầu
    this.breadcrumbs = this.createBreadcrumbs(this.activatedRoute.root);

    // Lắng nghe sự kiện đổi route để cập nhật lại breadcrumb
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        this.breadcrumbs = this.createBreadcrumbs(this.activatedRoute.root);
      });
  }

  breadcrumbs: Breadcrumb[] = [];
  isCollapsed = false;
  protected readonly date = new Date();

  toggleCollapse() {
    this.isCollapsed = !this.isCollapsed;
  }

  // Hàm đệ quy để duyệt cây Router và lấy data.breadcrumb
  private createBreadcrumbs(
    route: ActivatedRoute,
    url: string = '',
    breadcrumbs: Breadcrumb[] = [],
  ): Breadcrumb[] {
    const children: ActivatedRoute[] = route.children;

    if (children.length === 0) {
      return breadcrumbs;
    }

    for (const child of children) {
      const routeURL: string = child.snapshot.url
        .map((segment) => segment.path)
        .join('/');

      if (routeURL !== '') {
        url += `/${routeURL}`;
      }

      // Chỉ lấy những route có data.breadcrumb
      const label = child.snapshot.data['breadcrumb'];
      if (label) {
        breadcrumbs.push({ label, url });
      }

      return this.createBreadcrumbs(child, url, breadcrumbs);
    }
    return breadcrumbs;
  }
}
