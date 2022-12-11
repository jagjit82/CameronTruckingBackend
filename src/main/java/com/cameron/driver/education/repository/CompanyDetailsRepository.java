package com.cameron.driver.education.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.CompanyDetails;

@Repository
public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails, Long>,JpaSpecificationExecutor<CompanyDetails>{

	
	@Query("select comDetails from CompanyDetails comDetails where ((comDetails.businessLicense>:today and"
			+ " comDetails.businessLicense<:businessLicenseExpiry) or (comDetails.schoolLicense>:today and "
			+ "comDetails.schoolLicense<:schoolLicenseExpiry)) and comDetails.company.id=:company")
	List<CompanyDetails> getCompanyDetailsReminder(@Param("company") long company,
			@Param("today") LocalDate today,
			@Param("businessLicenseExpiry") LocalDate businessLicenseExpiry,@Param("schoolLicenseExpiry") LocalDate schoolLicenseExpiry );
	
	@Query("select count(comDetails) from CompanyDetails comDetails where ((comDetails.businessLicense>:today and"
			+ " comDetails.businessLicense<:businessLicenseExpiry) or (comDetails.schoolLicense>:today and "
			+ "comDetails.schoolLicense<:schoolLicenseExpiry)) and comDetails.company.id=:company")
	Integer countCompanyDetailsReminder(@Param("company") long company,
			@Param("today") LocalDate today,
			@Param("businessLicenseExpiry") LocalDate businessLicenseExpiry,@Param("schoolLicenseExpiry") LocalDate schoolLicenseExpiry );
	

}
