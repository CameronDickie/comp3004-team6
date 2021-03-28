package com.comp3004.educationmanager.db.repositories;

import com.comp3004.educationmanager.accounts.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
A repository for the Student users - used by the database
 */
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUsername(String username);
}
