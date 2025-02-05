package com.unir.ms_books_payments.service.payment;

import com.unir.ms_books_payments.dto.OrderRequestDto;
import com.unir.ms_books_payments.facade.BooksFacade;
import com.unir.ms_books_payments.facade.model.Book;
import com.unir.ms_books_payments.model.Order;
import com.unir.ms_books_payments.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentService implements IPaymentService {

  private final OrderRepository orderRepository;
  private final BooksFacade booksFacade;

  public PaymentService(OrderRepository orderRepository, BooksFacade booksFacade) {
    this.orderRepository = orderRepository;
    this.booksFacade = booksFacade;
  }

  @Override
  public HashMap<String, Object> saveNewOrder(OrderRequestDto newOrder) {
    HashMap<String, Object> respuesta = new HashMap<>();

    List<Book> books = newOrder.getBookIds().stream().map(booksFacade::getBook).filter(Objects::nonNull).toList();

    /*
     * Valido que la cantidad de libro obtenidos desde ms-books-inventory sea igual a la cantidad de libros que solicito guardar
     * Valido que todos los libros sean visibles
     * Valido que todos los libros tengan stock mayor a cero
     */
    if (books.size() != newOrder.getBookIds().size() || books.stream().anyMatch(book -> !book.getVisible() || book.getStock() <= 0)) {
      respuesta.put("code", 500);
      respuesta.put("message", "Error al generar pedido");
    } else {
      Order order = Order.builder().books(books.stream().map(Book::getId).collect(Collectors.toList())).build();
      Order orderSaved = orderRepository.save(order);
      respuesta.put("code", 201);
      respuesta.put("message", "Se ha registrado la orden: " + orderSaved.getOrderId());
    }

    return respuesta;
  }
}
