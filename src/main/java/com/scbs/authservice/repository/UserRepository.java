package com.scbs.authservice.repository;

import com.scbs.authservice.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int userId);
    Optional<User> findByEmail(String email);
}
