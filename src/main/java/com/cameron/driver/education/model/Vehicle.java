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
@Table(name = "vehicle")
@CrossOrigin("*")
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	
	@Column(name="year",nullable=false)
	private Long year;
	
	@Column(name="make",nullable=false)
	private String make;
	
	@Column(name="vehicleNo",nullable=false)
	private String vehicleNo;
	
	@Column(name="vehicle_type",nullable=false)
	private String vehicleType;
	
	@Column(name="loan",nullable=false)
	private Long loan;
	
	@Column(name="plate_no",nullable=false)
	private String plateNo;
	
	@Column(name = "plateNo_exp", nullable = true)
	private LocalDate plateNoExp;
	
	@Column(name = "insurance_date", nullable = false)
	private LocalDate insuranceDate;
	
	@Column(name = "cvip_date", nullable = false)
	private LocalDate cvipDate;
	
	@Column(name = "plateNo_renewal", nullable = false)
	private LocalDate plateNoRenewal;
	
	@Column(name="loan_remarks",nullable=true)
	private String loanRemarks;
	
	@Column(name="remarks",nullable=true)
	private String remarks;
	
	@Column(name="vin_no",nullable=false)
	private String vinNo;
	
	@Column(name="color",nullable=false)
	private String color;
	
	@Column(name="valid_insurance_6d_endorse",nullable=false)
	private String validInsur6dEndorse;
	
	@Column(name="inspection_report_12_months",nullable=false)
	private String inspectionReportIn12Mnths;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;
		
	@Column(name="status")
	private String status;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_COMPANY_ID", insertable = true, updatable = true ,nullable=false)
	private Company company;
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public Vehicle() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}


	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getValidInsur6dEndorse() {
		return validInsur6dEndorse;
	}

	public void setValidInsur6dEndorse(String validInsur6dEndorse) {
		this.validInsur6dEndorse = validInsur6dEndorse;
	}

	public String getInspectionReportIn12Mnths() {
		return inspectionReportIn12Mnths;
	}

	public void setInspectionReportIn12Mnths(String inspectionReportIn12Mnths) {
		this.inspectionReportIn12Mnths = inspectionReportIn12Mnths;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public Long getLoan() {
		return loan;
	}

	public void setLoan(Long loan) {
		this.loan = loan;
	}

	public LocalDate getPlateNoExp() {
		return plateNoExp;
	}

	public void setPlateNoExp(LocalDate plateNoExp) {
		this.plateNoExp = plateNoExp;
	}

	public LocalDate getInsuranceDate() {
		return insuranceDate;
	}

	public void setInsuranceDate(LocalDate insuranceDate) {
		this.insuranceDate = insuranceDate;
	}

	public LocalDate getCvipDate() {
		return cvipDate;
	}

	public void setCvipDate(LocalDate cvipDate) {
		this.cvipDate = cvipDate;
	}

	public LocalDate getPlateNoRenewal() {
		return plateNoRenewal;
	}

	public void setPlateNoRenewal(LocalDate plateNoRenewal) {
		this.plateNoRenewal = plateNoRenewal;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getLoanRemarks() {
		return loanRemarks;
	}
	public void setLoanRemarks(String loanRemarks) {
		this.loanRemarks = loanRemarks;
	}

	
		
}
