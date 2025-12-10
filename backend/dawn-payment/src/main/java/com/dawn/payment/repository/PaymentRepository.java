package com.dawn.payment.repository;

import com.dawn.booking.model.Reservation;
import com.dawn.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //    Find payment by reservation
    Optional<Payment> findByReservation(Reservation reservation);
}
