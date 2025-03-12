package com.project.library_management_system.service;

import com.project.library_management_system.Entity.Book;
import com.project.library_management_system.Entity.BorrowingRecord;
import com.project.library_management_system.Entity.Patron;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.repository.BookRepository;
import com.project.library_management_system.repository.BorrowingRecordRepository;
import com.project.library_management_system.repository.PatronRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingRecordServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingService borrowingRecordService;

    private Book createMockBook(Long id) {
        Book book = new Book();
        book.setId(id);
        book.setTitle("To Delete");
        book.setAuthor("Test Author");
        book.setIsbn("123-4567890123");
        book.setPublicationYear(Year.of(2000));
        return book;
    }

    private Patron createMockPatron(Long id) {
        Patron patron = new Patron();
        patron.setId(id);
        patron.setName("Jane Smith");
        patron.setEmail("john@example.com");
        patron.setPhoneNumber("123-456-7890");
        patron.setContactInfo("https://github.com/ENG-AmerAlhomsi");
        return patron;
    }

    @Test
    public void testBorrowBookSuccess() {
        Long bookId = 1L;
        Long patronId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(createMockBook(bookId)));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(createMockPatron(patronId)));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> {
            BorrowingRecord record = invocation.getArgument(0);
            record.setId(1L);
            return record;
        });

        BorrowingRecord result = borrowingRecordService.borrowBook(bookId, patronId);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BorrowingRecord.BorrowingStatus.BORROWED);
        verify(borrowingRecordRepository).save(any(BorrowingRecord.class));
    }

    @Test
    public void testBorrowBookBookNotFound() {
        Long bookId = 1L;
        Long patronId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingRecordService.borrowBook(bookId, patronId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found");
    }

    @Test
    public void testReturnBookSuccess() {
        Long bookId = 1L;
        Long patronId = 1L;

        // Create a complete BorrowingRecord with book and patron
        Book book = new Book();
        book.setId(bookId);
        Patron patron = new Patron();
        patron.setId(patronId);

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setStatus(BorrowingRecord.BorrowingStatus.BORROWED);

        // Match the corrected method name
        when(borrowingRecordRepository.findByBook_IdAndPatron_IdAndStatus(
                eq(bookId),
                eq(patronId),
                eq(BorrowingRecord.BorrowingStatus.BORROWED)
        )).thenReturn(Optional.of(record));

        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenAnswer(invocation -> {
            BorrowingRecord saved = invocation.getArgument(0);
            saved.setReturnedDate(LocalDateTime.now());
            saved.setStatus(BorrowingRecord.BorrowingStatus.RETURNED);
            return saved;
        });

        BorrowingRecord result = borrowingRecordService.returnBook(bookId, patronId);

        assertThat(result.getStatus()).isEqualTo(BorrowingRecord.BorrowingStatus.RETURNED);
        assertThat(result.getReturnedDate()).isNotNull();
        verify(borrowingRecordRepository).save(record);
    }

    @Test
    public void testReturnBookNotFound() {
        Long bookId = 1L;
        Long patronId = 1L;

        // Use the correct method name with underscores
        when(borrowingRecordRepository.findByBook_IdAndPatron_IdAndStatus(
                eq(bookId),
                eq(patronId),
                eq(BorrowingRecord.BorrowingStatus.BORROWED)
        )).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingRecordService.returnBook(bookId, patronId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No active borrowing record found");
    }
}
