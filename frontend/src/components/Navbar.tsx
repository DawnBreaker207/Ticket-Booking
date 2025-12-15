import React, { useEffect, useState } from "react";
import type { FC } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Layout, Button, Divider, Typography } from "antd";
import { logout as authLogout } from "../services/authService";
import Logo from "../assets/logo.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSignOutAlt } from "@fortawesome/free-solid-svg-icons";

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
    const raw = sessionStorage.getItem("user");
    if (raw) {
      try {
        setUser(JSON.parse(raw));
      } catch {
        setUser(null);
      }
    }
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
    try {
      authLogout();
      setUser(null);
      window.dispatchEvent(new Event("auth:logout"));
      navigate("/login?tab=login");
    } catch (err) {
      console.warn("Logout error", err);
      sessionStorage.removeItem("user");
      setUser(null);
      window.dispatchEvent(new Event("auth:logout"));
      navigate("/login?tab=login");
    }
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
                icon={<FontAwesomeIcon icon={faSignOutAlt} />}
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
            onClick={(e) => {
              e.preventDefault();
              navigate("/");
            }}
            style={{
              display: "inline-flex",
              alignItems: "center",
              gap: 20,
              textDecoration: "none",
              color: "inherit",
              cursor: "pointer",
              padding: "8px 12px",
              borderRadius: 8,
              position: "relative",
              zIndex: 9999,
              pointerEvents: "auto",
              background: "transparent",
            }}
            aria-label="Go to homepage"
          >
            <img
              src={Logo}
              alt="Logo"
              style={{
                height: 130,
                objectFit: "contain",
                display: "block",
                pointerEvents: "none",
                userSelect: "none",
              }}
            />
          </Link>
        </div>
      </div>
    </Header>
  );
};

export default Navbar;
