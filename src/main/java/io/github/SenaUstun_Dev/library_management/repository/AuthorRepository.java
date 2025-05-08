package io.github.SenaUstun_Dev.library_management.repository;

import io.github.SenaUstun_Dev.library_management.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE " +
            "(:penName IS NULL OR lower(a.penName) LIKE lower(concat('%', :penName, '%'))) AND " +
            "(:firstName IS NULL OR lower(a.firstName) LIKE lower(concat('%', :firstName, '%'))) AND " +
            "(:secondName IS NULL OR lower(a.secondName) LIKE lower(concat('%', :secondName, '%')))")
    List<Author> findByCriteria(@Param("penName") String penName,
                                @Param("firstName") String firstName,
                                @Param("secondName") String secondName);
}
