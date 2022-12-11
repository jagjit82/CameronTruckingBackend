package com.cameron.driver.education.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.web.bind.annotation.CrossOrigin;

@Entity
@Table(name = "company_details")
@CrossOrigin("*")
public class CompanyDetails {

	private long id;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name="business_license")
	private LocalDate businessLicense;
	
	@Column(name="school_license")
	private LocalDate schoolLicense;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;
	
	private Company company;
	
	
	public CompanyDetails() {
		
	} 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public LocalDate getBusinessLicense() {
		return businessLicense;
	}
	public void setBusinessLicense(LocalDate businessLicense) {
		this.businessLicense = businessLicense;
	}
	public LocalDate getSchoolLicense() {
		return schoolLicense;
	}
	public void setSchoolLicense(LocalDate schoolLicense) {
		this.schoolLicense = schoolLicense;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_COMPANY_ID", insertable = true, updatable = true ,nullable=false)
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	} 
	
	
}
