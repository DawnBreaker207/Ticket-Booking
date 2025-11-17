package com.dawn.backend.repository;

import com.dawn.backend.model.Reservation;
import com.dawn.backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //    Find payment by reservation
    Optional<Payment> findByReservation(Reservation reservation);

    //    Find by stripe payment intent id
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
}
