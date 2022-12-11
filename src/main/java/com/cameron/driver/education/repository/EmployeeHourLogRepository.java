package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.EmployeeHourLog;

@Repository
public interface EmployeeHourLogRepository extends JpaRepository<EmployeeHourLog, Long>,JpaSpecificationExecutor<EmployeeHourLog>{

	
	
	

}
