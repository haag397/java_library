package com.library.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Entity(name = "AppUser")
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name ="user_name_email_constraint",
                columnNames = {"user_name", "email"}),
                @UniqueConstraint(name = "unique_mobile",
                columnNames = {"mobile"})
        })
@Getter
@Setter
@NoArgsConstructor
public class AppUser {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1)
    @GeneratedValue(
            generator = "user_sequence",
            strategy = SEQUENCE)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "first_name", nullable = false,length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false,length = 100)
    private String lastName;

    @Column(name = "user_name", nullable = false,length = 100,unique = true)
    private String userName;

    @JsonIgnore
    @Column(name = "password", nullable = false,length = 200)
    private String password;

    @Column(name = "email", nullable = false,length = 100,unique = true)
    private String email;

    @Column(name = "mobile", nullable = false,length = 50,unique = true)
    private String mobile;

    @OneToMany(mappedBy = "appUser")
    private List<Book> books = new ArrayList<>();

    public AppUser(String firstName, String lastName, String userName, String password, String email, String mobile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", books=" + books +
                '}';
    }
}
