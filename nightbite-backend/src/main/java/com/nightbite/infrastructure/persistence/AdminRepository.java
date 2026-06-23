package com.nightbite.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightbite.domain.Admin;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);

    Optional<Admin> findByUsername(String username);
}

