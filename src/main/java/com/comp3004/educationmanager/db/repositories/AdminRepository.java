package com.comp3004.educationmanager.db.repositories;

import com.comp3004.educationmanager.accounts.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
//    List<Admin> findByNameContaining(String username);
}
