package com.unir.ms_books_catalogue.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDto {
  private String title;
  private String author;
  private LocalDate publishedDate;
  private String category;
  private String isbn;
  private Short rating;
  private Integer stock;
}
