package com.cameron.driver.education.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.VehicleLog;

@Repository
public interface VehicleLogRepository extends JpaRepository<VehicleLog, Long>,JpaSpecificationExecutor<VehicleLog>,VehicleLogCustomRepository {
	

	
	  @Query("SELECT fleet FROM VehicleLog fleet where fleet.status = :status") 
	  List<VehicleLog> findVehicleLogsByStatus(@Param("status") String status);
	  
  
	  Page<VehicleLog> findByStatus(String status,Pageable page);
	  
	  //Page<VehicleServiceLog> findByStatusAndEmployee(String status,Employee_User employee,Pageable page);
	  
	  
	
}