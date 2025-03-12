package com.project.library_management_system.service;


import com.project.library_management_system.Entity.Book;
import com.project.library_management_system.Entity.BorrowingRecord;
import com.project.library_management_system.Entity.Patron;
import com.project.library_management_system.exception.AlreadyBorrowedException;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.repository.BookRepository;
import com.project.library_management_system.repository.BorrowingRecordRepository;
import com.project.library_management_system.repository.PatronRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found"));

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setStatus(BorrowingRecord.BorrowingStatus.BORROWED);
        return borrowingRecordRepository.save(record);
    }

    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        BorrowingRecord record = borrowingRecordRepository
                .findByBook_IdAndPatron_IdAndStatus(bookId, patronId, BorrowingRecord.BorrowingStatus.BORROWED)
                .orElseThrow(() -> new ResourceNotFoundException("No active borrowing record found"));

        record.setReturnedDate(LocalDateTime.now());
        record.setStatus(BorrowingRecord.BorrowingStatus.RETURNED);
        return borrowingRecordRepository.save(record);
    }
}
