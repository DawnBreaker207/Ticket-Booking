import { Component, inject } from '@angular/core';
import { ArticleService } from '@/app/core/services/article/article.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { Article } from '@/app/core/models/article.model';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzTableModule } from 'ng-zorro-antd/table';
import { headerColumns } from '@/app/core/constants/column';
import { NzTooltipModule } from 'ng-zorro-antd/tooltip';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { DatePipe } from '@angular/common';
import { NzDrawerModule } from 'ng-zorro-antd/drawer';
import { NzPopconfirmModule } from 'ng-zorro-antd/popconfirm';
import { FormsModule } from '@angular/forms';
import { FormComponent } from '@/app/modules/admin/components/article/form/form.component';

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
    FormComponent,
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
