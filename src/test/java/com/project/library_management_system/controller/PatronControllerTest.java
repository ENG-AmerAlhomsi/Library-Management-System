package com.project.library_management_system.controller;

import com.project.library_management_system.Entity.Patron;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.service.PatronService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatronController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllPatrons() throws Exception {
        Patron patron = new Patron();
        patron.setName("John Doe");
        patron.setEmail("john@example.com");
        Mockito.when(patronService.getAllPatrons()).thenReturn(Collections.singletonList(patron));

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    public void testGetPatronByIdFound() throws Exception {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setName("Jane Smith");
        Mockito.when(patronService.getPatronById(1L)).thenReturn(patron);

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jane Smith")));
    }

    @Test
    public void testGetPatronByIdNotFound() throws Exception {
        Mockito.when(patronService.getPatronById(1L))
                .thenThrow(new ResourceNotFoundException("Patron not found with id: 1"));

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Patron not found with id: 1"));
    }

    @Test
    public void testCreatePatron() throws Exception {
        Patron patron = new Patron();
        patron.setName("New Patron");
        patron.setEmail("new@example.com");
        patron.setPhoneNumber("123-456-7890");
        patron.setContactInfo("https://github.com/ENG-AmerAlhomsi");


        Mockito.when(patronService.addPatron(any(Patron.class))).thenReturn(patron);

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Patron"));
    }

    @Test
    public void testCreatePatronInvalidInput() throws Exception {
        Patron invalidPatron = new Patron(); // Missing required fields
        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatron)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatePatron() throws Exception {
        Patron updatedPatron = new Patron();
        updatedPatron.setName("Updated Name");
        updatedPatron.setEmail("updated@example.com");
        updatedPatron.setPhoneNumber("987-654-3210");
        updatedPatron.setContactInfo("https://github.com/ENG-AmerAlhomsi");

        Mockito.when(patronService.updatePatron(eq(1L), any(Patron.class))).thenReturn(updatedPatron);

        mockMvc.perform(put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatron)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void testDeletePatron() throws Exception {
        mockMvc.perform(delete("/api/patrons/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(patronService, times(1)).deletePatron(1L);
    }
}
