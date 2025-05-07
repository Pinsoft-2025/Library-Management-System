package io.github.SenaUstun_Dev.library_management.repository;

import io.github.SenaUstun_Dev.library_management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
