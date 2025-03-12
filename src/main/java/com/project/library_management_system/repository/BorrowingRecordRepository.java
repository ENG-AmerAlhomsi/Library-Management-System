package com.project.library_management_system.repository;

import com.project.library_management_system.Entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    // Check if a book is currently borrowed
    @Query("SELECT br FROM BorrowingRecord br WHERE br.book.id = :bookId AND br.status = 'BORROWED'")
    List<BorrowingRecord> findActiveBorrowingByBookId(@Param("bookId") Long bookId);

    // Find a specific borrowing record
    @Query("SELECT br FROM BorrowingRecord br WHERE br.book.id = :bookId AND br.patron.id = :patronId AND br.status = :status")
    Optional<BorrowingRecord> findByBook_IdAndPatron_IdAndStatus(
            @Param("bookId") Long bookId,
            @Param("patronId") Long patronId,
            @Param("status") BorrowingRecord.BorrowingStatus status
    );

    Object findByBookIdAndPatronIdAndStatus(Long bookId, Long patronId, BorrowingRecord.BorrowingStatus borrowingStatus);
}
