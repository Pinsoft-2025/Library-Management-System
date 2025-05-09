package io.github.SenaUstun_Dev.library_management.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.SenaUstun_Dev.library_management.entity.Author;
import io.github.SenaUstun_Dev.library_management.entity.Book;
import io.github.SenaUstun_Dev.library_management.entity.BookGenre;
import io.github.SenaUstun_Dev.library_management.entity.Publisher;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenresContains(BookGenre genre);

    List<Book> findByPublishersContains(Publisher publisher);

    List<Book> findByAuthorsIn(Set<Author> matchingAuthors);

    Optional<Book> findByNameIgnoreCase(String name);
}
