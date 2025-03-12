package com.project.library_management_system.service;
import com.project.library_management_system.Entity.Book;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(new Book()));
        List<Book> books = bookService.getAllBooks();
        assertThat(books).hasSize(1);
    }

    @Test
    public void testGetBookByIdFound() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book found = bookService.getBookById(1L);
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book not found");
    }

    @Test
    public void testCreateBook() {
        Book book = new Book();
        book.setTitle("New Book");
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book created = bookService.addBook(book);
        assertThat(created.getTitle()).isEqualTo("New Book");
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");

        Book updatedBook = new Book();
        updatedBook.setTitle("New Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook);
        assertThat(result.getTitle()).isEqualTo("New Title");
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 1L;
        Book mockBook = new Book();
        mockBook.setId(bookId);

        // Mock the existence check
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        // Mock the deletion with entity parameter
        doNothing().when(bookRepository).delete(mockBook);

        bookService.deleteBook(bookId);

        // Verify the interactions
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(mockBook);
    }
}
