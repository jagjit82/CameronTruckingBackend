package com.cameron.driver.education.model;

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
@Table(name = "reminders")
@CrossOrigin("*")
public class Reminders {

	private long id;
	
	@Column(name = "status", nullable = true)
	private String status;
	
	@Column(name = "payment", nullable = false,unique=true)
	private long payment;
	
	@Column(name = "plate_No_Exp", nullable = false,unique=true)
	private long plateNoExp;
	
	@Column(name = "cvip_date", nullable = false,unique=true)
	private long cvipDate;
	 
	@Column(name = "vehicle_insurance_expiry", nullable = false)
	private long vehicleInsuranceExpiry;
	
	@Column(name = "instructor_license_expiry", nullable = false)
	private long instructorLicenceExp;
	
	@Column(name = "operator_license_expiry", nullable = false)
	private long operatorLicenceExp;
	
	@Column(name = "license_expiry", nullable = false)
	private long licenseExpiry;
	
	@Column(name = "business_license", nullable = false)
	private long businessLicense;
	
	@Column(name = "school_license", nullable = false)
	private long schoolLicense;
	
	private Company company;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;
	
		
	public Reminders() {
		
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

	public long getPayment() {
		return payment;
	}

	public void setPayment(long payment) {
		this.payment = payment;
	}

	
	public long getLicenseExpiry() {
		return licenseExpiry;
	}

	public void setLicenseExpiry(long licenseExpiry) {
		this.licenseExpiry = licenseExpiry;
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

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_COMPANY_ID", insertable = true, updatable = true ,nullable=false)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public long getPlateNoExp() {
		return plateNoExp;
	}

	public void setPlateNoExp(long plateNoExp) {
		this.plateNoExp = plateNoExp;
	}

	public long getCvipDate() {
		return cvipDate;
	}

	public void setCvipDate(long cvipDate) {
		this.cvipDate = cvipDate;
	}

	public long getVehicleInsuranceExpiry() {
		return vehicleInsuranceExpiry;
	}

	public void setVehicleInsuranceExpiry(long vehicleInsuranceExpiry) {
		this.vehicleInsuranceExpiry = vehicleInsuranceExpiry;
	}

	public long getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(long businessLicense) {
		this.businessLicense = businessLicense;
	}

	public long getSchoolLicense() {
		return schoolLicense;
	}

	public void setSchoolLicense(long schoolLicense) {
		this.schoolLicense = schoolLicense;
	}

	public long getInstructorLicenceExp() {
		return instructorLicenceExp;
	}

	public void setInstructorLicenceExp(long instructorLicenceExp) {
		this.instructorLicenceExp = instructorLicenceExp;
	}

	public long getOperatorLicenceExp() {
		return operatorLicenceExp;
	}

	public void setOperatorLicenceExp(long operatorLicenceExp) {
		this.operatorLicenceExp = operatorLicenceExp;
	}
	
	
}
