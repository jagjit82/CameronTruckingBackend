package com.cameron.driver.education.model;

public class StudentVO {

	private StudentPayment studPayment;
	
	private long amountReceived;
	
	public StudentVO(){
	}

	public StudentVO(StudentPayment studPay,long amountReceived){
		this.studPayment=studPay;
		this.amountReceived=amountReceived;
	}
	
	public StudentPayment getStudPayment() {
		return studPayment;
	}

	public void setStudPayment(StudentPayment studPayment) {
		this.studPayment = studPayment;
	}

	public long getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(long amountReceived) {
		this.amountReceived = amountReceived;
	}

		
	}
