package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.StudentLog;

@Repository
public interface StudentLogRepository extends JpaRepository<StudentLog, Long>,JpaSpecificationExecutor<StudentLog>{

	   @Query("SELECT sum(hours) FROM StudentLog log WHERE log.student.id=:id")
	    String calculateHours(@Param("id") Long id);
	

}
