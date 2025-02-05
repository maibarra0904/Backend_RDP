package com.unir.ms_books_catalogue.service.books;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.unir.ms_books_catalogue.dto.BookDto;
import com.unir.ms_books_catalogue.dto.CreateBookDto;
import com.unir.ms_books_catalogue.model.Book;
import com.unir.ms_books_catalogue.repository.BookRepository;
import com.unir.ms_books_catalogue.utils.Consts;
import com.unir.ms_books_catalogue.utils.SearchCriteria;
import com.unir.ms_books_catalogue.utils.SearchOperation;
import com.unir.ms_books_catalogue.utils.SearchStatement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BooksService implements IBooksService {

  private final BookRepository bookRepository;
  private final ObjectMapper objectMapper;

  public BooksService(BookRepository bookRepository, ObjectMapper objectMapper) {
    this.bookRepository = bookRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  public HashMap<String, Object> getBookById(Long id) {
    HashMap<String, Object> respuesta = new HashMap<>();

    Optional<Book> book = bookRepository.findById(id);
    if (book.isEmpty()) {
      respuesta.put("code", 204);
      respuesta.put("message", "Book not found");
    } else {
      respuesta.put("code", 200);
      respuesta.put("message", book.get());
    }
    return respuesta;
  }

  @Override
  public HashMap<String, Object> searchBooks(String title, String author, String category, String isbn, Short rating, Integer stock, Boolean visible) {
    HashMap<String, Object> respuesta = new HashMap<>();

    List<Book> books;

    if (title != null || author != null || category != null || isbn != null || rating != null || stock != null || visible != null) {
      books = searchBooksWithFilters(title, author, category, isbn, rating, stock, visible);
    } else {
      books = bookRepository.findAll();
    }

    respuesta.put("code", 200);
    respuesta.put("message", books);

    return respuesta;
  }

  /**
   * Creo el criterio de busqueda para el listado de libros
   *
   * @param title    Titulo del libro
   * @param author   Nombre del autor
   * @param category Categoria del libro
   * @param isbn     Código ISBN del libro
   * @param rating   Rating del libro
   * @param stock    Stock del libro
   * @param visible  Propiedad si el libro es visible o no
   * @return Listado de libros consultados en la base de datos con las condiciones buscadas
   */
  private List<Book> searchBooksWithFilters(String title, String author, String category, String isbn, Short rating, Integer stock, Boolean visible) {
    SearchCriteria<Book> spec = new SearchCriteria<>();

    if (title != null && !title.isEmpty()) {
      spec.add(new SearchStatement(Consts.TITLE, title, SearchOperation.MATCH));
    }

    if (author != null && !author.isEmpty()) {
      spec.add(new SearchStatement(Consts.AUTHOR, author, SearchOperation.EQUAL));
    }

    if (category != null && !category.isEmpty()) {
      spec.add(new SearchStatement(Consts.CATEGORY, category, SearchOperation.MATCH));
    }

    if (isbn != null && !isbn.isEmpty()) {
      spec.add(new SearchStatement(Consts.ISBN, isbn, SearchOperation.MATCH));
    }

    if (rating != null) {
      spec.add(new SearchStatement(Consts.RATING, rating, SearchOperation.EQUAL));
    }

    if (stock != null) {
      spec.add(new SearchStatement(Consts.STOCK, stock, SearchOperation.EQUAL));
    }

    if (visible != null) {
      spec.add(new SearchStatement(Consts.VISIBLE, visible, SearchOperation.EQUAL));
    }

    return bookRepository.findAll(spec);
  }

  @Override
  public HashMap<String, Object> addNewBook(CreateBookDto createBookDto) {
    HashMap<String, Object> respuesta = new HashMap<>();

    try {
      Book book = Book.builder()
          // .id(bookRepository.getNextBookId())
          .title(createBookDto.getTitle())
          .author(createBookDto.getAuthor())
          .publishedDate(createBookDto.getPublishedDate())
          .category(createBookDto.getCategory())
          .isbn(createBookDto.getIsbn())
          .rating(createBookDto.getRating())
          .stock(createBookDto.getStock())
          .visible(createBookDto.getVisible())
          .build();
      System.out.println(book.toString());
      bookRepository.save(book);
      respuesta.put("code", 201);
      respuesta.put("message", book);
    } catch (Exception e) {
      log.error("Error al guardar un nuevo libro {}", e.getMessage());
      respuesta.put("code", 500);
      respuesta.put("message", "Error al guardar un nuevo libro");
    }

    return respuesta;
  }

  @Override
  public HashMap<String, Object> updateBook(Long id, BookDto book) {
    HashMap<String, Object> respuesta = new HashMap<>();

    Optional<Book> optionalBook = bookRepository.findById(id);

    if (optionalBook.isPresent()) {
      Book bookDB = optionalBook.get();
      bookDB.update(book);

      bookRepository.save(bookDB);
      respuesta.put("code", 200);
      respuesta.put("message", bookDB);
    } else {
      respuesta.put("code", 404);
      respuesta.put("message", "Book not found");
    }


    return respuesta;
  }

  @Override
  public HashMap<String, Object> patchBook(Long id, String patchBody) {
    HashMap<String, Object> respuesta = new HashMap<>();

    Optional<Book> optionalBook = bookRepository.findById(id);

    if (optionalBook.isPresent()) {
      try {
        Book book = optionalBook.get();
        JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(patchBody));
        JsonNode target = jsonMergePatch.apply(objectMapper.readTree(objectMapper.writeValueAsString(book)));
        Book bookPatched = objectMapper.treeToValue(target, Book.class);
        bookRepository.save(bookPatched);
        respuesta.put("code", 200);
        respuesta.put("message", bookPatched);
      } catch (JsonProcessingException | JsonPatchException e) {
        log.error("Error: {}", e.getMessage());
        respuesta.put("code", 500);
        respuesta.put("message", "Error al editar el valor");
      }
    } else {
      respuesta.put("code", 404);
      respuesta.put("message", "Book not found");
    }

    return respuesta;
  }

  @Override
  public HashMap<String, Object> deleteBook(Long id) {
    HashMap<String, Object> respuesta = new HashMap<>();

    try {
      Optional<Book> bookExist = bookRepository.findById(id);
      if (bookExist.isPresent()) {
        bookRepository.deleteById(id);

        respuesta.put("code", 200);
        respuesta.put("message", "Book deleted successfully");
      } else {
        respuesta.put("code", 404);
        respuesta.put("message", "Book not found");
      }
    } catch (Exception e) {
      respuesta.put("code", 500);
      respuesta.put("message", e.getMessage());
    }

    return respuesta;
  }
}
