package com.cameron.driver.education.model;

import java.io.Serializable;
import java.time.LocalDate;

public class PhoneLogSearch implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private LocalDate callStartDate;
	
	private LocalDate callEndDate;
	
	private String callerType;
	
	private String name;
	
	private String phoneNo;
	
	private String status;
	
	private String company;

	private int pageNumber;
	
	public LocalDate getCallStartDate() {
		return callStartDate;
	}

	public void setCallStartDate(LocalDate callStartDate) {
		this.callStartDate = callStartDate;
	}

	public LocalDate getCallEndDate() {
		return callEndDate;
	}

	public void setCallEndDate(LocalDate callEndDate) {
		this.callEndDate = callEndDate;
	}

	public String getCallerType() {
		return callerType;
	}

	public void setCallerType(String callerType) {
		this.callerType = callerType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	}
