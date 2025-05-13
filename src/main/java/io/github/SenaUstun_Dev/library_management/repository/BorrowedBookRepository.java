package io.github.SenaUstun_Dev.library_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.SenaUstun_Dev.library_management.entity.AppUser;
import io.github.SenaUstun_Dev.library_management.entity.BorrowedBook;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    // Aktif olarak ödünç alınan kitaplar (iade edilmemiş)
    List<BorrowedBook> findByUserAndReturnDateIsNull(AppUser user);
    
    // Geçmiş ödünç kitap kayıtları (iade edilmiş)
    List<BorrowedBook> findByUserAndReturnDateIsNotNull(AppUser user);
    
    // Belli bir kullanıcının aktif ödünç kitapları - Admin için
    List<BorrowedBook> findByUserIdAndReturnDateIsNull(Long userId);
    
    // Belli bir kullanıcının geçmiş ödünç kitapları - Admin için
    List<BorrowedBook> findByUserIdAndReturnDateIsNotNull(Long userId);
    
    @Query("SELECT COUNT(b) FROM BorrowedBook b WHERE b.user = :user AND b.returnDate IS NULL")
    int countActiveBorrowedBooksByUser(@Param("user") AppUser user);
}
