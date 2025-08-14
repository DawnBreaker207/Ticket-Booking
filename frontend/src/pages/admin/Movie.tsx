// File: src/pages/MoviePage.tsx
import React, { useState, useEffect } from 'react';
import {
    Table, Button, Input, Space, Modal, Form,
    InputNumber, Select, DatePicker, Row, Col,
    Typography, List, Avatar, Popconfirm, Tag, Switch
} from 'antd';
import { DeleteOutlined, EditOutlined, EyeOutlined, RollbackOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import type { ColumnsType } from 'antd/es/table';
import type { Dayjs } from 'dayjs';

const { Title } = Typography;
const { Option } = Select;

interface Movie {
    key: string;
    title: string;
    overview: string;
    genre: string[];
    duration: number;
    language: string;
    releaseDate: string;
    poster?: string;
    deleted?: boolean;
}

interface ApiMovie {
    id: number;
    Title: string;
    Year: string;
    Genre: string;
    imdbID?: string;
    Poster?: string;
}

const apiKey = import.meta.env.VITE_TMDB_API_KEY ?? '';

const apiUrl = 'https://api.themoviedb.org/3';
const imageUrl = 'https://image.tmdb.org/t/p/original';

const MoviePage: React.FC = () => {
    const [data, setData] = useState<Movie[]>([]);
    const [search, setSearch] = useState<string>('');

    const [isModalVisible, setIsModalVisible] = useState<boolean>(false); // used for add/edit/view
    const [isApiModalVisible, setIsApiModalVisible] = useState<boolean>(false);
    const [apiResults, setApiResults] = useState<ApiMovie[]>([]);
    const [editingKey, setEditingKey] = useState<string | null>(null);
    const [mode, setMode] = useState<'add' | 'edit' | 'view' | null>(null);
    const [showDeleted, setShowDeleted] = useState<boolean>(false);

    const [form] = Form.useForm<any>();
    const [genresMap, setGenresMap] = useState<Record<number, string>>({});

    useEffect(() => {
        // fetch genre list once from TMDB so we can map genre_ids -> names (vi-VN)
        const fetchGenres = async () => {
            try {
                const res = await fetch(`${apiUrl}/genre/movie/list?language=vi-VN`, {
                    headers: { Authorization: `Bearer ${apiKey}` }
                });
                if (!res.ok) throw new Error('Failed to fetch genres');
                const json = await res.json();
                const map: Record<number, string> = {};
                (json.genres || []).forEach((g: any) => { map[g.id] = g.name; });
                setGenresMap(map);
            } catch (e) {
                console.error('Genres fetch error', e);
            }
        };
        fetchGenres();
    }, []);

    // by default hide soft-deleted items
    const filtered = data.filter(item => !item.deleted && item.title.toLowerCase().includes(search.toLowerCase()));
    const dataSource = showDeleted ? data : filtered;

    const handleSoftDelete = (key: string) => {
        setData(prev => prev.map(item => item.key === key ? { ...item, deleted: true } : item));
    };

    const handleRestore = (key: string) => {
        setData(prev => prev.map(item => item.key === key ? { ...item, deleted: false } : item));
    };

    const handlePermanentDelete = (key: string) => {
        setData(prev => prev.filter(item => item.key !== key));
    };

    const openAddModal = () => {
        form.resetFields();
        setEditingKey(null);
        setMode('add');
        setIsModalVisible(true);
    };

    const openEditModal = (record: Movie) => {
        form.setFieldsValue({
            title: record.title,
            overview: record.overview,
            genre: record.genre,
            duration: record.duration,
            language: record.language,
            releaseDate: dayjs(record.releaseDate, 'DD/MM/YYYY'),
            poster: record.poster,
        });
        setEditingKey(record.key);
        setMode('edit');
        setIsModalVisible(true);
    };

    const openViewModal = (record: Movie) => {
        form.setFieldsValue({
            title: record.title,
            overview: record.overview,
            genre: record.genre,
            duration: record.duration,
            language: record.language,
            releaseDate: dayjs(record.releaseDate, 'DD/MM/YYYY'),
            poster: record.poster,
        });
        setMode('view');
        setIsModalVisible(true);
    };

    const closeModal = () => {
        form.resetFields();
        setEditingKey(null);
        setMode(null);
        setIsModalVisible(false);
    };

    const openApiModal = () => {
        setApiResults([]);
        setIsApiModalVisible(true);
    };

    const closeApiModal = () => setIsApiModalVisible(false);

    const handleSave = () => {
        // used for both add & edit
        form.validateFields().then(values => {
            const release: Dayjs = values.releaseDate;
            if (mode === 'edit' && editingKey) {
                setData(prev => prev.map(item => {
                    if (item.key !== editingKey) return item;
                    return {
                        ...item,
                        title: values.title,
                        overview: values.overview,
                        genre: values.genre || [],
                        duration: values.duration,
                        language: values.language,
                        releaseDate: release.format('DD/MM/YYYY'),
                        poster: values.poster || item.poster,
                    };
                }));
            } else {
                const nextKey = (data.length + 1).toString();
                const newMovie: Movie = {
                    key: nextKey,
                    title: values.title,
                    overview: values.overview,
                    genre: values.genre || [],
                    duration: values.duration,
                    language: values.language,
                    releaseDate: release.format('DD/MM/YYYY'),
                    poster: values.poster || undefined,
                };
                setData(prev => [...prev, newMovie]);
            }
            closeModal();
        });
    };

    // Use TMDB search endpoint (language=vi-VN)
    const handleApiSearch = async (value: string) => {
        if (!value) return setApiResults([]);
        try {
            const endpoint = `${apiUrl}/search/movie?query=${encodeURIComponent(value)}&language=vi-VN&page=1`;
            const res = await fetch(endpoint, { headers: { Authorization: `Bearer ${apiKey}` } });
            if (!res.ok) throw new Error('TMDB search failed');
            const json = await res.json();
            const results = (json.results || []).map((m: any) => ({
                id: m.id,
                Title: m.title,
                Year: m.release_date ? m.release_date.slice(0, 4) : '',
                Genre: (m.genre_ids || []).map((id: number) => genresMap[id]).filter(Boolean).join(', '),
                imdbID: m.imdb_id,
                Poster: m.poster_path ? `${imageUrl}${m.poster_path}` : undefined,
            }));
            setApiResults(results);
        } catch (e) {
            console.error('TMDB search error', e);
            setApiResults([]);
        }
    };

    const handleApiSelect = async (item: ApiMovie) => {
        try {
            const detailsRes = await fetch(`${apiUrl}/movie/${item.id}?language=vi-VN`, {
                headers: { Authorization: `Bearer ${apiKey}` }
            });
            if (!detailsRes.ok) throw new Error('Failed to fetch movie details');
            const details = await detailsRes.json();

            const runtime: number | undefined = details.runtime; // phút
            const originalLang: string = details.original_language || details.originalLanguage || '';
            const releaseDateStr: string = details.release_date || '';
            const posterUrl = details.poster_path ? `${imageUrl}${details.poster_path}` : item.Poster;

            let langLabel = originalLang;
            if (originalLang === 'en') langLabel = 'Tiếng Anh';
            else if (originalLang === 'vi') langLabel = 'Tiếng Việt';

            form.setFieldsValue({
                title: item.Title,
                overview: details.overview ? details.overview : `${item.Title} (${item.Year})`,
                genre: item.Genre ? item.Genre.split(', ').slice(0, 3) : [],
                poster: posterUrl,
                duration: runtime,
                language: langLabel,
                releaseDate: releaseDateStr ? dayjs(releaseDateStr, 'YYYY-MM-DD') : undefined,
            });
        } catch (e) {
            console.error('Movie details fetch error', e);
            form.setFieldsValue({
                title: item.Title,
                overview: `${item.Title} (${item.Year})`,
                genre: item.Genre ? item.Genre.split(', ').slice(0, 3) : [],
                poster: item.Poster,
            });
        } finally {
            closeApiModal();
            // make sure add/edit modal is open so user can save
            if (!isModalVisible) {
                setMode('add');
                setIsModalVisible(true);
            }
        }
    };

    const columns: ColumnsType<Movie> = [
        {
            title: 'Poster', dataIndex: 'poster', key: 'poster', width: 120,
            render: (poster: string | undefined) => (
                <img
                    src={poster}
                    alt="poster"
                    style={{ width: 96, height: 144, objectFit: 'cover', borderRadius: 4 }}
                />
            ),
        },
        { title: 'Tên phim', dataIndex: 'title', key: 'title' },
        {
            title: 'Thể loại',
            dataIndex: 'genre',
            key: 'genre',
            render: (genre: string[]) => (
                <>
                    {Array.isArray(genre) && genre.map(tag => (
                        <Tag key={tag}>{tag}</Tag>
                    ))}
                </>
            ),
        },
        { title: 'Thời lượng (phút)', dataIndex: 'duration', key: 'duration' },
        { title: 'Ngôn ngữ', dataIndex: 'language', key: 'language' },
        { title: 'Ngày khởi chiếu', dataIndex: 'releaseDate', key: 'releaseDate' },
        {
            title: 'Hành động',
            key: 'action',
            render: (_text, record) => (
                <Space>
                    {!record.deleted ? (
                        <>
                            <Button type="text" icon={<EyeOutlined />} onClick={() => openViewModal(record)} />
                            <Button type="text" icon={<EditOutlined />} onClick={() => openEditModal(record)} />
                            <Popconfirm
                                title={`Xóa phim "${record.title}"?`}
                                onConfirm={() => handleSoftDelete(record.key)}
                                okText="Có"
                                cancelText="Không"
                            >
                                <Button type="text" icon={<DeleteOutlined />} />
                            </Popconfirm>
                        </>
                    ) : (
                        <>
                            <Tag>Đã xóa</Tag>
                            <Button type="text" icon={<RollbackOutlined />} onClick={() => handleRestore(record.key)} />
                            <Popconfirm
                                title={`Xóa hoàn toàn phim "${record.title}"? (không thể khôi phục)`}
                                onConfirm={() => handlePermanentDelete(record.key)}
                                okText="Xóa"
                                cancelText="Hủy"
                            >
                                <Button type="text" danger icon={<DeleteOutlined />} />
                            </Popconfirm>
                        </>
                    )}
                </Space>
            ),
        },
    ];

    const modalTitle = mode === 'view' ? 'Chi tiết Phim' : (mode === 'edit' ? 'Sửa Phim' : 'Thêm Phim mới');

    return (
        <>
            <Space direction="vertical" style={{ width: '100%' }} size="middle">
                <Title level={2}>Quản lý Phim</Title>
                <Row style={{ width: '100%' }} align="middle" gutter={12}>
                    <Col>
                        <Input.Search
                            placeholder="Tìm kiếm phim"
                            allowClear
                            onSearch={value => setSearch(value)}
                            enterButton
                            style={{ width: 400, fontSize: '16px' }}
                        />
                    </Col>
                    <Col flex="auto" />
                    <Col style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                        <span>Hiện phim đã xóa</span>
                        <Switch checked={showDeleted} onChange={setShowDeleted} />
                        <Button type="primary" onClick={openAddModal}>Thêm phim</Button>
                    </Col>
                </Row>

                <Table columns={columns} dataSource={dataSource} rowKey="key" />
            </Space>

            {/* Shared Modal for Add / Edit / View */}
            <Modal
                title={modalTitle}
                open={isModalVisible}
                onCancel={closeModal}
                width={900}
                footer={mode === 'view' ? [<Button key="close" onClick={closeModal}>Đóng</Button>] : undefined}
                okText={mode === 'edit' ? 'Lưu' : 'Thêm'}
                onOk={mode === 'view' ? undefined : handleSave}
            >
                <Form form={form} layout="vertical">
                    <Row gutter={16}>
                        <Col span={8}>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
                                <Form.Item shouldUpdate noStyle>
                                    {() => {
                                        const src = form.getFieldValue('poster');
                                        return (
                                            <div style={{ width: '100%', aspectRatio: '2 / 3', background: '#f5f5f5', borderRadius: 8, overflow: 'hidden' }}>
                                                <img src={src} style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }} />
                                            </div>
                                        );
                                    }}
                                </Form.Item>
                            </div>
                            <div style={{ marginTop: 60 }}></div>
                            <Form.Item label="Poster (URL)" name="poster">
                                <Input placeholder="URL ảnh poster (ví dụ https://...jpg)" disabled={mode === 'view'} />
                            </Form.Item>
                        </Col>
                        <Col span={16}>
                            <Form.Item>
                                <Button type="primary" block onClick={openApiModal} disabled={mode === 'view'}>
                                    Tìm phim từ API
                                </Button>
                            </Form.Item>

                            <Form.Item label="Tên phim" name="title" rules={[{ required: true, message: 'Vui lòng nhập tên phim' }]}
                            >
                                <Input placeholder="Nhập tên phim" disabled={mode === 'view'} />
                            </Form.Item>
                            <Form.Item label="Mô tả" name="overview" rules={[{ required: true, message: 'Vui lòng nhập mô tả' }]}
                            >
                                <Input.TextArea rows={4} disabled={mode === 'view'} />
                            </Form.Item>
                            <Row gutter={12}>
                                <Col span={12}>
                                    <Form.Item label="Thời lượng (phút)" name="duration" rules={[{ required: true, message: 'Vui lòng nhập thời lượng' }]}
                                    >
                                        <InputNumber style={{ width: '100%' }} disabled={mode === 'view'} />
                                    </Form.Item>
                                </Col>
                                <Col span={12}>
                                    <Form.Item label="Ngôn ngữ" name="language" rules={[{ required: true, message: 'Vui lòng nhập ngôn ngữ' }]}
                                    >
                                        <Select placeholder="Chọn ngôn ngữ" disabled={mode === 'view'}>
                                            <Option value="Tiếng Anh">Tiếng Anh</Option>
                                            <Option value="Tiếng Việt">Tiếng Việt</Option>
                                        </Select>
                                    </Form.Item>
                                </Col>
                            </Row>

                            <Form.Item label="Thể loại" name="genre" rules={[{ required: true, message: 'Vui lòng nhập thể loại' }]}
                            >
                                <Select mode="tags" style={{ width: '100%' }} placeholder="Thêm hoặc chọn thể loại" disabled={mode === 'view'} />
                            </Form.Item>

                            <Form.Item label="Ngày khởi chiếu" name="releaseDate" rules={[{ required: true, message: 'Vui lòng nhập ngày khởi chiếu' }]}
                            >
                                <DatePicker style={{ width: '100%' }} format="DD/MM/YYYY" disabled={mode === 'view'} />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>
            <Modal
                title="Tìm phim từ API"
                open={isApiModalVisible}
                footer={null}
                onCancel={closeApiModal}
            >
                <Space direction="vertical" style={{ width: '100%' }}>
                    <Input.Search
                        placeholder="Nhập tên phim API"
                        allowClear
                        enterButton="Tìm"
                        onSearch={handleApiSearch}
                        style={{ width: '100%' }}
                    />
                    <List
                        bordered
                        dataSource={apiResults}
                        renderItem={item => (
                            <List.Item
                                onClick={() => handleApiSelect(item)}
                                style={{ cursor: 'pointer' }}
                            >
                                <List.Item.Meta
                                    avatar={<Avatar shape="square" size={100} src={item.Poster} />}
                                    title={<span style={{ fontWeight: 500 }}>{item.Title}</span>}
                                    description={<span style={{ fontSize: 12, color: '#888' }}>{item.Year} - {item.Genre}</span>}
                                />
                            </List.Item>
                        )}
                        style={{ maxHeight: 400, overflowY: 'auto' }}
                    />
                </Space>
            </Modal>
        </>
    );
};

export default MoviePage;
