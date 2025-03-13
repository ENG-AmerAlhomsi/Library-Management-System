package com.project.library_management_system.service;


import com.project.library_management_system.Entity.Book;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Cacheable("books") // Cache results of this method
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Cacheable(value = "books", key = "#id") // Cache by book ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    @Transactional
    @CacheEvict(value = "books", allEntries = true) // Clear cache when a new book is added
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    @CachePut(value = "books", key = "#id") // Update cache after modification
    public Book updateBook(Long id, Book book) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublicationYear(book.getPublicationYear());
        existingBook.setIsbn(book.getIsbn());
        return bookRepository.save(existingBook);
    }

    @Transactional
    @CacheEvict(value = "books", key = "#id") // Remove entry from cache on deletion
    public void deleteBook(Long id) {
        bookRepository.delete(getBookById(id));
    }
}
