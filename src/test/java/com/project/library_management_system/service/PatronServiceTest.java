package com.project.library_management_system.service;

import com.project.library_management_system.Entity.Patron;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.repository.PatronRepository;
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
public class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    @Test
    public void testGetAllPatrons() {
        when(patronRepository.findAll()).thenReturn(Collections.singletonList(new Patron()));
        List<Patron> patrons = patronService.getAllPatrons();
        assertThat(patrons).hasSize(1);
    }

    @Test
    public void testGetPatronByIdFound() {
        Patron patron = new Patron();
        patron.setId(1L);
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        Patron found = patronService.getPatronById(1L);
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    public void testGetPatronByIdNotFound() {
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patronService.getPatronById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patron not found");
    }

    @Test
    public void testCreatePatron() {
        Patron patron = new Patron();
        patron.setName("New Patron");
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        Patron created = patronService.addPatron(patron);
        assertThat(created.getName()).isEqualTo("New Patron");
    }

    @Test
    public void testUpdatePatron() {
        Patron existingPatron = new Patron();
        existingPatron.setId(1L);
        existingPatron.setName("Old Name");

        Patron updatedPatron = new Patron();
        updatedPatron.setName("New Name");

        when(patronRepository.findById(1L)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(any(Patron.class))).thenReturn(updatedPatron);

        Patron result = patronService.updatePatron(1L, updatedPatron);
        assertThat(result.getName()).isEqualTo("New Name");
    }

    @Test
    public void testDeletePatron() {
        Long patronId = 1L;
        Patron mockPatron = new Patron();
        mockPatron.setId(patronId);

        when(patronRepository.findById(patronId)).thenReturn(Optional.of(mockPatron));
        doNothing().when(patronRepository).delete(mockPatron);

        patronService.deletePatron(patronId);

        verify(patronRepository, times(1)).findById(patronId);
        verify(patronRepository, times(1)).delete(mockPatron);
    }
}
