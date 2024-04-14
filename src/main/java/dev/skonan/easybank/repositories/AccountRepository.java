package dev.skonan.easybank.repositories;

import dev.skonan.easybank.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
