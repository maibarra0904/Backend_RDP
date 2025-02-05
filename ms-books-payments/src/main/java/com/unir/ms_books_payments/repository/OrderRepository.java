package com.unir.ms_books_payments.repository;

import com.unir.ms_books_payments.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}