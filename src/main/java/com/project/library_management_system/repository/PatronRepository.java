package com.project.library_management_system.repository;

import com.project.library_management_system.Entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {
}
