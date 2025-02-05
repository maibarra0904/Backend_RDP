package com.unir.ms_books_catalogue.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unir.ms_books_catalogue.dto.BookDto;
import com.unir.ms_books_catalogue.utils.Consts;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "books")
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = Consts.TITLE)
  private String title;

  @Column(name = Consts.AUTHOR)
  private String author;

  @Column(name = Consts.PUBLISHED_DATE)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate publishedDate;

  @Column(name = Consts.CATEGORY)
  private String category;

  @Column(name = Consts.ISBN)
  private String isbn;

  @Column(name = Consts.RATING)
  private Short rating;

  @Column(name = Consts.STOCK)
  private Integer stock;

  @Column(name = Consts.VISIBLE)
  private Boolean visible;

  public void update(BookDto bookDto) {
    this.title = bookDto.getTitle();
    this.author = bookDto.getAuthor();
    this.publishedDate = bookDto.getPublishedDate();
    this.category = bookDto.getCategory();
    this.isbn = bookDto.getIsbn();
    this.rating = bookDto.getRating();
    this.stock = bookDto.getStock();
  }
}
