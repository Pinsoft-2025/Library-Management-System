package io.github.SenaUstun_Dev.library_management.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.SenaUstun_Dev.library_management.entity.AppUser;
import io.github.SenaUstun_Dev.library_management.entity.Book;
import io.github.SenaUstun_Dev.library_management.entity.BorrowedBook;
import io.github.SenaUstun_Dev.library_management.entity.enums.BookStatus;
import io.github.SenaUstun_Dev.library_management.entity.enums.BorrowingPrivilege;
import io.github.SenaUstun_Dev.library_management.repository.BookRepository;
import io.github.SenaUstun_Dev.library_management.repository.BorrowedBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTaskService {
    /*
     * Checks whether borrowed books have been returned before their return date; if not, marks them as lost
     * -----------------------------------------------------------------------------------------------------
     * Run every time the clock strikes 00:00 (12:00 AM)
     * find all borrowed books
     * if (localdatetime.now is more future than return date): borrowed book lost = true
     * else do nothing
     *
     * if a book gets marked as lost find it's user (the one who borrowed)
     * if users borrowingprivilage is FULL make it LIMITED
     *                             is LIMITED make it UNABLE
     *                             is UNABLE do nothing. say its already UNABLE
     * */

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Her gün gece yarısı (00:00:00) çalışır
    @Transactional
    public void checkOverdueBooks() {
        log.info("İade tarihi geçmiş kitapları kontrol etme görevi başlatıldı");

        LocalDate today = LocalDate.now();

        // Veritabanından doğrudan son iade tarihi geçmiş, iade edilmemiş ve kayıp olarak işaretlenmemiş kitapları getir
        List<BorrowedBook> overdueBorrowedBooks = borrowedBookRepository.findByDueDateLessThanAndLostIsFalseAndActualReturnDateIsNull(today);

        log.info("İade tarihi geçmiş ve işaretlenmemiş ödünç kitap sayısı: {}", overdueBorrowedBooks.size());

        // İade tarihi geçmiş kayıtları işaretle
        for (BorrowedBook borrowedBook : overdueBorrowedBooks) {
            log.info("İade tarihi geçmiş kitap bulundu - ID: {}, Kitap: {}, Kullanıcı: {}",
                    borrowedBook.getId(),
                    borrowedBook.getBook().getName(),
                    borrowedBook.getUser().getUsername());

                borrowedBook.setLost(true);

            // Kitabın durumunu LOST olarak güncelle
            Book book = borrowedBook.getBook();
            book.setStatus(BookStatus.LOST);
            bookRepository.save(book);

            // Kullanıcının kayıp kitap sayısını artır
            AppUser user = borrowedBook.getUser();
            user.setLostBookCount(user.getLostBookCount() + 1);

            // Kullanıcının ödünç alma ayrıcalığını düşür
            updateUserBorrowingPrivilege(user);

            // Değişiklikleri kaydet
            borrowedBookRepository.save(borrowedBook);
        }

        log.info("İade tarihi geçmiş kitapları kontrol etme görevi tamamlandı");
    }

    /**
     * Kullanıcının ödünç alma ayrıcalığını düşürür.
     * FULL -> LIMITED, LIMITED -> UNABLE şeklinde düşer.
     * UNABLE durumunda değişiklik yapılmaz.
     */
    private void updateUserBorrowingPrivilege(AppUser user) {
        BorrowingPrivilege currentPrivilege = user.getBorrowingPrivilege();

        switch (currentPrivilege) {
            case FULL:
                user.setBorrowingPrivilege(BorrowingPrivilege.LIMITED);
                log.info("Kullanıcı {} ayrıcalığı FULL -> LIMITED olarak düşürüldü", user.getUsername());
                break;

            case LIMITED:
                user.setBorrowingPrivilege(BorrowingPrivilege.UNABLE);
                log.info("Kullanıcı {} ayrıcalığı LIMITED -> UNABLE olarak düşürüldü", user.getUsername());
                break;

            case UNABLE:
                // Zaten en düşük seviyede, değişiklik yapma
                log.info("Kullanıcı {} ayrıcalığı zaten UNABLE durumunda", user.getUsername());
                break;
        }
    }
}