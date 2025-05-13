package io.github.SenaUstun_Dev.library_management.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.SenaUstun_Dev.library_management.dto.request.BorrowBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.AppUserResponse;
import io.github.SenaUstun_Dev.library_management.dto.response.BookResponse;
import io.github.SenaUstun_Dev.library_management.dto.response.BorrowedBookResponse;
import io.github.SenaUstun_Dev.library_management.entity.AppUser;
import io.github.SenaUstun_Dev.library_management.entity.Book;
import io.github.SenaUstun_Dev.library_management.entity.BorrowedBook;
import io.github.SenaUstun_Dev.library_management.entity.enums.BookStatus;
import io.github.SenaUstun_Dev.library_management.entity.enums.BorrowingPrivilege;
import io.github.SenaUstun_Dev.library_management.exception.BaseException;
import io.github.SenaUstun_Dev.library_management.exception.ErrorMessages;
import io.github.SenaUstun_Dev.library_management.repository.BookRepository;
import io.github.SenaUstun_Dev.library_management.repository.BorrowedBookRepository;
import io.github.SenaUstun_Dev.library_management.service.BorrowedBookService;
import io.github.SenaUstun_Dev.library_management.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BorrowedBookServiceImpl implements BorrowedBookService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Override
    public BorrowedBookResponse borrowBook(AppUser user, BorrowBookRequest request) {
        // Kullanıcının ödünç alma iznini kontrol etme
        if (user.getBorrowingPrivilege() == BorrowingPrivilege.UNABLE) {
            throw new BaseException(
                    HttpStatus.FORBIDDEN, 
                    ErrorMessages.BORROWING_NOT_ALLOWED,
                    "Birden fazla kez ödünç alınan kitapları geri getirmediğiz tespit edilmiştir. Kitap ödünç almak hakkınız iptal edilmiştir. Bir yanlışlık olduğunu düşünüyorsanız ilgili birimle iletişime geçiniz."
            );
        }
        
        // Kullanıcının mevcut ödünç aldığı kitap sayısını kontrol etme
        int activeBorrowedBooksCount = borrowedBookRepository.countActiveBorrowedBooksByUser(user);
        if (activeBorrowedBooksCount >= 3) {
            throw new BaseException(
                    HttpStatus.BAD_REQUEST, 
                    ErrorMessages.MAX_BOOKS_EXCEEDED,
                    "Aynı anda en fazla 3 tane ödünç kitaba sahip olunabilir."
            );
        }
        
        // İstenen kitabı bulma
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new BaseException(
                        HttpStatus.NOT_FOUND, 
                        ErrorMessages.BOOK_NOT_FOUND,
                        "Kitap bulunamadı. ID: " + request.bookId()
                ));
        
        // Kitabın ödünç alınabilir olup olmadığını kontrol etme
        if (book.getStatus() != BookStatus.ACTIVE) {
            throw new BaseException(
                    HttpStatus.BAD_REQUEST, 
                    ErrorMessages.BOOK_NOT_AVAILABLE,
                    "Kitap şu anda ödünç alınamaz. Durum: " + book.getStatus()
            );
        }
        
        // Kullanıcının ödünç alma iznine göre iade tarihini hesaplama
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate;
        
        if (user.getBorrowingPrivilege() == BorrowingPrivilege.FULL) {
            returnDate = borrowDate.plusDays(30);
        } else if (user.getBorrowingPrivilege() == BorrowingPrivilege.LIMITED) {
            returnDate = borrowDate.plusDays(15);
        } else {
            throw new BaseException(
                    HttpStatus.INTERNAL_SERVER_ERROR, 
                    ErrorMessages.UNEXPECTED_ERROR,
                    "Beklenmeyen bir hata oluştu. User BorrowingPrivilege değeri null veya sorunlu olabilir?"
            );
        }
        
        // Yeni bir ödünç kitap kaydı oluşturma
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setUser(user);
        borrowedBook.setBook(book);
        borrowedBook.setBorrowDate(borrowDate);
        borrowedBook.setReturnDate(returnDate);
        borrowedBook.setLost(false);
        
        // Kitabın durumunu güncelleme
        book.setStatus(BookStatus.BORROWED);
        
        // Veritabanına kaydetme
        BorrowedBook savedBorrowedBook = borrowedBookRepository.save(borrowedBook);
        bookRepository.save(book);
        
        // Response oluşturma
        return convertToResponse(savedBorrowedBook);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BorrowedBookResponse> getCurrentlyBorrowedBooks(AppUser user) {
        // Kullanıcının şu anda ödünç aldığı kitapları al
        List<BorrowedBook> currentlyBorrowedBooks = borrowedBookRepository.findByUserAndReturnDateIsNull(user);
        
        // Boş veya dolu liste olduğunu gözetmeksizin sonuçları DTO'ya dönüştür ve döndür
        return currentlyBorrowedBooks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BorrowedBookResponse> getBorrowedBookHistory(AppUser user) {
        // Kullanıcının geçmiş ödünç aldığı kitapları al
        List<BorrowedBook> borrowedBookHistory = borrowedBookRepository.findByUserAndReturnDateIsNotNull(user);
        
        // Boş veya dolu liste olduğunu gözetmeksizin sonuçları DTO'ya dönüştür ve döndür
        return borrowedBookHistory.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BorrowedBookResponse> getUserCurrentlyBorrowedBooks(Long userId) {
        // Kullanıcıyı bul
        AppUser user = userService.findUserEntityById(userId);
        
        // Kullanıcının şu anda ödünç aldığı kitapları al
        List<BorrowedBook> currentlyBorrowedBooks = borrowedBookRepository.findByUserIdAndReturnDateIsNull(userId);
        
        // Boş veya dolu liste olduğunu gözetmeksizin sonuçları DTO'ya dönüştür ve döndür
        return currentlyBorrowedBooks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BorrowedBookResponse> getUserBorrowedBookHistory(Long userId) {
        // Kullanıcıyı bul
        AppUser user = userService.findUserEntityById(userId);
        
        // Kullanıcının geçmiş ödünç aldığı kitapları al
        List<BorrowedBook> borrowedBookHistory = borrowedBookRepository.findByUserIdAndReturnDateIsNotNull(userId);
        
        // Boş veya dolu liste olduğunu gözetmeksizin sonuçları DTO'ya dönüştür ve döndür
        return borrowedBookHistory.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Entity'i DTO'ya dönüştürme
    private BorrowedBookResponse convertToResponse(BorrowedBook borrowedBook) {
        AppUserResponse userResponse = new AppUserResponse(
                borrowedBook.getUser().getUsername(),
                borrowedBook.getUser().getEmail(),
                borrowedBook.getUser().getFirstName(),
                borrowedBook.getUser().getLastName()
        );
        
        BookResponse bookResponse = BookResponse.builder()
                .id(borrowedBook.getBook().getId())
                .name(borrowedBook.getBook().getName())
                .status(borrowedBook.getBook().getStatus())
                .build();
        
        return BorrowedBookResponse.builder()
                .id(borrowedBook.getId())
                .borrowDate(borrowedBook.getBorrowDate())
                .returnDate(borrowedBook.getReturnDate())
                .lost(borrowedBook.isLost())
                .user(userResponse)
                .book(bookResponse)
                .build();
    }
} 