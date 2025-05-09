package io.github.SenaUstun_Dev.library_management.repository;

import io.github.SenaUstun_Dev.library_management.entity.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {
    Optional<BookGenre> findByNameIgnoreCase(String name);
}
