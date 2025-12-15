import { Component, inject } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTableModule } from 'ng-zorro-antd/table';

import { NzTooltipModule } from 'ng-zorro-antd/tooltip';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { DatePipe } from '@angular/common';
import { NzDrawerModule } from 'ng-zorro-antd/drawer';
import { NzPopconfirmModule } from 'ng-zorro-antd/popconfirm';
import { FormsModule } from '@angular/forms';
import { ArticleService } from '@domain/article/data-access/article.service';
import { Article } from '@domain/article/models/article.model';
import { headerColumns } from '@core/constants/column';
import { ArticleFormComponent } from '@features/admin/article/form/article-form.component';

@Component({
  selector: 'app-article',
  imports: [
    NzButtonModule,
    NzIconModule,
    NzTableModule,
    NzTooltipModule,
    NzTagModule,
    DatePipe,
    NzDrawerModule,
    FormsModule,
    NzPopconfirmModule,
    ArticleFormComponent,
  ],
  templateUrl: './article.component.html',
  styleUrl: './article.component.css',
})
export class ArticleComponent {
  private articleService = inject(ArticleService);
  private msg = inject(NzMessageService);

  articles = this.articleService.articles;
  headerColumn = headerColumns.article;
  drawerVisible = false;
  editingArticle: Article | null = null;

  openDrawer(article?: Article) {
    this.drawerVisible = true;
    this.editingArticle = article || null;
  }

  closeDrawer() {
    this.drawerVisible = false;
    this.editingArticle = null;
  }

  handleSave(formData: any) {
    if (this.editingArticle) {
      this.articleService.update(this.editingArticle.id, formData);
      this.msg.success('Cập nhật bài viết thành công ');
    } else {
      this.articleService.add({ ...formData, author: 'Admin' });
      this.msg.success('Đăng bài viết mới thành công');
    }
    this.closeDrawer();
  }

  deleteArticle(id: string) {
    this.articleService.delete(id);
    this.msg.info('Đã xóa bài viết');
  }
}
