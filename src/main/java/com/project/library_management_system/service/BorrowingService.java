package com.project.library_management_system.service;


import com.project.library_management_system.Entity.Book;
import com.project.library_management_system.Entity.BorrowingRecord;
import com.project.library_management_system.Entity.Patron;
import com.project.library_management_system.exception.AlreadyBorrowedException;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.repository.BorrowingRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookService bookService;
    private final PatronService patronService;

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookService.getBookById(bookId);
        Patron patron = patronService.getPatronById(patronId);

        // Check if the book is already borrowed
        if (!borrowingRecordRepository.findActiveBorrowingByBookId(bookId).isEmpty()) {
            throw new AlreadyBorrowedException("Book is already borrowed");
        }

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setStatus(BorrowingRecord.BorrowingStatus.BORROWED);
        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        BorrowingRecord record = borrowingRecordRepository
                .findByBookAndPatronAndStatus(bookId, patronId, BorrowingRecord.BorrowingStatus.BORROWED)
                .orElseThrow(() -> new ResourceNotFoundException("No active borrowing record found"));

        record.setReturnedDate(LocalDateTime.now());
        record.setStatus(BorrowingRecord.BorrowingStatus.RETURNED);
        return borrowingRecordRepository.save(record);
    }
}
