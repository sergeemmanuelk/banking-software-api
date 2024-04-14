package dev.skonan.easybank.repositories;

import dev.skonan.easybank.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
