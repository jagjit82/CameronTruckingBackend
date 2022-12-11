package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.Employee_User;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee_User, Long>,JpaSpecificationExecutor<Employee_User>{

	
	@Query("select emp from Employee_User emp where emp.userName = :userName")
	Employee_User findEmployeeByName(@Param("userName") String userName);
	
	

}
