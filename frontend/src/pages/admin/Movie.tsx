import React, { useState } from 'react';
import {
    Table, Button, Input, Space, Modal, Form,
    InputNumber, Select, DatePicker, Row, Col,
    Typography, List, Avatar, Popconfirm, Tag
} from 'antd';
import { DeleteOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import type { Dayjs } from 'dayjs';

const { Title } = Typography;
const { Option } = Select;

interface Movie {
    key: string;
    title: string;
    description: string;
    director: string;
    tags: string[];
    duration: number;
    language: string;
    releaseDate: string;
}

interface ApiMovie {
    Title: string;
    Year: string;
    Genre: string;
    imdbID: string;
    Type: string;
    Poster?: string;
}

// mock data
const fakeApiData: ApiMovie[] = [
    { Title: 'Interstellar', Year: '2014', Genre: 'Sci-Fi', imdbID: 'tt0816692', Type: 'movie', Poster: 'https://via.placeholder.com/48?text=Inter' },
    { Title: 'Dunkirk', Year: '2017', Genre: 'War', imdbID: 'tt5013056', Type: 'movie', Poster: 'https://via.placeholder.com/48?text=Dunk' },
    { Title: 'Tenet', Year: '2020', Genre: 'Action', imdbID: 'tt6723592', Type: 'movie', Poster: 'https://via.placeholder.com/48?text=Tenet' },
    { Title: 'Memento', Year: '2000', Genre: 'Thriller', imdbID: 'tt0209144', Type: 'movie', Poster: 'https://via.placeholder.com/48?text=Meme' },
    { Title: 'The Prestige', Year: '2006', Genre: 'Drama', imdbID: 'tt0482571', Type: 'movie', Poster: 'https://via.placeholder.com/48?text=Prest' },
];

const initialData: Movie[] = [
    {
        key: '1', title: 'Inception', description: 'A mind-bending thriller by Christopher Nolan.',
        director: 'Christopher Nolan', tags: ['Sci-Fi'], duration: 148,
        language: 'English', releaseDate: '16/07/2010',
    },
    {
        key: '2', title: 'The Dark Knight', description: 'Batman faces the Joker in Gotham City.',
        director: 'Christopher Nolan', tags: ['Action'], duration: 152,
        language: 'English', releaseDate: '18/07/2008',
    },
];

const MoviePage: React.FC = () => {
    const [data, setData] = useState<Movie[]>(initialData);
    const [search, setSearch] = useState<string>('');

    const [isAddModalVisible, setIsAddModalVisible] = useState<boolean>(false);
    const [isApiModalVisible, setIsApiModalVisible] = useState<boolean>(false);
    const [apiResults, setApiResults] = useState<ApiMovie[]>([]);
    const [form] = Form.useForm<any>();

    const filtered = data.filter(item =>
        item.title.toLowerCase().includes(search.toLowerCase())
    );

    const handleDelete = (key: string) => {
        setData(prev => prev.filter(item => item.key !== key));
    };

    const columns: ColumnsType<Movie> = [
        { title: 'Tên phim', dataIndex: 'title', key: 'title' },
        { title: 'Mô tả', dataIndex: 'description', key: 'description' },
        { title: 'Đạo diễn', dataIndex: 'director', key: 'director' },
        {
            title: 'Thể loại',
            dataIndex: 'tags',
            key: 'tags',
            render: (tags: string[]) => (
                <>
                    {Array.isArray(tags) && tags.map(tag => (
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
                <Popconfirm
                    title={`Xóa phim "${record.title}"?`}
                    onConfirm={() => handleDelete(record.key)}
                    okText="Có"
                    cancelText="Không"
                >
                    <Button type="text" icon={<DeleteOutlined />} />
                </Popconfirm>
            ),
        },
    ];

    const openAddModal = () => {
        form.resetFields();
        setIsAddModalVisible(true);
    };

    const closeAddModal = () => setIsAddModalVisible(false);

    const openApiModal = () => {
        setApiResults([]);
        setIsApiModalVisible(true);
    };

    const closeApiModal = () => setIsApiModalVisible(false);

    const handleAddOk = () => {
        form.validateFields().then(values => {
            const nextKey = (data.length + 1).toString();
            const release: Dayjs = values.releaseDate;
            const newMovie: Movie = {
                key: nextKey,
                title: values.title,
                description: values.description,
                director: values.director,
                tags: values.tags || [],
                duration: values.duration,
                language: values.language,
                releaseDate: release.format('DD/MM/YYYY'),
            };
            setData(prev => [...prev, newMovie]);
            closeAddModal();
        });
    };

    const handleApiSearch = (value: string) => {
        const results = fakeApiData.filter(item =>
            item.Title.toLowerCase().includes(value.toLowerCase())
        );
        setApiResults(results);
    };

    const handleApiSelect = (item: ApiMovie) => {

        form.setFieldsValue({
            title: item.Title,
            description: `${item.Title} (${item.Year})`,
            genre: item.Genre,
        });
        closeApiModal();
    };

    return (
        <>
            <Space direction="vertical" style={{ width: '100%' }} size="middle">
                <Title level={2}>Quản lý Phim</Title>
                <Input.Search
                    placeholder="Tìm kiếm phim"
                    allowClear
                    onSearch={value => setSearch(value)}
                    enterButton
                    style={{ width: 400, fontSize: '16px' }}
                />
                <Row justify="end"><Col><Button type="primary" onClick={openAddModal}>Thêm phim</Button></Col></Row>
                <Table columns={columns} dataSource={filtered} />
            </Space>
            <Modal
                title="Thêm Phim mới"
                open={isAddModalVisible}
                onOk={handleAddOk}
                onCancel={closeAddModal}
                okText="Thêm"
            >
                <Form form={form} layout="vertical">
                    <Form.Item>
                        <Button type="primary" onClick={openApiModal}>
                            Tìm phim từ API
                        </Button>
                    </Form.Item>

                    <Form.Item label="Tên phim" name="title" rules={[{ required: true, message: 'Vui lòng nhập tên phim' }]}>
                        <Input placeholder="Nhập tên phim" />
                    </Form.Item>
                    <Form.Item label="Mô tả" name="description" rules={[{ required: true, message: 'Vui lòng nhập mô tả' }]}>
                        <Input.TextArea rows={3} />
                    </Form.Item>
                    <Form.Item label="Đạo diễn" name="director" rules={[{ required: true, message: 'Vui lòng nhập đạo diễn' }]}>
                        <Input />
                    </Form.Item>
                    <Form.Item label="Thể loại" name="tags" rules={[{ required: true, message: 'Vui lòng nhập thể loại' }]}>
                        <Select mode="tags" style={{ width: '100%' }} placeholder="Thêm hoặc chọn thể loại" />
                    </Form.Item>
                    <Form.Item label="Thời lượng (phút)" name="duration" rules={[{ required: true, message: 'Vui lòng nhập thời lượng' }]}>
                        <InputNumber style={{ width: '100%' }} />
                    </Form.Item>
                    <Form.Item label="Ngôn ngữ" name="language" rules={[{ required: true, message: 'Vui lòng nhập ngôn ngữ' }]}>
                        <Select placeholder="Chọn ngôn ngữ">
                            <Option value="Tiếng Anh">Tiếng Anh</Option>
                            <Option value="Tiếng Việt">Tiếng Việt</Option>
                        </Select>
                    </Form.Item>
                    <Form.Item label="Ngày khởi chiếu" name="releaseDate" rules={[{ required: true, message: 'Vui lòng nhập ngày khởi chiếu' }]}>
                        <DatePicker style={{ width: '100%' }} format="DD/MM/YYYY" />
                    </Form.Item>
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
                                    avatar={<Avatar shape="square" size={48} src={item.Poster} />}
                                    title={<span style={{ fontWeight: 500 }}>{item.Title}</span>}
                                    description={<span style={{ fontSize: 12, color: '#888' }}>{item.Year} - {item.Type} - {item.Genre}</span>}
                                />
                            </List.Item>
                        )}
                        style={{ maxHeight: 300, overflowY: 'auto' }}
                    />
                </Space>
            </Modal>
        </>
    );
};

export default MoviePage;
