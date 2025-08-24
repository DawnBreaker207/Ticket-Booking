import { useState, ChangeEvent, FormEvent } from 'react';
import { useDispatch } from 'react-redux';
import type { AppDispatch } from '../../app/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faLock, faEnvelope, faCalendar, faMale, faPhoneSquare } from '@fortawesome/free-solid-svg-icons';

interface LoginForm {
    email: string;
    password: string;
}

interface RegisterForm {
    name: string;
    email: string;
    password: string;
    confirmPassword: string;
    dob: string;
    gender: string;
    phone: string;
}

const Login: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const [activeTab, setActiveTab] = useState<'login' | 'register'>('login');
    const [loginForm, setLoginForm] = useState<LoginForm>({ email: '', password: '' });
    const [registerForm, setRegisterForm] = useState<RegisterForm>({ name: '', email: '', password: '', confirmPassword: '', dob: '', gender: '', phone: '' });

    const handleLoginChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setLoginForm(prev => ({ ...prev, [name]: value }));
    };

    const handleRegisterChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setRegisterForm(prev => ({ ...prev, [name]: value }));
    };

    const handleLoginSubmit = (e: FormEvent) => {
        e.preventDefault();
        console.log('Logging in with', loginForm);
    };

    const handleRegisterSubmit = (e: FormEvent) => {
        e.preventDefault();
        console.log('Registering with', registerForm);
    };

    const gradientStyle = { backgroundImage: 'linear-gradient(to right, #0a64a7 0%, #258dcf 51%, #3db1f3 100%)' };

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <div className="w-[530px] bg-white">
                {/* Tabs Header */}
                <div className="flex sticky top-0 bg-white z-10">
                    <button
                        onClick={() => setActiveTab('login')}
                        className={`flex-1 py-3 text-center font-medium cursor-pointer ${activeTab === 'login' ? 'bg-[#03599d] text-white' : 'bg-transparent text-gray-700'}`}
                    >
                        ĐĂNG NHẬP
                    </button>
                    <button
                        onClick={() => setActiveTab('register')}
                        className={`flex-1 py-3 text-center font-medium cursor-pointer ${activeTab === 'register' ? 'bg-[#03599d] text-white' : 'bg-transparent text-gray-700'}`}
                    >
                        ĐĂNG KÝ
                    </button>
                </div>

                {/* Form Container */}
                <div className="p-4">
                    {activeTab === 'login' ? (
                        <form onSubmit={handleLoginSubmit} className="space-y-4">
                            <div>
                                <label htmlFor="email" className="block text-sm font-medium mb-1">Email</label>
                                <div className="relative">
                                    <FontAwesomeIcon icon={faUser} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
                                    <input
                                        type="email"
                                        id="email"
                                        name="email"
                                        value={loginForm.email}
                                        onChange={handleLoginChange}
                                        required
                                        className="w-full pl-10 pr-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                        placeholder="Nhập email"
                                    />
                                </div>
                            </div>

                            <div>
                                <label htmlFor="password" className="block text-sm font-medium mb-1">Mật khẩu</label>
                                <div className="relative">
                                    <FontAwesomeIcon icon={faLock} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
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
                        <form onSubmit={handleRegisterSubmit} className="grid grid-cols-2 gap-4">
                            <div>
                                <label htmlFor="name" className="block text-sm font-medium mb-1">* Họ tên</label>
                                <input
                                    type="text"
                                    id="name"
                                    name="name"
                                    value={registerForm.name}
                                    onChange={handleRegisterChange}
                                    required
                                    className="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                    placeholder="Họ tên"
                                />
                            </div>
                            <div>
                                <label htmlFor="emailReg" className="block text-sm font-medium mb-1">* Email</label>
                                <div className="relative">
                                    <FontAwesomeIcon icon={faEnvelope} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
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
                                <label htmlFor="passwordReg" className="block text-sm font-medium mb-1">* Mật khẩu</label>
                                <div className="relative">
                                    <FontAwesomeIcon icon={faLock} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
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
                                <label htmlFor="confirmPassword" className="block text-sm font-medium mb-1">* Xác nhận lại mật khẩu</label>
                                <div className="relative">
                                    <FontAwesomeIcon icon={faLock} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
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
                                <label htmlFor="dob" className="block text-sm font-medium mb-1">* Ngày sinh</label>
                                <div className="relative">
                                    <FontAwesomeIcon icon={faCalendar} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
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
                                <label htmlFor="gender" className="block text-sm font-medium mb-1">Giới tính</label>
                                <div className="relative">
                                    <FontAwesomeIcon icon={faMale} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
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
                                <label htmlFor="phone" className="block text-sm font-medium mb-1">* Số điện thoại</label>
                                <div className="relative">
                                    <FontAwesomeIcon icon={faPhoneSquare} className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
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
    );
};

export default Login;
