package com.unir.ms_books_catalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookDto {
  private String title;
  private String author;
  private LocalDate publishedDate;
  private String category;
  private String isbn;
  private Short rating;
  private Integer stock;
  private Boolean visible;
}
