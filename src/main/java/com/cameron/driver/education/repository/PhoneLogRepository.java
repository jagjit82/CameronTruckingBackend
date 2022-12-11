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
import com.cameron.driver.education.model.PhoneLog;

@Repository
public interface PhoneLogRepository extends JpaRepository<PhoneLog, Long>,JpaSpecificationExecutor<PhoneLog> {
	

	
	  @Query("SELECT fleet FROM PhoneLog fleet where fleet.status = :status") 
	  List<DieselLog> findPhoneLogByStatus(@Param("status") String status);
	  
  
	  Page<DieselLog> findByStatus(String status,Pageable page);
	  
	  //Page<VehicleServiceLog> findByStatusAndEmployee(String status,Employee_User employee,Pageable page);
	  
	  
	
}