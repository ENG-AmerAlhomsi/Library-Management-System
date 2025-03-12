package com.project.library_management_system.repository;

import com.project.library_management_system.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Object> findByIsbn(String isbn);
}
