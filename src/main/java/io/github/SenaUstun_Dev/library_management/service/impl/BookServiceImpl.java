package io.github.SenaUstun_Dev.library_management.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.SenaUstun_Dev.library_management.dto.request.CreateBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.AuthorResponse;
import io.github.SenaUstun_Dev.library_management.dto.response.BookGenreResponse;
import io.github.SenaUstun_Dev.library_management.dto.response.BookResponse;
import io.github.SenaUstun_Dev.library_management.dto.response.PublisherResponse;
import io.github.SenaUstun_Dev.library_management.entity.Author;
import io.github.SenaUstun_Dev.library_management.entity.Book;
import io.github.SenaUstun_Dev.library_management.entity.BookGenre;
import io.github.SenaUstun_Dev.library_management.entity.Publisher;
import io.github.SenaUstun_Dev.library_management.exception.BaseException;
import io.github.SenaUstun_Dev.library_management.exception.ErrorMessages;
import io.github.SenaUstun_Dev.library_management.repository.AuthorRepository;
import io.github.SenaUstun_Dev.library_management.repository.BookGenreRepository;
import io.github.SenaUstun_Dev.library_management.repository.BookRepository;
import io.github.SenaUstun_Dev.library_management.repository.PublisherRepository;
import io.github.SenaUstun_Dev.library_management.service.BookService;
import lombok.RequiredArgsConstructor;

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

        // İlişkili entity'leri ID'lerine göre bulma
        Set<Author> authors = new HashSet<>();
        if (request.authorIds() != null && !request.authorIds().isEmpty()) {
            for (Long authorId : request.authorIds()) {
                Author author = authorRepository.findById(authorId)
                        .orElseThrow(() -> new BaseException(
                                HttpStatus.NOT_FOUND,
                                ErrorMessages.AUTHOR_NOT_FOUND,
                                "Yazar ID: " + authorId + " bulunamadı."
                        ));
                authors.add(author);
            }
        }

        Set<Publisher> publishers = new HashSet<>();
        if (request.publisherIds() != null && !request.publisherIds().isEmpty()) {
            for (Long publisherId : request.publisherIds()) {
                Publisher publisher = publisherRepository.findById(publisherId)
                        .orElseThrow(() -> new BaseException(
                                HttpStatus.NOT_FOUND,
                                ErrorMessages.PUBLISHER_NOT_FOUND,
                                "Yayıncı ID: " + publisherId + " bulunamadı."
                        ));
                publishers.add(publisher);
            }
        }

        Set<BookGenre> genres = new HashSet<>();
        if (request.genreIds() != null && !request.genreIds().isEmpty()) {
            for (Long genreId : request.genreIds()) {
                BookGenre genre = bookGenreRepository.findById(genreId)
                        .orElseThrow(() -> new BaseException(
                                HttpStatus.NOT_FOUND,
                                ErrorMessages.BOOKGENRE_NOT_FOUND,
                                "Kitap türü ID: " + genreId + " bulunamadı."
                        ));
                genres.add(genre);
            }
        }

        // Book nesnesi oluştur
        Book book = Book.builder()
                .name(request.name())
                .status(request.status())
                .authors(authors)
                .publishers(publishers)
                .genres(genres)
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
        if (request.authorIds() != null) {
            Set<Author> authors = new HashSet<>();
            for (Long authorId : request.authorIds()) {
                Author author = authorRepository.findById(authorId)
                        .orElseThrow(() -> new BaseException(
                                HttpStatus.NOT_FOUND,
                                ErrorMessages.AUTHOR_NOT_FOUND,
                                "Yazar ID: " + authorId + " bulunamadı."
                        ));
                authors.add(author);
            }
            bookToUpdate.setAuthors(authors);
        }

        if (request.publisherIds() != null) {
            Set<Publisher> publishers = new HashSet<>();
            for (Long publisherId : request.publisherIds()) {
                Publisher publisher = publisherRepository.findById(publisherId)
                        .orElseThrow(() -> new BaseException(
                                HttpStatus.NOT_FOUND,
                                ErrorMessages.PUBLISHER_NOT_FOUND,
                                "Yayıncı ID: " + publisherId + " bulunamadı."
                        ));
                publishers.add(publisher);
            }
            bookToUpdate.setPublishers(publishers);
        }

        if (request.genreIds() != null) {
            Set<BookGenre> genres = new HashSet<>();
            for (Long genreId : request.genreIds()) {
                BookGenre genre = bookGenreRepository.findById(genreId)
                        .orElseThrow(() -> new BaseException(
                                HttpStatus.NOT_FOUND,
                                ErrorMessages.BOOKGENRE_NOT_FOUND,
                                "Kitap türü ID: " + genreId + " bulunamadı."
                        ));
                genres.add(genre);
            }
            bookToUpdate.setGenres(genres);
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public List<BookResponse> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

        // Entity'leri DTO'lara dönüştürme
        Set<AuthorResponse> authorResponses = (book.getAuthors() == null) ?
                Collections.emptySet() :
                book.getAuthors().stream()
                        .filter(Objects::nonNull)
                        .map(this::convertToAuthorResponse)
                        .collect(Collectors.toSet());

        Set<PublisherResponse> publisherResponses = (book.getPublishers() == null) ?
                Collections.emptySet() :
                book.getPublishers().stream()
                        .filter(Objects::nonNull)
                        .map(this::convertToPublisherResponse)
                        .collect(Collectors.toSet());

        Set<BookGenreResponse> genreResponses = (book.getGenres() == null) ?
                Collections.emptySet() :
                book.getGenres().stream()
                        .filter(Objects::nonNull)
                        .map(this::convertToGenreResponse)
                        .collect(Collectors.toSet());

        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .status(book.getStatus())
                .authors(authorResponses)
                .publishers(publisherResponses)
                .genres(genreResponses)
                .build();
    }

    private AuthorResponse convertToAuthorResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .penName(author.getPenName())
                .firstName(author.getFirstName())
                .secondName(author.getSecondName())
                .build();
    }

    private PublisherResponse convertToPublisherResponse(Publisher publisher) {
        return PublisherResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }

    private BookGenreResponse convertToGenreResponse(BookGenre genre) {
        return BookGenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
