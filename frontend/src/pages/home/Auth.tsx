// src/pages/Auth.tsx
import React, { useState } from "react";
import type { ChangeEvent, FormEvent } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUser,
  faLock,
  faEnvelope,
  faCalendar,
  faMale,
  faPhoneSquare,
} from "@fortawesome/free-solid-svg-icons";
import Navbar from "../../components/Navbar";
import { message } from "antd";
import { useAppDispatch } from "../../hooks/useAppDispatch";
import { loginUser, registerUser } from "../../features/auth/authSlice";
import { useNavigate } from "react-router-dom";

import { Layout } from "antd";
import type { LoginForm, RegisterForm } from "../../types/Auth";

const Login: React.FC = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState<"login" | "register">("login");
  const [loginForm, setLoginForm] = useState<LoginForm>({
    username: "",
    password: "",
  });
  const [registerForm, setRegisterForm] = useState<RegisterForm>({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    dob: "",
    gender: "",
    phone: "",
    roles: ["user"],
  });

  const handleLoginChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setLoginForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleRegisterChange = (
    e: ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setRegisterForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleLoginSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const payload = {
      username: loginForm.username,
      password: loginForm.password,
    };

    try {
      const resultAction = await dispatch(loginUser(payload as any));
      console.log("resultAction (full)", resultAction);
      console.log("resultAction.payload", resultAction.payload);

      // nếu thunk fulfilled
      if (loginUser.fulfilled.match(resultAction)) {
        // thunk của bạn bây giờ trả trực tiếp AuthData (token/email...), nhưng
        // để an toàn ta vẫn try lấy payload.data nếu payload là wrapper {code,...,data}
        const payloadData =
          (resultAction.payload as any) ?? (resultAction as any).data ?? null;

        const userToStore: {
          username?: string | null;
          email?: string | null;
          token?: string | null;
          userId: number | null;
          roles?: string[] | null;
          refreshToken?: string | null;
        } = {
          username: payloadData?.username ?? loginForm.username,
          email: payloadData?.email ?? null,
          token: payloadData?.token ?? null,
          userId: payloadData?.userId ?? null,
          roles: payloadData?.roles ?? null,
          refreshToken: payloadData?.refreshToken ?? null,
        };

        try {
          sessionStorage.setItem("user", JSON.stringify(userToStore));
        } catch (err) {
          console.warn("Không thể lưu vào sessionStorage", err);
        }

        message.success("Đăng nhập thành công");
        navigate("/", { replace: true });
      } else {
        // lấy lỗi nếu có
        // @ts-ignore
        const err = resultAction.payload ?? resultAction.error?.message;
        message.error(err ?? "Đăng nhập thất bại");
      }
    } catch (err) {
      message.error("Lỗi khi gọi API");
    }
  };

  const handleRegisterSubmit = async (e: FormEvent) => {
    e.preventDefault();

    if (registerForm.password !== registerForm.confirmPassword) {
      message.error("Mật khẩu và xác nhận mật khẩu không khớp");
      return;
    }

    try {
      const payload = {
        username: registerForm.username,
        email: registerForm.email,
        password: registerForm.password,
        confirmPassword: registerForm.confirmPassword,
        dob: registerForm.dob,
        gender: registerForm.gender,
        phone: registerForm.phone,
        role: registerForm.roles,
      };
      const resultAction = await dispatch(registerUser(payload as any));
      if (registerUser.fulfilled.match(resultAction)) {
        message.success("Đăng ký thành công");
        navigate("/login", { replace: true });
      } else {
        // @ts-ignore
        const err = resultAction.payload ?? resultAction.error?.message;
        message.error("Đăng ký thất bại");
      }
    } catch (err) {
      message.error("Lỗi khi gọi API");
    }
  };

  const gradientStyle = {
    backgroundImage:
      "linear-gradient(to right, #0a64a7 0%, #258dcf 51%, #3db1f3 100%)",
  };

  return (
    <Layout>
      <Navbar />
      <div
        className="flex items-center justify-center"
        style={{ minHeight: "calc(100vh - 103px)" }}
      >
        <div className="w-[530px] bg-white">
          {/* Tabs Header */}
          <div className="flex sticky top-0 bg-white z-10">
            <button
              onClick={() => setActiveTab("login")}
              className={`flex-1 py-3 text-center font-medium cursor-pointer ${
                activeTab === "login"
                  ? "bg-[#03599d] text-white"
                  : "bg-transparent text-gray-700"
              }`}
            >
              ĐĂNG NHẬP
            </button>
            <button
              onClick={() => setActiveTab("register")}
              className={`flex-1 py-3 text-center font-medium cursor-pointer ${
                activeTab === "register"
                  ? "bg-[#03599d] text-white"
                  : "bg-transparent text-gray-700"
              }`}
            >
              ĐĂNG KÝ
            </button>
          </div>

          {/* Form Container */}
          <div className="p-4">
            {activeTab === "login" ? (
              <form onSubmit={handleLoginSubmit} className="space-y-4">
                <div>
                  <label
                    htmlFor="username"
                    className="block text-sm font-medium mb-1"
                  >
                    Tên đăng nhập
                  </label>
                  <div className="relative">
                    <FontAwesomeIcon
                      icon={faUser}
                      className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    />
                    <input
                      type="text"
                      id="username"
                      name="username"
                      value={loginForm.username}
                      onChange={handleLoginChange}
                      required
                      className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="Nhập tên đăng nhập"
                    />
                  </div>
                </div>

                <div>
                  <label
                    htmlFor="password"
                    className="block text-sm font-medium mb-1"
                  >
                    Mật khẩu
                  </label>
                  <div className="relative">
                    <FontAwesomeIcon
                      icon={faLock}
                      className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    />
                    <input
                      type="password"
                      id="password"
                      name="password"
                      value={loginForm.password}
                      onChange={handleLoginChange}
                      required
                      className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="Nhập mật khẩu"
                    />
                  </div>
                </div>

                <div className="flex justify-center">
                  <button
                    type="submit"
                    className="w-[60%] px-8 py-2 text-white text-sm rounded-lg transition cursor-pointer hover:opacity-90"
                    style={gradientStyle}
                  >
                    ĐĂNG NHẬP
                  </button>
                </div>
              </form>
            ) : (
              <form
                onSubmit={handleRegisterSubmit}
                className="grid grid-cols-2 gap-4"
              >
                <div>
                  <label
                    htmlFor="usernameReg"
                    className="block text-sm font-medium mb-1"
                  >
                    * Tên đăng nhập
                  </label>
                  <input
                    type="text"
                    id="usernameReg"
                    name="username"
                    value={registerForm.username}
                    onChange={handleRegisterChange}
                    required
                    className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="Tên đăng nhập"
                  />
                </div>
                <div>
                  <label
                    htmlFor="emailReg"
                    className="block text-sm font-medium mb-1"
                  >
                    * Email
                  </label>
                  <div className="relative">
                    <FontAwesomeIcon
                      icon={faEnvelope}
                      className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    />
                    <input
                      type="email"
                      id="emailReg"
                      name="email"
                      value={registerForm.email}
                      onChange={handleRegisterChange}
                      required
                      className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="Email"
                    />
                  </div>
                </div>

                <div>
                  <label
                    htmlFor="passwordReg"
                    className="block text-sm font-medium mb-1"
                  >
                    * Mật khẩu
                  </label>
                  <div className="relative">
                    <FontAwesomeIcon
                      icon={faLock}
                      className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    />
                    <input
                      type="password"
                      id="passwordReg"
                      name="password"
                      value={registerForm.password}
                      onChange={handleRegisterChange}
                      required
                      className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="Mật khẩu"
                    />
                  </div>
                </div>
                <div>
                  <label
                    htmlFor="confirmPassword"
                    className="block text-sm font-medium mb-1"
                  >
                    * Xác nhận lại mật khẩu
                  </label>
                  <div className="relative">
                    <FontAwesomeIcon
                      icon={faLock}
                      className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    />
                    <input
                      type="password"
                      id="confirmPassword"
                      name="confirmPassword"
                      value={registerForm.confirmPassword}
                      onChange={handleRegisterChange}
                      required
                      className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="Xác nhận mật khẩu"
                    />
                  </div>
                </div>
                <div>
                  <label
                    htmlFor="dob"
                    className="block text-sm font-medium mb-1"
                  >
                    * Ngày sinh
                  </label>
                  <div className="relative">
                    <FontAwesomeIcon
                      icon={faCalendar}
                      className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    />
                    <input
                      type="date"
                      id="dob"
                      name="dob"
                      value={registerForm.dob}
                      onChange={handleRegisterChange}
                      required
                      className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                </div>
                <div>
                  <label
                    htmlFor="gender"
                    className="block text-sm font-medium mb-1"
                  >
                    Giới tính
                  </label>
                  <div className="relative">
                    <FontAwesomeIcon
                      icon={faMale}
                      className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    />
                    <select
                      id="gender"
                      name="gender"
                      value={registerForm.gender}
                      onChange={handleRegisterChange}
                      className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="">Giới tính</option>
                      <option value="male">Nam</option>
                      <option value="female">Nữ</option>
                      <option value="other">Khác</option>
                    </select>
                  </div>
                </div>
                <div>
                  <label
                    htmlFor="phone"
                    className="block text-sm font-medium mb-1"
                  >
                    * Số điện thoại
                  </label>
                  <div className="relative">
                    <FontAwesomeIcon
                      icon={faPhoneSquare}
                      className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    />
                    <input
                      type="tel"
                      id="phone"
                      name="phone"
                      value={registerForm.phone}
                      onChange={handleRegisterChange}
                      required
                      className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="Số điện thoại"
                    />
                  </div>
                </div>
                <div className="col-span-2 flex justify-center">
                  <button
                    type="submit"
                    className="w-[60%] px-8 py-2 text-white text-sm rounded-lg transition cursor-pointer hover:opacity-90"
                    style={gradientStyle}
                  >
                    ĐĂNG KÝ
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Login;
