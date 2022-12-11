package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.InstructorLog;

@Repository
public interface InstructorLogRepository extends JpaRepository<InstructorLog, Long>,JpaSpecificationExecutor<InstructorLog> {
	
	
}