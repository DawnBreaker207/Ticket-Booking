import React from "react";
import type { FC } from "react";
import { Layout, Menu, Button, Avatar } from "antd";
import { MenuOutlined, UserOutlined } from "@ant-design/icons";

const { Header, Content } = Layout;

const topStripStyle: React.CSSProperties = {
    background: "#000",
    height: 24,
    padding: "0 16px",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    color: "#fff",
    fontSize: 12,
};

const headerContentStyle: React.CSSProperties = {
    height: 79,
    padding: "0 16px",
    display: "flex",
    alignItems: "center",
    background: "#ffffff",
    borderBottom: "1px solid #f0f0f0",
};

const AntdNavbar: FC = () => {
    const menuItems = [
        { key: "home", label: "Home" },
        { key: "products", label: "Products" },
        { key: "settings", label: "Settings" },
    ];

    return (
        <Layout style={{ minHeight: "100vh" }}>
            <Header style={{ padding: 0, background: "transparent" }}>
                <div style={topStripStyle}>
                    <div style={{ display: "flex", alignItems: "start", gap: 12 }}>
                        <button>đăng ký</button>
                    </div>
                </div>
                <div style={headerContentStyle}>
                    <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
                        <div style={{ fontWeight: 700, fontSize: 18 }}>Tên Ứng Dụng</div>
                    </div>

                    <div style={{ marginLeft: "auto", display: "flex", alignItems: "center", gap: 12 }}>
                        <Menu
                            mode="horizontal"
                            selectable={false}
                            items={menuItems as any}
                            style={{ background: "transparent", borderBottom: "none" }}
                        />
                        <Button type="text" icon={<MenuOutlined />} />
                    </div>
                </div>
            </Header>

            {/* Main content area */}
            <Content style={{ background: "#fff", padding: 24 }}>
                <div style={{ minHeight: 360 }}>
                    <p>list phim</p>
                </div>
            </Content>
        </Layout>
    );
};

export default AntdNavbar;
