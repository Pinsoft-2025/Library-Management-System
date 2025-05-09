package io.github.SenaUstun_Dev.library_management.service;

import io.github.SenaUstun_Dev.library_management.dto.request.*;
import io.github.SenaUstun_Dev.library_management.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    public BookResponse addBook(CreateBookRequest request);
    public BookResponse updateBook(UpdateBookRequest request, Long id);
    public void deleteBook(Long id);

    public BookResponse findBookById(Long id);
    public BookResponse findBookByName(String name);
    public List<BookResponse> findAll();
    public List<BookResponse> findBookByAuthor(String author);
    public List<BookResponse> findBookByGenre(String genre);
    public List<BookResponse> findBookByPublisher(String publisher);
}
