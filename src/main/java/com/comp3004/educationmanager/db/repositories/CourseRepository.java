package com.comp3004.educationmanager.db.repositories;

import com.comp3004.educationmanager.observer.CourseData;
import org.springframework.data.jpa.repository.JpaRepository;

/*
A repository for the courses - used by the database
 */
public interface CourseRepository extends JpaRepository<CourseData, Long> {
    CourseData findByCourseCode(String courseCode);
}
