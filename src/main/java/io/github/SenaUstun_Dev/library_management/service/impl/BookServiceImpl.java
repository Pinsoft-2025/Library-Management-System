package io.github.SenaUstun_Dev.library_management.service.impl;

import io.github.SenaUstun_Dev.library_management.exception.BaseException;
import io.github.SenaUstun_Dev.library_management.dto.request.CreateBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.BookResponse;
import io.github.SenaUstun_Dev.library_management.entity.Author;
import io.github.SenaUstun_Dev.library_management.entity.Book;
import io.github.SenaUstun_Dev.library_management.entity.BookGenre;
import io.github.SenaUstun_Dev.library_management.entity.Publisher;
import io.github.SenaUstun_Dev.library_management.exception.ErrorMessages;
import io.github.SenaUstun_Dev.library_management.repository.AuthorRepository;
import io.github.SenaUstun_Dev.library_management.repository.BookGenreRepository;
import io.github.SenaUstun_Dev.library_management.repository.BookRepository;
import io.github.SenaUstun_Dev.library_management.repository.PublisherRepository;
import io.github.SenaUstun_Dev.library_management.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookGenreRepository bookGenreRepository;
    private final PublisherRepository publisherRepository;

    @Override
    @Transactional
    public BookResponse addBook(CreateBookRequest request) {
        // Temel alan kontrolü
        if (request.name() == null || request.name().trim().isEmpty()) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "Book name cannot be empty.");
        }

        // Aynı isimde kitap kontrolü
        if (bookRepository.findByNameIgnoreCase(request.name()).isPresent()) {
            throw new BaseException(HttpStatus.CONFLICT, "Book with name '" + request.name() + "' already exists.");
        }

        // Book nesnesi oluştur
        Book book = Book.builder()
                .name(request.name())
                .status(request.status())
                .authors(request.authors() != null ? request.authors() : new HashSet<>())
                .publishers(request.publisher() != null ? request.publisher() : new HashSet<>())
                .genres(request.genres() != null ? request.genres() : new HashSet<>())
                .build();

        Book savedBook = bookRepository.save(book);
        return convertToResponseDto(savedBook);
    }

    @Override
    @Transactional
    public BookResponse updateBook(UpdateBookRequest request, Long id) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new BaseException(
                        HttpStatus.NOT_FOUND,
                        ErrorMessages.BOOK_NOT_FOUND,
                        "Book with ID " + id + " not found. Cannot update."
                ));

        // Alanları güncelle
        if (request.name() != null && !request.name().trim().isEmpty()) {
            bookToUpdate.setName(request.name());
        }
        if (request.status() != null) {
            bookToUpdate.setStatus(request.status());
        }

        // İlişkili entity'leri güncelle
        if (request.authors() != null) {
            bookToUpdate.setAuthors(request.authors());
        }
        if (request.publisher() != null) {
            bookToUpdate.setPublishers(request.publisher());
        }
        if (request.genres() != null) {
            bookToUpdate.setGenres(request.genres());
        }

        Book updatedBook = bookRepository.save(bookToUpdate);
        return convertToResponseDto(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BaseException(
                    HttpStatus.NOT_FOUND,
                    ErrorMessages.BOOK_NOT_FOUND,
                    "Book with ID " + id + " not found. Cannot delete."
            );
        }
        bookRepository.deleteById(id);
    }

    @Override
    public BookResponse findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BaseException(
                        HttpStatus.NOT_FOUND,
                        ErrorMessages.BOOK_NOT_FOUND,
                        "Book with ID " + id + " not found."
                ));
        return convertToResponseDto(book);
    }

    @Override
    public BookResponse findBookByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "Book name cannot be empty for search.");
        }
        Book book = bookRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new BaseException(
                        HttpStatus.NOT_FOUND,
                        ErrorMessages.BOOK_NOT_FOUND,
                        "Book with name '" + name + "' not found."
                ));
        return convertToResponseDto(book);
    }

    @Override
    public List<BookResponse> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> findBookByAuthor(String authorCriteria) {
        if (authorCriteria == null || authorCriteria.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Kriterlere uyan yazarları bul
        Set<Author> matchingAuthors = authorRepository.findByPenNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrSecondNameContainingIgnoreCase(
                authorCriteria, authorCriteria, authorCriteria
        );

        if (matchingAuthors.isEmpty()) {
            return Collections.emptyList();
        }

        // Bu yazarlara ait kitapları bul
        List<Book> booksByAuthors = bookRepository.findByAuthorsIn(matchingAuthors);

        return booksByAuthors.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> findBookByGenre(String genreName) {
        if (genreName == null || genreName.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Adına göre tür bul
        BookGenre genre = bookGenreRepository.findByNameIgnoreCase(genreName)
                .orElseThrow(() -> new BaseException(
                        HttpStatus.NOT_FOUND,
                        ErrorMessages.BOOKGENRE_NOT_FOUND,
                        "Book genre with name '" + genreName + "' not found."
                ));

        // Bu türe ait kitapları bul
        List<Book> booksByGenre = bookRepository.findByGenresContains(genre);

        return booksByGenre.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookResponse> findBookByPublisher(String publisherName) {
        if (publisherName == null || publisherName.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // Adına göre yayıncı bul
        Publisher publisher = publisherRepository.findByNameIgnoreCase(publisherName)
                .orElseThrow(() -> new BaseException(
                        HttpStatus.NOT_FOUND,
                        ErrorMessages.PUBLISHER_NOT_FOUND,
                        "Publisher with name '" + publisherName + "' not found."
                ));

        // Güncellenmiş repository metodu ile yayıncıya göre kitapları bul
        List<Book> booksByPublisher = bookRepository.findByPublishersContains(publisher);

        return booksByPublisher.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }


    //>>>>>>>>>>>>>> HELPER METHODS

    private BookResponse convertToResponseDto(Book book) {
        if (book == null) {
            throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, "Book entity is null during conversion.");
        }
        return BookResponse.builder()
                .name(book.getName())
                .status(book.getStatus())
                .authors(book.getAuthors())
                .publisher(book.getPublishers())
                .genres(book.getGenres())
                .build();
    }
}
