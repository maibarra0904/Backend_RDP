package com.unir.ms_books_catalogue.controller;

import com.unir.ms_books_catalogue.dto.BookDto;
import com.unir.ms_books_catalogue.dto.CreateBookDto;
import com.unir.ms_books_catalogue.service.books.IBooksService;
import com.unir.ms_books_catalogue.service.common.ICommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("books")
public class CatalogueController {

  private final ICommonService commonService;
  private final IBooksService booksService;

  public CatalogueController(ICommonService commonService, IBooksService booksService) {
    this.commonService = commonService;
    this.booksService = booksService;
  }

  @GetMapping("test")
  public ResponseEntity<?> testService() {
    HashMap<String, Object> respuesta = new HashMap<>();
    respuesta.put("code", 200);
    respuesta.put("message", "Servicio catalogue correcto");
    return commonService.getResponse(respuesta);
  }

  @GetMapping()
  public ResponseEntity<?> searchBooks(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String author,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String isbn,
      @RequestParam(required = false) Short rating,
      @RequestParam(required = false) Integer stock,
      @RequestParam(required = false) Boolean visible) {

    HashMap<String, Object> respuesta = booksService.searchBooks(title, author, category, isbn, rating, stock, visible);

    return commonService.getResponse(respuesta);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getBookById(@PathVariable Long id) {
    HashMap<String, Object> respuesta = booksService.getBookById(id);

    return commonService.getResponse(respuesta);
  }

  @PostMapping
  public ResponseEntity<?> addBook(@RequestBody CreateBookDto createBookDto) {
    HashMap<String, Object> respuesta = booksService.addNewBook(createBookDto);

    return commonService.getResponse(respuesta);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateBook(
      @PathVariable Long id,
      @RequestBody BookDto book) {
    HashMap<String, Object> respuesta = booksService.updateBook(id, book);

    return commonService.getResponse(respuesta);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> patchBook(
      @PathVariable Long id,
      @RequestBody String patchBody) {
    HashMap<String, Object> respuesta = booksService.patchBook(id, patchBody);

    return commonService.getResponse(respuesta);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteBook(@PathVariable Long id) {
    HashMap<String, Object> respuesta = booksService.deleteBook(id);

    return commonService.getResponse(respuesta);
  }

}
