package dev.skonan.easybank.repositories;

import dev.skonan.easybank.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
