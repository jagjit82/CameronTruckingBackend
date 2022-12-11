package com.cameron.driver.education.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "student")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property="id")
public class Student {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name="studentNum")
	private String studentNum;
	
	@Column(name="firstName")
	private String firstName;
	
	@Column(name="middleName")
	private String middleName;
	
	@Column(name="lastName")
	private String lastName;
	
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;
	
	@Column(name = "license_expiry")
	private LocalDate licenseExpiry;
	
	@Column(name="address")
	private String address;
	
	@Column(name="city")
	private String city;
	
	@Column(name="postal_code")
	private String postalCode;
	
	@Column(name="MVID")
	private String mVID;
	
	@Column(name="class")
	private String studentClass;
	
	@Column(name="from_class")
	private long fromClass;
	
	@Column(name="to_class")
	private long toClass;
	
	@Column(name="license")
	private String license;
	
	@Column(name="phoneNo")
	private String phoneNo;
	
	@Column(name="emergency_contact")
	private String emergencyContact;

	@Column(name="email",unique=true,nullable=false)
	private String email;
	
	@Column(name="course")
	private String course;
	
	@Column(name="reference")
	private String reference;
	
	@Column(name="endorsementQ")
	private String endorsementQ;
	
	@Column(name="payment_mode")
	private String paymentMode;
	
	@Column(name="password")
	private String passowrd;
	
	@Column(name="invoice")
	private String invoice;
	
	@Column(name="preffered_hours")
	private long prefferedHours;
	
	@Column(name="discount")
	private double discount;
	
	@Column(name = "issue_date", nullable = false)
	private LocalDate issueDate;
	
	@Column(name = "registration_date", nullable = false)
	private LocalDate registrationDate;
	
	@Column(name="total_amount")
	private long totalAmount;
	
	@Column(name="payments_received")
	private long paymentsReceived;
	
	@Column(name="funding_agency")
	private String fundingAgency;
	
	@Column(name="status")
	private String status;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;
	
	@Column(name = "start_time", nullable = true)
	private LocalTime startTime;
	
	@Column(name = "end_time", nullable = true)
	private LocalTime endTime;
	
	
	@Column(name="day1")
	private String day1;
	
	@Column(name="day2")
	private String day2;
	
	@Column(name="day3")
	private String day3;
	
	@Column(name="day4")
	private String day4;
	
	@Column(name="day5")
	private String day5;
	
	@Column(name="day6")
	private String day6;
	
	@Column(name="day7")
	private String day7;
	
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_COMPANY_ID", insertable = true, updatable = true ,nullable=false)
	private Company company;
	
	//@JsonManagedReference
	@OneToOne(mappedBy="student", cascade=CascadeType.REMOVE, fetch = FetchType.EAGER)
	private StudentResult studentResult;
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getStudentNum() {
		return studentNum;
	}
	public void setStudentNum(String studentNum) {
		this.studentNum = studentNum;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getmVID() {
		return mVID;
	}
	public void setmVID(String mVID) {
		this.mVID = mVID;
	}
	public String getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
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
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public LocalDate getLicenseExpiry() {
		return licenseExpiry;
	}
	public void setLicenseExpiry(LocalDate licenseExpiry) {
		this.licenseExpiry = licenseExpiry;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public long getFromClass() {
		return fromClass;
	}
	public void setFromClass(long fromClass) {
		this.fromClass = fromClass;
	}
	public long getToClass() {
		return toClass;
	}
	public void setToClass(long toClass) {
		this.toClass = toClass;
	}
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public long getPrefferedHours() {
		return prefferedHours;
	}
	public void setPrefferedHours(long prefferedHours) {
		this.prefferedHours = prefferedHours;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public LocalDate getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}
	public long getPaymentsReceived() {
		return paymentsReceived;
	}
	public void setPaymentsReceived(long paymentsReceived) {
		this.paymentsReceived = paymentsReceived;
	}
	public String getFundingAgency() {
		return fundingAgency;
	}
	public void setFundingAgency(String fundingAgency) {
		this.fundingAgency = fundingAgency;
	}
	public long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public StudentResult getStudentResult() {
		return studentResult;
	}
	public void setStudentResult(StudentResult studentResult) {
		this.studentResult = studentResult;
	}
	public String getDay1() {
		return day1;
	}
	public void setDay1(String day1) {
		this.day1 = day1;
	}
	public String getDay2() {
		return day2;
	}
	public void setDay2(String day2) {
		this.day2 = day2;
	}
	public String getDay3() {
		return day3;
	}
	public void setDay3(String day3) {
		this.day3 = day3;
	}
	public String getDay4() {
		return day4;
	}
	public void setDay4(String day4) {
		this.day4 = day4;
	}
	public String getDay5() {
		return day5;
	}
	public void setDay5(String day5) {
		this.day5 = day5;
	}
	public String getDay6() {
		return day6;
	}
	public void setDay6(String day6) {
		this.day6 = day6;
	}
	public String getDay7() {
		return day7;
	}
	public String getPassowrd() {
		return passowrd;
	}
	public void setPassowrd(String passowrd) {
		this.passowrd = passowrd;
	}
	public void setDay7(String day7) {
		this.day7 = day7;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public String getEndorsementQ() {
		return endorsementQ;
	}
	public void setEndorsementQ(String endorsementQ) {
		this.endorsementQ = endorsementQ;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public LocalDate getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	
	}
