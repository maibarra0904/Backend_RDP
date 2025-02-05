package com.unir.ms_books_catalogue.service.books;

import com.unir.ms_books_catalogue.dto.BookDto;
import com.unir.ms_books_catalogue.dto.CreateBookDto;

import java.util.HashMap;

public interface IBooksService {

  HashMap<String, Object> getBookById(Long id);

  HashMap<String, Object> searchBooks(String title, String author, String category, String isbn, Short rating, Integer stock, Boolean visible);

  HashMap<String, Object> addNewBook(CreateBookDto createBookDto);

  HashMap<String, Object> updateBook(Long id, BookDto book);

  HashMap<String, Object> patchBook(Long id, String patchBody);

  HashMap<String, Object> deleteBook(Long id);
}
