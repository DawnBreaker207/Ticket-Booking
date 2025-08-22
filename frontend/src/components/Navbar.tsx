import React from "react";
import type { FC } from "react";
import { Layout, Menu, Button, Divider, Typography } from "antd";
import Logo from "../assets/logo.png";
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

const Navbar: FC = () => {
    return (
        <Header style={{ padding: 0, background: "transparent" }}>
            <div style={topStripStyle}>
                <div />
                <div style={{ display: "flex", alignItems: "center", gap: 2, margin: "0 200px" }}>
                    <Button
                        type="text"
                        size="small"
                        style={{ color: "#fff", padding: "0 8px", height: 20, lineHeight: "20px" }}
                    >
                        Đăng nhập
                    </Button>
                    <Divider type="vertical" style={{ borderColor: "rgba(255,255,255,0.6)", height: 18 }} />
                    <Button
                        type="text"
                        size="small"
                        style={{ color: "#fff", padding: "0 8px", height: 20, lineHeight: "20px" }}
                    >
                        Đăng ký
                    </Button>
                </div>
            </div>

            <div style={headerContentStyle}>
                <div style={{ display: "flex", alignItems: "center", gap: 20, margin: "0 200px" }}>
                    <img
                        src={Logo}
                        alt="Logo"
                        style={{ height: 48, objectFit: "contain" }}
                    />
                    <Typography.Text
                        style={{ fontSize: 20, fontWeight: 700, marginRight: 24 }}
                    >
                        Alpha Cinema
                    </Typography.Text>

                </div>
            </div>
        </Header>
    );
};

export default Navbar;
