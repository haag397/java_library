//package com.library.library.repository;
//
//import com.library.library.model.Token;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface TokenRepository extends JpaRepository<Token, Long> {
//    @Query("""
//        SELECT t FROM Token t
//        JOIN t.appUser u
//        WHERE u.id = :userId AND (t.expired = false OR t.revoked = false)
//    """)
//    List<Token> findAllValidTokenByUser(@Param("userId") Long userId);
//
//    Optional<Token> findByToken(String token);
//
//}
