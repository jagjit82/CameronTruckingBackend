package com.cameron.driver.education.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.DieselLog;
import com.cameron.driver.education.model.InstructorMonitoring;

@Repository
public interface InstructorMonitoringRepository extends JpaRepository<InstructorMonitoring, Long>,JpaSpecificationExecutor<InstructorMonitoring> {
	

	
	  @Query("SELECT fleet FROM InstructorMonitoring fleet where fleet.status = :status") 
	  List<DieselLog> findInstructorMonitoringByStatus(@Param("status") String status);
	  
  
	  Page<DieselLog> findByStatus(String status,Pageable page);
	  
	  //Page<VehicleServiceLog> findByStatusAndEmployee(String status,Employee_User employee,Pageable page);
	  
	  
	
}