import React, { useEffect, useState } from "react";
import type { FC } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Layout, Button, Divider, Typography } from "antd";
import { logout } from "../services/authService";
import Logo from "../assets/logo.png";

const { Header } = Layout;

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
  const navigate = useNavigate();
  const [user, setUser] = useState<{
    username?: string;
    token?: string;
  } | null>(null);

  useEffect(() => {
    const raw = localStorage.getItem("user");
    if (raw) {
      try {
        setUser(JSON.parse(raw));
      } catch {
        setUser(null);
      }
    }
    // cập nhật khi thay đổi localStorage ở tab khác
    const handler = (e: StorageEvent) => {
      if (e.key === "user") {
        if (e.newValue) setUser(JSON.parse(e.newValue));
        else setUser(null);
      }
    };
    window.addEventListener("storage", handler);
    return () => window.removeEventListener("storage", handler);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("user");
    setUser(null);
  };

  return (
    <Header style={{ padding: 0, background: "transparent" }}>
      <div style={topStripStyle}>
        <div />
        <div
          style={{
            display: "flex",
            alignItems: "center",
            gap: 8,
            margin: "0 200px",
          }}
        >
          {user ? (
            <>
              <Typography.Text style={{ color: "#fff", marginRight: 8 }}>
                Xin chào, <strong>{user.username ?? "User"}</strong>
              </Typography.Text>
              <Divider
                type="vertical"
                style={{ borderColor: "rgba(255,255,255,0.6)", height: 18 }}
              />
              <Button
                type="text"
                size="small"
                style={{
                  color: "#fff",
                  padding: "0 8px",
                  height: 20,
                  lineHeight: "20px",
                }}
                onClick={handleLogout}
              >
                Đăng xuất
              </Button>
            </>
          ) : (
            <>
              <Button
                type="text"
                size="small"
                style={{
                  color: "#fff",
                  padding: "0 8px",
                  height: 20,
                  lineHeight: "20px",
                }}
                onClick={() => navigate("/login?tab=login")}
              >
                Đăng nhập
              </Button>
              <Divider
                type="vertical"
                style={{ borderColor: "rgba(255,255,255,0.6)", height: 18 }}
              />
              <Button
                type="text"
                size="small"
                style={{
                  color: "#fff",
                  padding: "0 8px",
                  height: 20,
                  lineHeight: "20px",
                }}
                onClick={() => navigate("/login?tab=register")}
              >
                Đăng ký
              </Button>
            </>
          )}
        </div>
      </div>

      <div style={headerContentStyle}>
        <div
          style={{
            display: "flex",
            alignItems: "center",
            gap: 20,
            margin: "0 200px",
          }}
        >
          <Link
            to="/"
            style={{
              display: "flex",
              alignItems: "center",
              gap: 20,
              textDecoration: "none",
              color: "inherit",
              cursor: "pointer",
            }}
            aria-label="Go to homepage"
          >
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
          </Link>
        </div>
      </div>
    </Header>
  );
};

export default Navbar;
