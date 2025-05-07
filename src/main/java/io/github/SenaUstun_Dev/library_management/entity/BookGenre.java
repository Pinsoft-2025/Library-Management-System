package io.github.SenaUstun_Dev.library_management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "book_genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

     @ManyToMany(mappedBy = "genres")
     private Set<Book> books;
}
