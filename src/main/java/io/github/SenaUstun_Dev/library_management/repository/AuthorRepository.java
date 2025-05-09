package io.github.SenaUstun_Dev.library_management.repository;

import io.github.SenaUstun_Dev.library_management.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE " +
            "(:penName IS NULL OR lower(a.penName) LIKE lower(concat('%', :penName, '%'))) AND " +
            "(:firstName IS NULL OR lower(a.firstName) LIKE lower(concat('%', :firstName, '%'))) AND " +
            "(:secondName IS NULL OR lower(a.secondName) LIKE lower(concat('%', :secondName, '%')))")
    List<Author> findByCriteria(@Param("penName") String penName,
                                @Param("firstName") String firstName,
                                @Param("secondName") String secondName);

    Set<Author> findByPenNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrSecondNameContainingIgnoreCase(String authorCriteria, String authorCriteria1, String authorCriteria2);
}
