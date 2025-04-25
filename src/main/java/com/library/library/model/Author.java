package com.library.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Entity(name = "Author")
@Table(name = "author")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {
    @Id
    @SequenceGenerator(
            name = "author_sequence",
            sequenceName = "auther_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "author_sequence",
            strategy = SEQUENCE
    )
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @ManyToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", books=" + books +
                '}';
    }
}
