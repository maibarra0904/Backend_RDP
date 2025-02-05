package com.unir.ms_books_payments.controller;

import com.unir.ms_books_payments.dto.OrderRequestDto;
import com.unir.ms_books_payments.service.common.ICommonService;
import com.unir.ms_books_payments.service.payment.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("payment")
public class PaymentController {

  private final IPaymentService paymentService;
  private final ICommonService commonService;

  public PaymentController(IPaymentService paymentService, ICommonService commonService) {
    this.paymentService = paymentService;
    this.commonService = commonService;
  }

  @GetMapping("test")
  public ResponseEntity<?> testService() {
    HashMap<String, Object> respuesta = new HashMap<>();
    respuesta.put("code", 200);
    respuesta.put("message", "Servicio payments correcto");
    return commonService.getResponse(respuesta);
  }

  @PostMapping
  public ResponseEntity<?> createPayment(@RequestBody OrderRequestDto newOrder) {
    HashMap<String, Object> respuesta = paymentService.saveNewOrder(newOrder);
    return commonService.getResponse(respuesta);
  }

}
