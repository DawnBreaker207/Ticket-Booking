import React from 'react';
import { Layout, Menu } from 'antd';
import {
    VideoCameraOutlined, LogoutOutlined
} from '@ant-design/icons';
import { Link, Outlet, useLocation } from 'react-router-dom';

const { Sider, Content } = Layout;

const AdminLayout: React.FC = () => {
    const { pathname } = useLocation();
    const handleLogout = () => {
        // TODO: implement logout logic (e.g., clear auth and redirect)
        console.log('User logged out');
    };

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <Sider>
                <div style={{ height: 32, margin: 16, color: '#fff', textAlign: 'center', fontSize: 18 }}>
                    Trang Admin
                </div>
                <Menu theme="dark" mode="inline" selectedKeys={[pathname]}
                >
                    <Menu.Item key="/admin/movie" icon={<VideoCameraOutlined />}>
                        <Link to="/admin/movie">Phim</Link>
                    </Menu.Item>

                </Menu>
            </Sider>
            <Layout>
                <Content style={{ margin: '20px' }}>
                    <Outlet />
                </Content>
            </Layout>
        </Layout>
    );
};
export default AdminLayout;