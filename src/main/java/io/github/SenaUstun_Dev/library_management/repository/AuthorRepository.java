package io.github.SenaUstun_Dev.library_management.repository;

import io.github.SenaUstun_Dev.library_management.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
