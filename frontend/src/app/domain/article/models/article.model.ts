export interface Article {
  id: string;
  title: string;
  summary: string;
  thumbnail: string;
  content: string; // HTML string từ Quill
  author: string;
  status: 'DRAFT' | 'PUBLISHED';
  category: string;
  views: number;
  createdAt: string;
}
