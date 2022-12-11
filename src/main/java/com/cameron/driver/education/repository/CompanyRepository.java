package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{

	
	@Query("select comp from Company comp where comp.company = :company")
	Company findCompanyByName(@Param("company") String company);
	
	

}
