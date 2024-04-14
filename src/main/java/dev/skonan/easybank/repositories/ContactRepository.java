package dev.skonan.easybank.repositories;

import dev.skonan.easybank.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
