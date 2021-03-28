package com.comp3004.educationmanager.db.repositories;

import com.comp3004.educationmanager.observer.CourseData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseData, Long> {
    CourseData findByCourseCode(String courseCode);
}
