package com.nightbite.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightbite.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

