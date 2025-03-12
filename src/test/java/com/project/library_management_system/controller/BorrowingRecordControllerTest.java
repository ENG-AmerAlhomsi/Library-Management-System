package com.project.library_management_system.controller;

import com.project.library_management_system.Entity.BorrowingRecord;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.service.BorrowingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowingController.class)
public class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingService borrowingRecordService;

    @Test
    public void testBorrowBookSuccess() throws Exception {
        Mockito.when(borrowingRecordService.borrowBook(anyLong(), anyLong()))
                .thenReturn(new BorrowingRecord());

        mockMvc.perform(post("/api/borrow/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testBorrowBookNotFound() throws Exception {
        Mockito.when(borrowingRecordService.borrowBook(anyLong(), anyLong()))
                .thenThrow(new ResourceNotFoundException("Book not found"));

        mockMvc.perform(post("/api/borrow/1/patron/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testReturnBookSuccess() throws Exception {
        Mockito.when(borrowingRecordService.returnBook(anyLong(), anyLong()))
                .thenReturn(new BorrowingRecord());

        mockMvc.perform(put("/api/return/1/patron/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testReturnBookNotFound() throws Exception {
        Mockito.when(borrowingRecordService.returnBook(anyLong(), anyLong()))
                .thenThrow(new ResourceNotFoundException("Record not found"));

        mockMvc.perform(put("/api/return/1/patron/1"))
                .andExpect(status().isNotFound());
    }
}
