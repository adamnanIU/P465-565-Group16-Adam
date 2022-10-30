package org.example.repo;


import org.example.entity.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("select p from Payment p where p.id = :id")
    public Payment findPaymentById(Integer id);
}
