package com.unir.ms_books_catalogue.repository;

import com.unir.ms_books_catalogue.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

  @Query("SELECT coalesce(count(b), 0) + 1 FROM Book b")
  Long getNextBookId();

}