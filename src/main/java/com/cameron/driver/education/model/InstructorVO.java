package com.cameron.driver.education.model;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public class InstructorVO {

	
	private int pageNum;
	
	private int pageSize;
	
	private String searchInstructor;
	
	private String company;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSearchInstructor() {
		return searchInstructor;
	}

	public void setSearchInstructor(String searchInstructor) {
		this.searchInstructor = searchInstructor;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	
	
	
}
