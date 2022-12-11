package com.cameron.driver.education.model;

import java.io.Serializable;
import java.time.LocalDate;

public class ExpenseLogSearch implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private LocalDate expenseStartDate;
	
	private LocalDate expenseEndDate;
	
	private Expense expense;

	private String paymentMode;
	
	private String status;
	
	private int pageNumber;
	
	private String accountType;
	
	private String company;

	public LocalDate getExpenseStartDate() {
		return expenseStartDate;
	}

	public void setExpenseStartDate(LocalDate expenseStartDate) {
		this.expenseStartDate = expenseStartDate;
	}

	public LocalDate getExpenseEndDate() {
		return expenseEndDate;
	}

	public void setExpenseEndDate(LocalDate expenseEndDate) {
		this.expenseEndDate = expenseEndDate;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	
}
