package com.comp3004.educationmanager.db.repositories;

import com.comp3004.educationmanager.accounts.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
A repository for the Admin users - used by the database
 */
public interface AdminRepository extends JpaRepository<Admin, Long> {
//    List<Admin> findByNameContaining(String username);
}
