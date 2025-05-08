package io.github.SenaUstun_Dev.library_management.repository;

import io.github.SenaUstun_Dev.library_management.entity.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {
    BookGenre findByNameIgnoreCase(String name);
}
