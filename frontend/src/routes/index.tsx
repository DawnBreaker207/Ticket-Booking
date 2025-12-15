import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "../pages/home/Auth.tsx";
import AdminLayout from "../pages/admin/AdminLayout";
import MoviePage from "../pages/admin/Movie";
import HomePage from "../pages/home/HomePage.tsx";
import CinemaHallPage from "../pages/admin/CinemaHallPage.tsx";
import SeatSelectionPage from "../pages/home/Seat.tsx";
import NotFoundPage from "../pages/error/error_404.tsx";
import PaymentResult from "../pages/home/PaymentResult.tsx";
import OrderList from "../pages/admin/OrderList";

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<HomePage />} />
      <Route path="/seat/:id" element={<SeatSelectionPage />} />
      <Route path="/paymentResult" element={<PaymentResult />} />
      <Route path="/admin" element={<AdminLayout />}>
        <Route path="movie" element={<MoviePage />} />
        <Route path="cinemahall" element={<CinemaHallPage />} />
        <Route path="order" element={<OrderList />} />
      </Route>
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
};

export default AppRoutes;
