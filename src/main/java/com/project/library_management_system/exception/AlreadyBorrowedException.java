package com.project.library_management_system.exception;

public class AlreadyBorrowedException extends RuntimeException {
    public AlreadyBorrowedException(String message) {
        super(message);
    }
}
