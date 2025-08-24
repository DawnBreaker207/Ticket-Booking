import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Login from '../pages/home/Login.tsx';
import AdminLayout from '../pages/admin/AdminLayout';
import MoviePage from '../pages/admin/Movie';
import HomePage from '../pages/home/HomePage.tsx';

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<HomePage />} />
      <Route path="/admin" element={<AdminLayout />}>
        <Route path="movie" element={<MoviePage />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
