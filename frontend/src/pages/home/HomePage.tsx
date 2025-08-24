// MovieGrid.tsx
import React from 'react';
import { Row, Col, Card, Button, Typography, Image, Layout } from 'antd';
import Navbar from '../../components/Navbar';
const { Title, Text } = Typography;
import type { Movie as MovieType } from '../../types/movie';

const { Content } = Layout;

const movies: MovieType[] = [
    {
        id: '1',
        poster: 'https://motchilllo.net/wp-content/uploads/2025/08/thanh-guom-diet-quy-vo-han-thanh-19930-thumb.webp',
        title: 'Thanh gươm diệt quỷ: Vô hạn thanh',
        genre: ['Action', 'Adventure'],
        duration: 120,
    },
    {
        id: '2',
        poster: 'https://files.betacorp.vn/media%2fimages%2f2025%2f07%2f10%2fscreenshot%2D2025%2D07%2D10%2D120703%2D130606%2D100725%2D11.png',
        title: 'connan',
        genre: ['Comedy', 'Drama'],
        duration: 90,
    },
    {
        id: '3',
        poster: 'https://files.betacorp.vn/media%2fimages%2f2025%2f07%2f21%2f400x633%2D1%2D160944%2D210725%2D16.jpg',
        title: 'Mang mẹ đi bỏ',
        genre: ['Sci-Fi', 'Thriller'],
        duration: 150,
    },
    {
        id: '4',
        poster: 'https://files.betacorp.vn/media%2fimages%2f2025%2f07%2f21%2f400x633%2D1%2D160944%2D210725%2D16.jpg',
        title: 'Movie Title 4',
        genre: ['Horror'],
        duration: 110,
    },
    {
        id: '5',
        poster: 'https://files.betacorp.vn/media%2fimages%2f2025%2f07%2f21%2f400x633%2D1%2D160944%2D210725%2D16.jpg',
        title: 'Movie Title 4',
        genre: ['Horror'],
        duration: 110,
    },
];

const HomePage: React.FC = () => {
    return (
        <Layout style={{ minHeight: "100vh" }}>
            <Navbar />
            <Content style={{ maxWidth: '1200px', marginRight: 'auto', marginLeft: 'auto', paddingTop: '30px' }}>
                <Title level={2} style={{ textAlign: 'center', margin: '24px 0' }}>
                    PHIM ĐANG CHIẾU
                </Title>
                <Row gutter={[16, 16]}>
                    {movies.map((movie) => (
                        <Col xs={24} sm={12} md={9} lg={6} key={movie.id} style={{ display: 'flex', justifyContent: 'center' }}>
                            <Card
                                cover={
                                    <Image
                                        alt={movie.title}
                                        src={movie.poster}
                                        preview={false}
                                        style={{ width: '100%', height: 360, objectFit: 'cover', display: 'block', borderRadius: 8 }}
                                    />
                                }
                                style={{
                                    width: 227,
                                    height: 570,
                                    display: 'flex',
                                    flexDirection: 'column',
                                    overflow: 'hidden',
                                    border: 'none',
                                    boxShadow: 'none',
                                    background: 'transparent',
                                }}
                                styles={{
                                    body: {
                                        padding: 12,
                                        display: 'flex',
                                        flexDirection: 'column',
                                        flex: 1,
                                    },
                                }}
                            >
                                <div>
                                    <Title level={4} style={{ margin: 0, padding: 0, lineHeight: 1.2 }}>
                                        {movie.title}
                                    </Title>
                                    <Text style={{ display: 'block', marginTop: 6 }}>
                                        <b>Thể loại:</b> {movie.genre.join(', ')}
                                    </Text>
                                    <Text style={{ display: 'block', marginTop: 4 }}>
                                        <b>Thời lượng:</b> {movie.duration}
                                    </Text>
                                </div>
                                <div style={{ marginTop: 'auto' }}>
                                    <Button type="primary" style={{ width: '100%', padding: '6px 12px', marginTop: 8 }}>
                                        MUA VÉ
                                    </Button>
                                </div>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Content>
        </Layout>
    );
};

export default HomePage;