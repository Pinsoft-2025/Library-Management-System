package io.github.SenaUstun_Dev.library_management.service;

import io.github.SenaUstun_Dev.library_management.dto.request.CreateBookGenreRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateBookGenreRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.BookGenreResponse;

import java.util.List;

public interface BookGenreService {
    public BookGenreResponse addBookGenre(CreateBookGenreRequest request);
    public BookGenreResponse updateBookGenre(UpdateBookGenreRequest request, Long id );
    public void deleteBookGenre(Long id);

    public BookGenreResponse findBookGenreById(Long id);
    public BookGenreResponse findBookGenreByName(String name);
    public List<BookGenreResponse> findAllBookGenres();
}
