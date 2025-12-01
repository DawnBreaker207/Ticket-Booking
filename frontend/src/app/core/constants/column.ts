export const headerColumns = {
  movie: ['Poster', 'Info', 'Genre', 'Status', 'Action'],
  theater: [
    '#',
    'Name',
    'Location',
    'Capacity',
    'Showtime',
    'Day Created',
    'Action',
  ],
  showtime: [
    '#',
    'Movie',
    'Poster',
    'Showtime',
    'Show date',
    'Theater',
    'Location',
    'Seat Available',
    'Action',
  ],
  reservation: ['#', 'Status', 'Total', 'Day Create', 'Action'],
  user: ['Info', 'Status', 'Action'],
  article: [
    'Ảnh',
    'Tiêu đề',
    'Danh mục',
    'Tác giả',
    'Trạng thái',
    'Ngày tạo',
    'Action',
  ],
};

export const displayColumns = {
  movie: [
    'id',
    'title',
    'poster',
    'overview',
    'genre',
    'duration',
    'release',
    'action',
  ],
};
