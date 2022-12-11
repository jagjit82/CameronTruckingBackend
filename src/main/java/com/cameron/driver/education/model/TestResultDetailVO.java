package com.cameron.driver.education.model;

import java.io.Serializable;

public class TestResultDetailVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private TestResultDetail testResultDetail;
	
	private long correctAnswers;

	public TestResultDetail getTestResultDetail() {
		return testResultDetail;
	}

	public void setTestResultDetail(TestResultDetail testResultDetail) {
		this.testResultDetail = testResultDetail;
	}

	public long getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(long correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

		
	
}
