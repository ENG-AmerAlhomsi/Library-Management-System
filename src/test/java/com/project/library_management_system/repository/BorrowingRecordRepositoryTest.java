package com.project.library_management_system.repository;

import com.project.library_management_system.Entity.Book;
import com.project.library_management_system.Entity.BorrowingRecord;
import com.project.library_management_system.Entity.Patron;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BorrowingRecordRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    private Book createTestBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("123-4567890123");
        book.setPublicationYear(Year.of(2000));
        return entityManager.persist(book);
    }

    private Patron createTestPatron() {
        Patron patron = new Patron();
        patron.setName("Test Patron");
        patron.setEmail("test@example.com");
        patron.setPhoneNumber("123-456-7890");
        patron.setContactInfo("https://github.com/ENG-AmerAlhomsi");
        return entityManager.persist(patron);
    }

    @Test
    public void testCreateBorrowingRecord() {
        Book book = createTestBook();
        Patron patron = createTestPatron();

        BorrowingRecord record = BorrowingRecord.builder()
                .book(book)
                .patron(patron)
                .status(BorrowingRecord.BorrowingStatus.BORROWED)
                .build();

        BorrowingRecord savedRecord = borrowingRecordRepository.save(record);
        assertThat(savedRecord.getId()).isNotNull();
        assertThat(savedRecord.getBook().getId()).isEqualTo(book.getId());
        assertThat(savedRecord.getStatus()).isEqualTo(BorrowingRecord.BorrowingStatus.BORROWED);
    }

    @Test
    public void testFindBorrowingRecordById() {
        Book book = createTestBook();
        Patron patron = createTestPatron();

        BorrowingRecord record = BorrowingRecord.builder()
                .book(book)
                .patron(patron)
                .status(BorrowingRecord.BorrowingStatus.BORROWED)
                .build();
        entityManager.persist(record);

        Optional<BorrowingRecord> foundRecord = borrowingRecordRepository.findById(record.getId());
        assertThat(foundRecord).isPresent();
        assertThat(foundRecord.get().getBook().getTitle()).isEqualTo("Test Book");
    }

    @Test
    public void testUpdateReturnDate() {
        Book book = createTestBook();
        Patron patron = createTestPatron();

        BorrowingRecord record = BorrowingRecord.builder()
                .book(book)
                .patron(patron)
                .status(BorrowingRecord.BorrowingStatus.BORROWED)
                .build();
        entityManager.persist(record);

        LocalDateTime returnDate = LocalDateTime.now();
        record.setReturnedDate(returnDate);
        record.setStatus(BorrowingRecord.BorrowingStatus.RETURNED);
        borrowingRecordRepository.save(record);

        BorrowingRecord updatedRecord = entityManager.find(BorrowingRecord.class, record.getId());
        assertThat(updatedRecord.getReturnedDate()).isEqualTo(returnDate);
        assertThat(updatedRecord.getStatus()).isEqualTo(BorrowingRecord.BorrowingStatus.RETURNED);
    }
}
