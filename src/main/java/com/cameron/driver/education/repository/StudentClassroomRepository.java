package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.StudentClassroom;

@Repository
public interface StudentClassroomRepository extends JpaRepository<StudentClassroom, Long>{

	
	@Query("select studentClass from StudentClassroom studentClass where studentClass.student.id = :id")
	Company findStudentClassroomById(@Param("id") String id);
	
	

}
