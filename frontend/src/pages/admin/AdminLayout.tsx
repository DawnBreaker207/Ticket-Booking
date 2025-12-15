import React, { useMemo } from "react";
import { Layout, Menu } from "antd";
import type { MenuProps } from "antd";
import {
  VideoCameraOutlined,
  CalendarOutlined,
  ShoppingCartOutlined,
} from "@ant-design/icons";
import { Link, Outlet, useLocation } from "react-router-dom";

const { Sider, Content } = Layout;

const AdminLayout: React.FC = () => {
  const { pathname } = useLocation();
  const items: MenuProps["items"] = useMemo(
    () => [
      {
        key: "/admin/movie",
        icon: <VideoCameraOutlined />,
        label: <Link to="/admin/movie">Phim</Link>,
      },
      {
        key: "/admin/cinemahall",
        icon: <CalendarOutlined />,
        label: <Link to="/admin/cinemahall">Xuất chiếu</Link>,
      },
      {
        key: "/admin/order",
        icon: <ShoppingCartOutlined />,
        label: <Link to="/admin/order">Đơn đặt</Link>,
      },
    ],
    []
  );

  const selected =
    items.find(
      (i) => typeof i?.key === "string" && pathname.startsWith(String(i.key))
    )?.key ?? items[0]?.key;
  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider>
        <div
          style={{
            height: 32,
            margin: 16,
            color: "#fff",
            textAlign: "center",
            fontSize: 18,
          }}
        >
          Trang Admin
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[String(selected)]}
          items={items}
        />
      </Sider>
      <Layout>
        <Content style={{ margin: "20px" }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};
export default AdminLayout;
