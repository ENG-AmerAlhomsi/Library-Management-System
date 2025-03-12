package com.project.library_management_system.repository;
import com.project.library_management_system.Entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Year;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testSaveBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("123-4567890123");
        book.setPublicationYear(Year.of(2000));

        Book savedBook = bookRepository.save(book);
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Test Book");
        assertThat(savedBook.getAuthor()).isEqualTo("Test Author");
        assertThat(savedBook.getIsbn()).isEqualTo("123-4567890123");
        assertThat(savedBook.getPublicationYear()).isEqualTo(Year.of(2000));
    }

    @Test
    public void testFindBookById() {
        Book book = new Book();
        book.setTitle("Find Me");
        book.setAuthor("Test Author");
        book.setIsbn("123-4567890123");
        book.setPublicationYear(Year.of(2000));
        entityManager.persist(book);

        Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Find Me");
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book();
        book.setTitle("To Delete");
        book.setAuthor("Test Author");
        book.setIsbn("123-4567890123");
        book.setPublicationYear(Year.of(2000));
        entityManager.persist(book);

        bookRepository.deleteById(book.getId());
        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }
}
