package com.comp3004.educationmanager.db.repositories;

import com.comp3004.educationmanager.accounts.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
A repository for the Professor users - used by the database
 */
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Professor findByUsername(String username);
}
