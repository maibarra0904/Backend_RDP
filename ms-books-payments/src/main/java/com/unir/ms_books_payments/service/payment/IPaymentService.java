package com.unir.ms_books_payments.service.payment;

import com.unir.ms_books_payments.dto.OrderRequestDto;

import java.util.HashMap;

public interface IPaymentService {
  HashMap<String, Object> saveNewOrder(OrderRequestDto newOrder);
}
