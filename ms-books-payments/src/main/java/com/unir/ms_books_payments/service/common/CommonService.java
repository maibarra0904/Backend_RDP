package com.unir.ms_books_payments.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class CommonService implements ICommonService {

  @Override
  public ResponseEntity<?> getResponse(HashMap<String, Object> resultado) {
    if(resultado != null){
      return switch ((Integer) resultado.get("code")) {
        case 200 -> new ResponseEntity<>(resultado.get("message"), HttpStatus.OK);
        case 201 -> new ResponseEntity<>(resultado.get("message"), HttpStatus.CREATED);
        case 400 -> new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
        case 204 -> new ResponseEntity<>(resultado, HttpStatus.NO_CONTENT);
        case 403 -> new ResponseEntity<>(resultado, HttpStatus.FORBIDDEN);
        case 404 -> new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        default -> new ResponseEntity<>(resultado, HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } else {
      return new ResponseEntity<>("Ocurrio un error al continuar el proceso", HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }


}
