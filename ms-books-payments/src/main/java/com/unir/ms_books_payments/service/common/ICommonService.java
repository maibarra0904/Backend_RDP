package com.unir.ms_books_payments.service.common;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public interface ICommonService {
  ResponseEntity<?> getResponse(HashMap<String, Object> resultado);
}
