package com.project.library_management_system.repository;

import com.project.library_management_system.Entity.Patron;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PatronRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PatronRepository patronRepository;

    @Test
    public void testSavePatron() {
        Patron patron = new Patron();
        patron.setName("John Doe");
        patron.setEmail("john@example.com");
        patron.setPhoneNumber("123-456-7890");
        patron.setContactInfo("https://github.com/ENG-AmerAlhomsi");

        Patron savedPatron = patronRepository.save(patron);
        assertThat(savedPatron).isNotNull();
        assertThat(savedPatron.getId()).isNotNull();
        assertThat(savedPatron.getName()).isEqualTo("John Doe");
        assertThat(savedPatron.getEmail()).isEqualTo("john@example.com");
        assertThat(savedPatron.getPhoneNumber()).isEqualTo("123-456-7890");
        assertThat(savedPatron.getContactInfo()).isEqualTo("https://github.com/ENG-AmerAlhomsi");
    }

    @Test
    public void testFindPatronById() {
        Patron patron = new Patron();
        patron.setName("Jane Smith");
        patron.setEmail("john@example.com");
        patron.setPhoneNumber("123-456-7890");
        patron.setContactInfo("https://github.com/ENG-AmerAlhomsi");
        entityManager.persist(patron);

        Optional<Patron> foundPatron = patronRepository.findById(patron.getId());
        assertThat(foundPatron).isPresent();
        assertThat(foundPatron.get().getName()).isEqualTo("Jane Smith");
    }

    @Test
    public void testDeletePatron() {
        Patron patron = new Patron();
        patron.setName("To Delete");
        patron.setEmail("john@example.com");
        patron.setPhoneNumber("123-456-7890");
        patron.setContactInfo("https://github.com/ENG-AmerAlhomsi");
        entityManager.persist(patron);

        patronRepository.deleteById(patron.getId());
        assertThat(patronRepository.findById(patron.getId())).isEmpty();
    }
}
