package com.project.library_management_system.service;


import com.project.library_management_system.Entity.Patron;
import com.project.library_management_system.exception.ResourceNotFoundException;
import com.project.library_management_system.repository.PatronRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatronService {
    private final PatronRepository patronRepository;

    @Cacheable("patrons")
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Cacheable(value = "patrons", key = "#id")
    public Patron getPatronById(Long id) {
        return patronRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found"));
    }

    @Transactional
    @CacheEvict(value = "patrons", allEntries = true)
    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Transactional
    @CachePut(value = "patrons", key = "#id")
    public Patron updatePatron(Long id, Patron patron) {
        Patron existingPatron = getPatronById(id);
        existingPatron.setName(patron.getName());
        existingPatron.setContactInfo(patron.getContactInfo());
        existingPatron.setEmail(patron.getEmail());
        existingPatron.setPhoneNumber(patron.getPhoneNumber());
        return patronRepository.save(existingPatron);
    }

    @Transactional
    @CacheEvict(value = "patrons", key = "#id")
    public void deletePatron(Long id) {
        patronRepository.delete(getPatronById(id));
    }
}
