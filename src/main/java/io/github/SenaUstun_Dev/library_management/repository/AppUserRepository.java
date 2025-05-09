package io.github.SenaUstun_Dev.library_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.SenaUstun_Dev.library_management.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findByFirstNameContainingIgnoreCase(String firstName);
    List<AppUser> findByLastNameContainingIgnoreCase(String lastName);
    Optional<AppUser> findByEmail(String email);
}
