package com.project.library_management_system.controller;
import com.project.library_management_system.Entity.Book;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;

import java.time.Year;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllBooks() throws Exception {
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor("Test Author");
        book.setIsbn("123-4567890123");
        book.setPublicationYear(Year.of(2000));
        Mockito.when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Sample Book")));
    }

    @Test
    public void testGetBookByIdFound() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Found Book");
        book.setAuthor("Test Author");
        book.setIsbn("123-4567890123");
        book.setPublicationYear(Year.of(2000));
        Mockito.when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Found Book")));
    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        Mockito.when(bookService.getBookById(1L))
                .thenThrow(new ResourceNotFoundException("Book not found with id: 1"));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Book not found with id: 1"));
    }

    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("Author");
        book.setIsbn("1234567890");
        book.setPublicationYear(Year.of(2000));

        // Fix the matcher usage
        Mockito.when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    @Test
    public void testCreateBookInvalidInput() throws Exception {
        Book invalidBook = new Book(); // Missing required fields
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setIsbn("123-4567890123"); // Valid ISBN format
        updatedBook.setPublicationYear(Year.of(2023));

        Mockito.when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(bookService, times(1)).deleteBook(1L);
    }
}
