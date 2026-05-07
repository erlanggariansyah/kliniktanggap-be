package com.albert.kliniktanggap.repository;

import com.albert.kliniktanggap.entity.User;
import com.albert.kliniktanggap.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    long countByRole(UserRole role);
    List<User> findByActiveTrue();
}
