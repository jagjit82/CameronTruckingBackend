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

@Repository
public interface DieselLogRepository extends JpaRepository<DieselLog, Long>,JpaSpecificationExecutor<DieselLog> {
	

	
	  @Query("SELECT fleet FROM DieselLog fleet where fleet.status = :status") 
	  List<DieselLog> findDieselLogsByStatus(@Param("status") String status);
	  
  
	  Page<DieselLog> findByStatus(String status,Pageable page);
	  
	  //Page<VehicleServiceLog> findByStatusAndEmployee(String status,Employee_User employee,Pageable page);
	  
	  
	
}