package com.project.library_management_system.controller;
import com.project.library_management_system.Entity.Patron;
import com.project.library_management_system.service.PatronService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patrons")
@RequiredArgsConstructor
public class PatronController {
    private final PatronService patronService;

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        return ResponseEntity.ok(patronService.getAllPatrons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        return ResponseEntity.ok(patronService.getPatronById(id));
    }

    @PostMapping
    public ResponseEntity<Patron> addPatron(@Valid @RequestBody Patron patron) {
        return new ResponseEntity<>(patronService.addPatron(patron), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron patron) {
        return ResponseEntity.ok(patronService.updatePatron(id, patron));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }
}
