	package com.cameron.driver.education.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long>,JpaSpecificationExecutor<Instructor>,InstructorCustomRepository{

	
	@Query("SELECT instructor FROM Instructor instructor where instructor.id =1") 
	 List<Instructor> findUnmappedDrivers();
	
	@Query("select instructor from Instructor instructor where ((instructor.instructorLicenceExp>:today and"
			+ " instructor.instructorLicenceExp<:instructorLicenceExp) or (instructor.operatorLicenceExp>:today and "
			+ "instructor.operatorLicenceExp<:operatorLicenceExp)) and instructor.company.id=:company")
	List<Instructor> getInstructorExpiryNotifications(@Param("company") long company,
			@Param("today") LocalDate today,
			@Param("instructorLicenceExp") LocalDate instructorLicenceExp,@Param("operatorLicenceExp") LocalDate operatorLicenceExp );
	
	@Query("select count(instructor) from Instructor instructor where ((instructor.instructorLicenceExp>:today and"
			+ " instructor.instructorLicenceExp<:instructorLicenceExp) or (instructor.operatorLicenceExp>:today and "
			+ "instructor.operatorLicenceExp<:operatorLicenceExp)) and instructor.company.id=:company")
	Integer countInstructorExpiryNotifications(@Param("company") long company,
			@Param("today") LocalDate today,
			@Param("instructorLicenceExp") LocalDate instructorLicenceExp,@Param("operatorLicenceExp") LocalDate operatorLicenceExp );
	
	 
}
