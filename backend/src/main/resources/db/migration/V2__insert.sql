INSERT
IGNORE INTO roles (name)
VALUES ('USER'),
       ('MODERATOR'),
       ('ADMIN');

INSERT
IGNORE INTO theater (name, location, capacity, is_deleted, created_at, updated_at)
VALUES ('Thanh Xuân', 'Thanh Xuân', 60, 0, '2025-11-10 08:03:37', '2025-11-10 08:03:37'),
       ('Mỹ Đình', 'Mỹ Đình', 60, 0, '2025-11-10 08:03:51', '2025-11-10 08:03:51'),
       ('Cầu Giấy', 'Cầu Giấy', 60, 0, '2025-11-10 08:04:25', '2025-11-10 08:04:25'),
       ('Hà Đông', 'Hà Đông', 60, 0, '2025-11-10 08:04:50', '2025-11-10 08:04:50'),
       ('Đại Mỗ', 'Đại Mỗ', 60, 0, '2025-11-10 08:05:02', '2025-11-10 08:05:02');

INSERT
IGNORE INTO genre (name, created_at, updated_at)
VALUES ('Khoa Học Viễn Tưởng', '2025-11-10 06:53:07', '2025-11-10 06:53:07'),
       ('Phiêu Lưu', '2025-11-10 06:53:07', '2025-11-10 06:53:07'),
       ('Hành Động', '2025-11-10 06:53:07', '2025-11-10 06:53:07'),
       ('Giả Tượng', '2025-11-10 08:00:18', '2025-11-10 08:00:18'),
       ('Gia Đình', '2025-11-10 08:00:18', '2025-11-10 08:00:18'),
       ('Gây Cấn', '2025-11-10 08:00:45', '2025-11-10 08:00:45'),
       ('Hình Sự', '2025-11-10 08:02:03', '2025-11-10 08:02:03'),
       ('Chính Kịch', '2025-11-10 08:02:03', '2025-11-10 08:02:03'),
       ('Hài', '2025-11-10 08:03:13', '2025-11-10 08:03:13'),
       ('Hoạt Hình', '2025-11-10 08:03:13', '2025-11-10 08:03:13');


INSERT
IGNORE INTO movie (title, poster, overview, duration, release_date, imdb_id, film_id, `language`,
                                  is_deleted, created_at, updated_at)
VALUES ('Superman', 'https://image.tmdb.org/t/p/original//f4hJ5yVSiOSnW9S6vtoGlNYvW5J.jpg',
        'Superman cố gắng can thiệp vào một cuộc khủng hoảng toàn cầu do Lex Luthor gây ra, nhưng lại bị công chúng hiểu lầm. Mọi chuyện trở nên nghiêm trọng hơn khi Luthor tạo ra một bản sao đen tối của Superman – Ultraman. Với sự giúp đỡ của Lois Lane và chú chó siêu năng lực Krypto, Superman phải đối mặt với chính bản ngã của mình để giành lại niềm tin từ nhân loại.',
        130, '2025-07-09', 'tt5950044', '1061474', 'en', 0, '2025-11-10 06:53:07', '2025-11-10 06:53:07'),
       ('Bí Kíp Luyện Rồng', 'https://image.tmdb.org/t/p/original//cLq0IEiC7vvb013UbsCLhS5puX.jpg',
        'Phiên bản Live Action (người đóng) của studio DreamWorks rất được mong chờ đã ra mắt. Câu chuyện về một chàng trai trẻ với ước mơ trở thành thợ săn rồng, nhưng định mệnh lại đưa đẩy anh đến tình bạn bất ngờ với một chú rồng.',
        126, '2025-06-06', 'tt26743210', '1087192', 'en', 0, '2025-11-10 08:00:18', '2025-11-10 08:00:18'),
       ('Cướp Biển Vùng Caribbean: Lời Nguyền Tàu Ngọc Trai Đen',
        'https://image.tmdb.org/t/p/original//z8onk7LV9Mmw6zKz4hT6pzzvmvl.jpg',
        'Khi chiếc thuyền của gia đình cô bé Elizabeth đi qua vùng biển Caribe nhiều cướp biển họ đã cứu được cậu bé Will Turner. Cả gia đình Elizabeth không hề biết rằng Will là William Turner, con trai của một tên trùm cướp biển khét tiếng.  Nhiều năm sau, mọi chuyện dần dần được hé lộ khi Elizabeth bị ngã xuống biển do một tai nạn và được thuyền trưởng Jack Sparrow cứu thoát.',
        143, '2003-07-09', 'tt0325980', '22', 'en', 0, '2025-11-10 08:00:33', '2025-11-10 08:00:33'),
       ('Chiến Binh Tự Do', 'https://image.tmdb.org/t/p/original//8s4bbo4lJGRQL3TBFWNTuuLdX4X.jpg',
        'Dựa theo bộ truyện tranh của Alan Moore, câu chuyện nói về thời tương lai tại Anh quốc, một tên khủng bố với biệt danh V đấu tranh chống lại chính phủ Anh cùng với sự giúp đỡ của một người phụ nữ mang tên Evey, nguời được V cứu sống...Nhân vật chính là V có kế hoạch cho nổ tòa đại hình và nhà quốc hội Anh, nhằm lật đổ đảng chuyên chế đang cầm quyền tại Anh lúc bấy giờ. Anh luôn xuất hiện trong bộ áo choàng đen và chiếc mặt nạ Guy Fawkes - biểu tượng của tư tưởng vô chính phủ.Bối cảnh xảy ra là nước Anh chuyên chế, câu chuyện xoay quanh một phụ nữ trẻ dịu dàng tên Evey Hammond (Natalie Portman). Cô đang muốn có một cuộc sống bình lặng yên ổn sau những biến cố xảy ra trong quá khứ. Một ngày nọ, Evey được cứu thoát ...',
        132, '2006-02-23', 'tt0434409', '752', 'en', 0, '2025-11-10 08:00:45', '2025-11-10 08:00:45'),
       ('Avengers 4: Hồi Kết', 'https://image.tmdb.org/t/p/original//8go3YE9sBMQaCXEx23j6BAfeuxd.jpg',
        'Sau sự kiện hủy diệt tàn khốc, vũ trụ chìm trong cảnh hoang tàn. Với sự trợ giúp của những đồng minh còn sống sót, biệt đội siêu anh hùng Avengers tập hợp một lần nữa để đảo ngược hành động của Thanos và khôi phục lại trật tự của vũ trụ.',
        180, '2019-04-24', 'tt4154796', '299534', 'en', 0, '2025-11-10 08:01:28', '2025-11-10 08:01:28'),
       ('Người Sắt', 'https://image.tmdb.org/t/p/original//qwZiKSoWFevkPSjt21zi3Mgcity.jpg',
        'Tony Stark vừa là chủ tập đoàn công nghệ, vừa là một tay chơi kỳ dị. Trong chuyến thị sát Afghanistan, ông bị nhóm khủng bố bắt cóc. Chúng đòi Tony chế tạo thứ vũ khí hủy diệt để tấn công nước Mỹ.  Nhận ra sự thật phũ phàng rằng, những vũ khí do mình chế tạo đang quay ngược lại tấn công chính mình, Tony bắt tay chế tạo bộ giáp công nghệ cao. Tẩu thoát khỏi nơi giam cầm, Tony trở thành đại diện công lý dưới biệt danh Người sắt. Trong khi đó, người đồng sự trong tập đoàn Stark âm mưu lật đổ Tony.  Bộ phim mở ra câu chuyện trong tương lai về nhóm siêu anh hùng Avenger khi tổ chức bí mật SHIELD bắt đầu lộ diện.',
        126, '2008-04-30', 'tt0371746', '1726', 'en', 0, '2025-11-10 08:01:44', '2025-11-10 08:01:44'),
       ('Người Dơi Bắt Đầu', 'https://image.tmdb.org/t/p/original//ziFjqY3ABYTJZ2kHMzjr3eeyMQZ.jpg',
        'Bruce Wayne thuở nhỏ là một cậu bé mắc chứng sợ dơi và có bố mẹ bị giết bởi tên cướp tên Joe Chill. Lớn lên, nung nấu ý định trả thù, Bruce Wayne rời khỏi thành phố và gặp được Henri Ducard, người truyền dạy cho anh những bí kíp võ thuật. Từ đây, với bản chất lương thiện, Bruce Wayne trở thành người hùng trừ gian diệt ác của thành phố… Với sự tham gia của dàn diễn viên nổi tiếng Christian Bale, Katie Holmes, Gary Oldman và được dàn dựng bởi đạo diễn tài ba Christopher Nolan, chỉ vài tháng sau khi ra mắt, Batman Begins đã được độc giả của tạp chí Empire bầu chọn đứng thứ 36 trong các phim hay nhất mọi thời đại, trở thành một trong những huyền thoại của dòng phim siêu anh hùng.',
        140, '2005-06-10', 'tt0372784', '272', 'en', 0, '2025-11-10 08:02:03', '2025-11-10 08:02:03'),
       ('Ma Trận', 'https://image.tmdb.org/t/p/original//fE7S6EwaBgRQeqKoDVFoImMmAF4.jpg',
        'The Matrix (MaTrận) là bộ phim khoa học giả tưởng về tương lai. Trong tương lai đó, Thomas, biệt danh Neo, là một hacker luôn có cảm giác khác thường về thế giới thực. Cho tới khi người đàn ông tự xưng là Morpheus tìm đến anh. Người đàn ông này nói cho anh biết, thế giới mà mọi người coi là thực chỉ là một chương trình giả lập để máy móc cai trị con người. Chỉ có một người có thể phá được Ma trận. Những người thật sống sót còn lại cuối cùng và những nhân viên Ma trận đều cùng đi tìm người đó. Và người đó chính là anh – Neo…',
        136, '1999-03-31', 'tt0133093', '603', 'en', 0, '2025-11-10 08:02:14', '2025-11-10 08:02:14'),
       ('Sát Thủ John Wick', 'https://image.tmdb.org/t/p/original//ayCZtAXj0ir7Fim57176pNeaLNZ.jpg',
        'John Wick xuất sắc như một tân binh hiện đại. Về cơ bản phim có một tiền đề vô cùng đặc sắc: một tay súng đã nghỉ hưu trở về với một cuộc đời tội phạm, thoạt đầu ta tưởng đó là một tên xã hội đen đã đánh cắp chiếc xe Mustang giết chết con chó của anh ta. Nhưng nếu nhìn nhận kỹ hơn, thực chất John phải vật lộn với một cuộc khủng hoảng hiện hữu sau cái chết của vợ mình. Có thể tất cả là sự phát tiết điên cuồng của John đè nén bấy lâu. Ngay cả kẻ thù của John cũng hiểu điều đó, và họ sợ đến chết khi thấy anh nổi dậy sau ngừng ấy năm.',
        101, '2014-10-22', 'tt2911666', '245891', 'en', 0, '2025-11-10 08:02:33', '2025-11-10 08:02:33'),
       ('Câu Chuyện Lego', 'https://image.tmdb.org/t/p/original//7PMGhfba3G1WGPEuHYH9SFNs1B1.jpg',
        'Phim sẽ theo chân Emmet – một nhân vật LEGO tí hon bình thường bất ngờ bị ngộ nhận là một trong những siêu nhân phi thường đang nắm giữ chìa khóa vận mệnh của cả thế giới. Emmet sẽ bị cuốn vào một đoàn người kỳ lạ trong một chuyến phiêu lưu huyền thoại nhằm ngăn chặn một tên độc tài xấu xa, chuyến đi dở khóc dở cười với vô vàn những tình huống bi hài ngoài dự liệu...',
        100, '2014-02-06', 'tt1490017', '137106', 'en', 0, '2025-11-10 08:03:13', '2025-11-10 08:03:13');
