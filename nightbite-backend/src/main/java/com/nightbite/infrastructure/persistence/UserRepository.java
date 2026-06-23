package com.nightbite.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nightbite.domain.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);
}

