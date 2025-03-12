package com.project.library_management_system.controller;
import com.project.library_management_system.Entity.BorrowingRecord;
import com.project.library_management_system.service.BorrowingService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId
    ) {
        return new ResponseEntity<>(
                borrowingService.borrowBook(bookId, patronId),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> returnBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId
    ) {
        return ResponseEntity.ok(borrowingService.returnBook(bookId, patronId));
    }
}
