package com.comp3004.educationmanager.db.repositories;

import com.comp3004.educationmanager.observer.CourseData;
import com.comp3004.educationmanager.observer.CourseDataSerialized;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseSerializedRepository extends JpaRepository<CourseDataSerialized, Long> {
    CourseDataSerialized findById(long id);

}
