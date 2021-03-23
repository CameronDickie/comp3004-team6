package com.comp3004.educationmanager.db.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDetails, Long> {
    List<UserDetails> findByNameContaining(String name);
}