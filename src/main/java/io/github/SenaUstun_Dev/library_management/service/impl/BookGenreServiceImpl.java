package io.github.SenaUstun_Dev.library_management.service.impl;

import io.github.SenaUstun_Dev.library_management.dto.request.CreateBookGenreRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateBookGenreRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.BookGenreResponse;
import io.github.SenaUstun_Dev.library_management.entity.BookGenre;
import io.github.SenaUstun_Dev.library_management.exception.BaseException;
import io.github.SenaUstun_Dev.library_management.exception.ErrorMessages;
import io.github.SenaUstun_Dev.library_management.repository.BookGenreRepository;
import io.github.SenaUstun_Dev.library_management.service.BookGenreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookGenreServiceImpl implements BookGenreService {

    private final BookGenreRepository bookGenreRepository;

    @Override
    public BookGenreResponse addBookGenre(CreateBookGenreRequest request) {
        BookGenre genre = BookGenre.builder()
                .name(request.name())
                .build();

        bookGenreRepository.save(genre);

        return convertToResponseDto(genre);
    }

    @Transactional
    @Override
    public BookGenreResponse updateBookGenre(UpdateBookGenreRequest request, Long id) {
        BookGenre genre = bookGenreRepository.findById(id).orElseThrow(() -> new BaseException(
                HttpStatus.NOT_FOUND,
                ErrorMessages.BOOKGENRE_NOT_FOUND,
                "Book genre with ID " + id + " not found."
        ));

        genre.setName(request.name());
        bookGenreRepository.save(genre);

        return convertToResponseDto(genre);
    }

    @Transactional
    @Override
    public void deleteBookGenre(Long id) {
        if (bookGenreRepository.existsById(id)) {
            bookGenreRepository.deleteById(id);
        } else {
            throw new BaseException(
                    HttpStatus.NOT_FOUND,
                    ErrorMessages.BOOKGENRE_NOT_FOUND,
                    "Book genre with ID " + id + " not found."
            );
        }
    }

    @Override
    public BookGenreResponse findBookGenreById(Long id) {
        BookGenre genre = bookGenreRepository.findById(id).orElseThrow(() -> new BaseException(
                HttpStatus.NOT_FOUND,
                ErrorMessages.BOOKGENRE_NOT_FOUND,
                "Book genre with ID " + id + " not found."
        ));
        return convertToResponseDto(genre);
    }

    @Override
    public BookGenreResponse findBookGenreByName(String name) {
        BookGenre genre = bookGenreRepository.findByNameIgnoreCase(name).orElseThrow(() -> new BaseException(
                HttpStatus.NOT_FOUND,
                ErrorMessages.BOOKGENRE_NOT_FOUND,
                "Book genre with name '" + name + "' not found."
        ));

        return convertToResponseDto(genre);
    }

    @Override
    public List<BookGenreResponse> findAllBookGenres() {
        List<BookGenre> genres = bookGenreRepository.findAll();

        return genres.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }


    //>>>>>>>>>>>>>> HELPER METHODS

    private BookGenreResponse convertToResponseDto(BookGenre genre) {
        if (genre == null) {
            throw new BaseException(HttpStatus.NOT_FOUND,
                    ErrorMessages.BOOKGENRE_NOT_FOUND,
                    "Book genre is null. Can't convert to BookGenreResponse.");
        }

        return BookGenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
