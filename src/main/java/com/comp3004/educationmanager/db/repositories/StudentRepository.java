package com.comp3004.educationmanager.db.repositories;

import com.comp3004.educationmanager.accounts.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUsername(String username);
}
