package io.github.SenaUstun_Dev.library_management.service;

import java.util.List;

import io.github.SenaUstun_Dev.library_management.dto.request.BorrowBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.BorrowedBookResponse;
import io.github.SenaUstun_Dev.library_management.entity.AppUser;

public interface BorrowedBookService {
    /*
    * user will be able keep  only 3 borrowed books at the same time, meaning:
    * method will take the info of current active user and the book info that they want to borrow
    * it will checks user's borrowingPrivilage:
    *   if its  FULL: it will add 30 days to borrowDate to calculate returnDate,
                LIMITED: it will add 15 days to borrowDate to calculate returnDate,
                UNABLE: it will return user a message "Birden fazla kez ödünç alınan kitapları geri getirmediğiz tespit edilmiştir. Kitap ödünç almak hakkınız iptal edilmiştir.Bir yanlışlık olduğunu düşünüyorsanız ilgili birimle iletişime geçiniz."
    * it will check how many books the user already has borrowed.
    * if borrowedBooks (not equal or less than 2): it will return a message "Aynı anda en fazla 3 tane ödünç kitaba sahip olunabilir."
    *                  (equal or less than 2): it will add the book to user's borrowedBooks list and return borrowedBooksResponse
    * */
    
    BorrowedBookResponse borrowBook(AppUser user, BorrowBookRequest request);
    
    // Kitap iade işlemi
    BorrowedBookResponse returnBook(AppUser user, Long bookId);
    
    // Kullanıcının aktif olarak ödünç aldığı kitaplar
    List<BorrowedBookResponse> getCurrentlyBorrowedBooks(AppUser user);
    
    // Kullanıcının geçmiş ödünç aldığı kitaplar 
    List<BorrowedBookResponse> getBorrowedBookHistory(AppUser user);
    
    // [ADMIN] Herhangi bir kullanıcının aktif olarak ödünç aldığı kitaplar
    List<BorrowedBookResponse> getUserCurrentlyBorrowedBooks(Long userId);
    
    // [ADMIN] Herhangi bir kullanıcının geçmiş ödünç kitap geçmişi
    List<BorrowedBookResponse> getUserBorrowedBookHistory(Long userId);
    
    // [ADMIN] Kayıp olarak işaretlenmiş tüm ödünç kitapları getir
    List<BorrowedBookResponse> getAllLostBooks();
}
