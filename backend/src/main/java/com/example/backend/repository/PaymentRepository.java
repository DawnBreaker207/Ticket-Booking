package com.example.backend.repository;

import com.example.backend.model.Order;
import com.example.backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //    Find payment by reservation
    Optional<Payment> findByReservation(Order reservation);

    //    Find by stripe payment intent id
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
}
