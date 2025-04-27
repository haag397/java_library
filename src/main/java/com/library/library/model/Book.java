package com.library.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Book")
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Book {
    @Id
    @SequenceGenerator(
            name = "book_sequence" ,
            sequenceName = "book_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "book_sequence",
            strategy = SEQUENCE)
    @Column(name = "id", nullable = false, updatable = false,unique = true)
    private Long id;

    @Column(name = "book_title", nullable = false, length = 150)
    private String bookTitle;

    @Column(name = "publisher", nullable = false, length = 150)
    private String publisher;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne()
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_id_fk")
    )
    private User users;

    @ManyToMany()
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"),
            foreignKey = @ForeignKey(name = "book_id_fk")
    )
    private List<Author> authors = new ArrayList<>();

    @ManyToMany()
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id",
                    foreignKey = @ForeignKey(name = "book_id_fk")),
            inverseJoinColumns = @JoinColumn(name = "category_id",
                    foreignKey = @ForeignKey(name = "category_id_fk"))
    )
    private List<Category> categories = new ArrayList<>();

    public Book(String bookTitle, String publisher, BigDecimal price, int quantity, User users, List<Author> authors, List<Category> categories) {
        this.bookTitle = bookTitle;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
        this.users = users;
        this.authors = authors;
        this.categories = categories;
    }
}
