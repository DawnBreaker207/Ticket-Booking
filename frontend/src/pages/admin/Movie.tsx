import React, { useState } from 'react';
import {
    Table, Button, Input, Space, Modal, Form,
    InputNumber, Select, DatePicker, Row, Col,
    Typography, List, Avatar, Popconfirm, Tag
} from 'antd';
import { DeleteOutlined, EditOutlined, EyeOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import moment from 'moment';
import type { Moment } from 'moment';
import axios from 'axios';

import type { Movie, ApiMovie } from '../../types/movie';

import { useAppDispatch } from '../../hooks/useAppDispatch';
import { useAppSelector } from '../../hooks/useAppSelector';
import { addMovie, updateMovie, removeMovie } from '../../features/movies/moviesSlice';



const { Title } = Typography;
const { Option } = Select;

const apiKey = import.meta.env.VITE_TMDB_API_KEY ?? '';

const apiUrl = 'https://api.themoviedb.org/3';
const imageUrl = 'https://image.tmdb.org/t/p/original';

const MoviePage: React.FC = () => {
    const dispatch = useAppDispatch();
    const movies = useAppSelector(state => state.movies.items);
    const [search, setSearch] = useState<string>('');

    const [isEditModalVisible, setIsEditModalVisible] = useState<boolean>(false);
    const [isViewModalVisible, setIsViewModalVisible] = useState<boolean>(false);
    const [isApiModalVisible, setIsApiModalVisible] = useState<boolean>(false);
    const [apiResults, setApiResults] = useState<ApiMovie[]>([]);
    const [editingId, setEditingId] = useState<string | null>(null);

    const [form] = Form.useForm<any>();
    const [viewForm] = Form.useForm<any>();

    const [page, setPage] = useState<number>(1);
    const [pageSize, setPageSize] = useState<number>(10);

    const filtered = movies.filter(item => item.title.toLowerCase().includes(search.toLowerCase()));



    const handleDelete = (id: string) => {
        dispatch(removeMovie(id));
    };

    const openAddModal = () => {
        form.resetFields();
        setEditingId(null);
        setIsEditModalVisible(true);
    };

    const openEditModal = (record: Movie) => {
        form.setFieldsValue({
            title: record.title,
            overview: record.overview,
            genre: record.genre,
            duration: record.duration,
            language: record.language,
            releaseDate: moment(record.releaseDate, 'DD/MM/YYYY'),
            poster: record.poster,
        });
        setEditingId(record.id);
        setIsEditModalVisible(true);
    };

    const openViewModal = (record: Movie) => {
        viewForm.setFieldsValue({
            title: record.title,
            overview: record.overview,
            genre: record.genre,
            duration: record.duration,
            language: record.language,
            releaseDate: moment(record.releaseDate, 'DD/MM/YYYY'),
            poster: record.poster,
        });
        setIsViewModalVisible(true);
    };

    const closeEditModal = () => {
        form.resetFields();
        setEditingId(null);
        setIsEditModalVisible(false);
    };

    const closeViewModal = () => {
        viewForm.resetFields();
        setIsViewModalVisible(false);
    };

    const openApiModal = () => {
        setApiResults([]);
        setIsApiModalVisible(true);
    };

    const closeApiModal = () => setIsApiModalVisible(false);

    const handleSave = () => {
        form.validateFields().then(values => {
            const release: Moment = values.releaseDate;
            const moviePayload: Movie = {
                id: editingId ?? Date.now().toString(),
                title: values.title,
                overview: values.overview,
                genre: values.genre || [],
                duration: values.duration,
                language: values.language,
                releaseDate: release.format('DD/MM/YYYY'),
                poster: values.poster || undefined,
            };

            if (editingId) {
                dispatch(updateMovie(moviePayload));
            } else {
                dispatch(addMovie(moviePayload));
            }
            closeEditModal();
        });
    };

    const handleApiSearch = async (value: string) => {
        if (!value) return setApiResults([]);
        try {
            const endpoint = `${apiUrl}/search/movie?query=${encodeURIComponent(value)}&language=vi-VN&page=1`;
            const res = await axios.get(endpoint, { headers: { Authorization: `Bearer ${apiKey}` } });
            const json = res.data;
            const results = (json.results || []).map((m: any) => ({
                id: m.id,
                Title: m.title,
                Year: m.release_date ? m.release_date.slice(0, 4) : '',
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
            const detailsRes = await axios.get(`${apiUrl}/movie/${item.id}?language=vi-VN`, {
                headers: { Authorization: `Bearer ${apiKey}` }
            });
            const details = detailsRes.data;

            const runtime: number | undefined = details.runtime;
            const originalLang: string = details.original_language || details.originalLanguage || '';
            const releaseDateStr: string = details.release_date || '';
            const posterUrl = details.poster_path ? `${imageUrl}${details.poster_path}` : item.Poster;

            let langLabel = originalLang;
            if (originalLang === 'en') langLabel = 'Tiếng Anh';
            else if (originalLang === 'vi') langLabel = 'Tiếng Việt';
            else if (originalLang === 'ja') langLabel = 'Tiếng Nhật';
            else if (originalLang === 'ko') langLabel = 'Tiếng Hàn';
            else if (originalLang === 'zh') langLabel = 'Tiếng Trung';

            const genreNames: string[] = (details.genres || []).map((g: any) => g.name).slice(0, 3);

            form.setFieldsValue({
                title: item.Title,
                overview: details.overview ? details.overview : `${item.Title} (${item.Year})`,
                genre: genreNames,
                poster: posterUrl,
                duration: runtime,
                language: langLabel,
                releaseDate: releaseDateStr ? moment(releaseDateStr, 'YYYY-MM-DD') : undefined,
            });
        } catch (e) {
            console.error('Movie details fetch error', e);
            form.setFieldsValue({
                title: item.Title,
                overview: `${item.Title} (${item.Year})`,
                genre: [],
                poster: item.Poster,
            });
        } finally {
            closeApiModal();
            if (!isEditModalVisible) {
                setEditingId(null);
                setIsEditModalVisible(true);
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
            render: (_text, record) => (<Space>
                <Button
                    type="text"
                    icon={<EyeOutlined style={{ color: '#1890ff', fontSize: 20 }} />}
                    onClick={() => openViewModal(record)}
                />
                <Button
                    type="text"
                    icon={<EditOutlined style={{ color: '#52c41a', fontSize: 20 }} />}
                    onClick={() => openEditModal(record)}
                />
                <Popconfirm
                    title={`Xóa phim "${record.title}"?`}
                    onConfirm={() => handleDelete(record.id)}
                    okText="Xóa"
                    cancelText="Hủy"
                >
                    <Button
                        type="text"
                        icon={<DeleteOutlined style={{ color: '#ff4d4f', fontSize: 20 }} />}
                    />
                </Popconfirm>
            </Space>
            ),
        },
    ];

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
                        <Button type="primary" onClick={openAddModal}>Thêm phim</Button>
                    </Col>
                </Row>

                <Table
                    columns={columns}
                    dataSource={filtered}
                    rowKey="id"
                    pagination={{
                        current: page,
                        pageSize,
                        total: filtered.length,
                        showSizeChanger: true,
                        pageSizeOptions: ['5', '10', '20', '50'],
                        onChange: (p, ps) => { setPage(p); setPageSize(ps); }
                    }}
                />
            </Space>

            {/* Edit / Add Modal */}
            <Modal
                title={editingId ? 'Sửa Phim' : 'Thêm Phim mới'}
                open={isEditModalVisible}
                onCancel={closeEditModal}
                width={900}
                okText={editingId ? 'Lưu' : 'Thêm'}
                onOk={handleSave}
            >
                <Form form={form} layout="vertical">
                    <Row gutter={16}>
                        <Col span={8}>
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
                            <div style={{ marginTop: 56 }} />
                            <Form.Item label="Poster (URL)" name="poster">
                                <Input placeholder="URL ảnh poster (ví dụ https://...jpg)" />
                            </Form.Item>
                        </Col>
                        <Col span={16}>
                            <Form.Item>
                                <Button type="primary" block onClick={openApiModal}>
                                    Tìm phim từ API
                                </Button>
                            </Form.Item>

                            <Form.Item label="Tên phim" name="title" rules={[{ required: true, message: 'Vui lòng nhập tên phim' }]}>
                                <Input placeholder="Nhập tên phim" />
                            </Form.Item>
                            <Form.Item label="Mô tả" name="overview" rules={[{ required: true, message: 'Vui lòng nhập mô tả' }]}>
                                <Input.TextArea rows={4} />
                            </Form.Item>
                            <Row gutter={12}>
                                <Col span={12}>
                                    <Form.Item label="Thời lượng (phút)" name="duration" rules={[{ required: true, message: 'Vui lòng nhập thời lượng' }]}>
                                        <InputNumber style={{ width: '100%' }} />
                                    </Form.Item>
                                </Col>
                                <Col span={12}>
                                    <Form.Item label="Ngôn ngữ" name="language" rules={[{ required: true, message: 'Vui lòng nhập ngôn ngữ' }]}>
                                        <Select placeholder="Chọn ngôn ngữ">
                                            <Option value="Tiếng Anh">Tiếng Anh</Option>
                                            <Option value="Tiếng Việt">Tiếng Việt</Option>
                                            <Option value="Tiếng Nhật">Tiếng Nhật</Option>
                                            <Option value="Tiếng Hàn">Tiếng Hàn</Option>
                                            <Option value="Tiếng Trung">Tiếng Trung</Option>
                                        </Select>
                                    </Form.Item>
                                </Col>
                            </Row>

                            <Form.Item label="Thể loại" name="genre" rules={[{ required: true, message: 'Vui lòng nhập thể loại' }]}>
                                <Select mode="tags" style={{ width: '100%' }} placeholder="Thêm hoặc chọn thể loại" />
                            </Form.Item>

                            <Form.Item label="Ngày khởi chiếu" name="releaseDate" rules={[{ required: true, message: 'Vui lòng nhập ngày khởi chiếu' }]}>
                                <DatePicker style={{ width: '100%' }} format="DD/MM/YYYY" />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            {/* View detail Modal */}
            <Modal
                title="Chi tiết Phim"
                open={isViewModalVisible}
                onCancel={closeViewModal}
                width={900}
                footer={[<Button key="close" onClick={closeViewModal}>Đóng</Button>]}
            >
                <Form form={viewForm} layout="vertical">
                    <Row gutter={16}>
                        <Col span={8}>
                            <Form.Item shouldUpdate noStyle>
                                {() => {
                                    const src = viewForm.getFieldValue('poster');
                                    return (
                                        <div style={{ width: '100%', aspectRatio: '2 / 3', background: '#f5f5f5', borderRadius: 8, overflow: 'hidden' }}>
                                            <img src={src} style={{ width: '100%', height: '100%', objectFit: 'cover', display: 'block' }} />
                                        </div>
                                    );
                                }}
                            </Form.Item>
                            <div style={{ marginTop: 16 }} />
                            <Form.Item label="Poster">
                                <Input readOnly value={viewForm.getFieldValue('poster') || ''} />
                            </Form.Item>
                        </Col>
                        <Col span={16}>
                            <Form.Item label="Tên phim">
                                <Input readOnly value={viewForm.getFieldValue('title') || ''} />
                            </Form.Item>

                            <Form.Item label="Mô tả">
                                <Input.TextArea rows={4} readOnly value={viewForm.getFieldValue('overview') || ''} />
                            </Form.Item>

                            <Row gutter={12}>
                                <Col span={12}>
                                    <Form.Item label="Thời lượng (phút)">
                                        <Input readOnly value={
                                            (() => {
                                                const v = viewForm.getFieldValue('duration');
                                                return v !== undefined && v !== null ? String(v) : '';
                                            })()
                                        } />
                                    </Form.Item>
                                </Col>
                                <Col span={12}>
                                    <Form.Item label="Ngôn ngữ">
                                        <Input readOnly value={viewForm.getFieldValue('language') || ''} />
                                    </Form.Item>
                                </Col>
                            </Row>

                            <Form.Item label="Thể loại">
                                <div>
                                    {Array.isArray(viewForm.getFieldValue('genre')) && viewForm.getFieldValue('genre').length > 0 &&
                                        (viewForm.getFieldValue('genre') as string[]).map(g => <Tag key={g}>{g}</Tag>)
                                    }
                                </div>
                            </Form.Item>

                            <Form.Item label="Ngày khởi chiếu">
                                <Input
                                    readOnly
                                    value={
                                        (() => {
                                            const rd = viewForm.getFieldValue('releaseDate');
                                            if (!rd) return '';
                                            return moment.isMoment(rd) ? rd.format('DD/MM/YYYY') : moment(rd).format('DD/MM/YYYY');
                                        })()
                                    }
                                />
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </Modal>

            {/* API Search Modal */}
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
                                    description={<span style={{ fontSize: 12, color: '#888' }}>{item.Year}</span>}
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
