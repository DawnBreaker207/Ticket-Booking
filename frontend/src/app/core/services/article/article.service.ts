import { Injectable, signal } from '@angular/core';
import { Article } from '@/app/core/models/article.model';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  //  Mock Data
  private mockArticles: Article[] = [
    {
      id: '1',
      title: 'Review: Dune 2 - Kiệt tác điện ảnh thập kỷ',
      summary:
        'Phần 2 của Dune không chỉ thỏa mãn thị giác mà còn mở rộng sâu sắc thế giới chính trị...',
      thumbnail:
        'https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg',
      content: '<p>Nội dung chi tiết bài viết ở đây...</p>',
      author: 'Admin',
      status: 'PUBLISHED',
      category: 'Review Phim',
      views: 12500,
      createdAt: new Date().toISOString(),
    },
    {
      id: '2',
      title: 'Top 5 phim bom tấn ra mắt tháng 4',
      summary:
        'Điểm danh những cái tên không thể bỏ lỡ tại rạp chiếu tháng tới.',
      thumbnail:
        'https://image.tmdb.org/t/p/w500/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg',
      content: '<p>Danh sách phim...</p>',
      author: 'Editor',
      status: 'DRAFT',
      category: 'Tin tức',
      views: 0,
      createdAt: new Date().toISOString(),
    },
  ];

  articles = signal<Article[]>(this.mockArticles);

  add(article: Omit<Article, 'id' | 'views' | 'createdAt'>) {
    const newArticle: Article = {
      ...article,
      id: Math.random().toString(36).substr(2, 9),
      views: 0,
      createdAt: new Date().toISOString(),
    };
    this.articles.update((list) => [newArticle, ...list]);
  }

  update(id: string, updateData: Partial<Article>) {
    this.articles.update((list) =>
      list.map((item) => (item.id === id ? { ...item, ...updateData } : item)),
    );
  }

  delete(id: string) {
    this.articles.update((list) => list.filter((item) => item.id !== id));
  }
}
