package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.Reminders;

@Repository
public interface RemindersRepository extends JpaRepository<Reminders, Long>,JpaSpecificationExecutor<Reminders>{

	
	/*
	 * @Query("select rem from Reminders rem where rem.company = :company") Company
	 * findRemindersByName(@Param("company") String company);
	 */
	
	

}
