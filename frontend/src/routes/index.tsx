import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "../pages/home/Login.tsx";
import AdminLayout from "../pages/admin/AdminLayout";
import MoviePage from "../pages/admin/Movie";
import HomePage from "../pages/home/HomePage.tsx";
import CinemaHallPage from "../pages/admin/CinemaHallPage.tsx";
import SeatSelectionPage from "../pages/home/SeatSelector.tsx";

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<HomePage />} />
      <Route path="/seat" element={<SeatSelectionPage />} />

      <Route path="/admin" element={<AdminLayout />}>
        <Route path="movie" element={<MoviePage />} />
        <Route path="cinemahall" element={<CinemaHallPage />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
