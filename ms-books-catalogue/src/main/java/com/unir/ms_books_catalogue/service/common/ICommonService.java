package com.unir.ms_books_catalogue.service.common;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public interface ICommonService {
  ResponseEntity<?> getResponse(HashMap<String, Object> resultado);
}
