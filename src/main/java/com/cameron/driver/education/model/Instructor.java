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
@Table(name = "instructor")
@CrossOrigin("*")
public class Instructor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String firstName;
	private String lastName;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "instructor_license_number", nullable = false)
	private String instructorLicenseNum;
	
	@Column(name = "instruction_type", nullable = false)
	private String instructionType;
	
	@Column(name = "instructor_license_exp", nullable = false)
	private LocalDate instructorLicenceExp;
	
	@Column(name = "date_of_joining", nullable = true)
	private LocalDate dateOfJoining;
	
	@Column(name = "operator_license_number", nullable = false)
	private String operatorLicenseNum;
	
	@Column(name = "operator_license_exp", nullable = false)
	private LocalDate operatorLicenceExp;
	
	@Column(name = "operator_lic_class", nullable = false)
	private String operatorLicenceClass;
	
	@Column(name = "address", nullable = true)
	private String address;
	
	@Column(name = "phone", nullable = true)
	private String phone;
	
	@Column(name = "email", nullable = true)
	private String email;
	
	@Column(name = "emergency_phone", nullable = true)
	private String emergencyContact;
	
	@Column(name = "medical_conditions", nullable = true)
	private String medicalConditions;
	
	@Column(name = "class_of_instructions", nullable = true)
	private String classOfInstructions;
	
	@Column(name = "class_of_driver_license", nullable = true)
	private String classOfDriverLicense;
	
	@Column(name = "instructorMonitoredDate", nullable = false)
	private LocalDate instructorMonitoredDate;
	
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_COMPANY_ID", insertable = true, updatable = true ,nullable=false)
	private Company company;
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public Instructor() {
		
	} 
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "first_name", nullable = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name", nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public String getInstructorLicenseNum() {
		return instructorLicenseNum;
	}

	public void setInstructorLicenseNum(String instructorLicenseNum) {
		this.instructorLicenseNum = instructorLicenseNum;
	}

	public String getInstructionType() {
		return instructionType;
	}

	public void setInstructionType(String instructionType) {
		this.instructionType = instructionType;
	}

	public LocalDate getInstructorLicenceExp() {
		return instructorLicenceExp;
	}

	public void setInstructorLicenceExp(LocalDate instructorLicenceExp) {
		this.instructorLicenceExp = instructorLicenceExp;
	}

	public String getOperatorLicenseNum() {
		return operatorLicenseNum;
	}

	public void setOperatorLicenseNum(String operatorLicenseNum) {
		this.operatorLicenseNum = operatorLicenseNum;
	}

	public LocalDate getOperatorLicenceExp() {
		return operatorLicenceExp;
	}

	public void setOperatorLicenceExp(LocalDate operatorLicenceExp) {
		this.operatorLicenceExp = operatorLicenceExp;
	}

	public String getOperatorLicenceClass() {
		return operatorLicenceClass;
	}

	public void setOperatorLicenceClass(String operatorLicenceClass) {
		this.operatorLicenceClass = operatorLicenceClass;
	}

	public LocalDate getInstructorMonitoredDate() {
		return instructorMonitoredDate;
	}

	public void setInstructorMonitoredDate(LocalDate instructorMonitoredDate) {
		this.instructorMonitoredDate = instructorMonitoredDate;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	public String getMedicalConditions() {
		return medicalConditions;
	}
	public void setMedicalConditions(String medicalConditions) {
		this.medicalConditions = medicalConditions;
	}
	public String getClassOfInstructions() {
		return classOfInstructions;
	}
	public void setClassOfInstructions(String classOfInstructions) {
		this.classOfInstructions = classOfInstructions;
	}
	public String getClassOfDriverLicense() {
		return classOfDriverLicense;
	}
	public void setClassOfDriverLicense(String classOfDriverLicense) {
		this.classOfDriverLicense = classOfDriverLicense;
	}
	public LocalDate getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(LocalDate dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	
}
