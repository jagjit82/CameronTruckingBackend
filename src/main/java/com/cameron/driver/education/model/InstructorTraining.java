package com.cameron.driver.education.model;

import java.io.Serializable;
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
@Table(name = "instructor_training")
@CrossOrigin("*")
public class InstructorTraining implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_INSTRUCTOR_ID", insertable = true, updatable = true ,nullable=false)
	private Instructor instructorTrainee;
	
	@Column(name="training_date",nullable=false)
	private LocalDate trainingDate;
	
	@Column(name="hours")
	private Long hours;
	
	@Column(name="training_name")
	private String trainingName;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_instructor_monitor", insertable = true, updatable = true ,nullable=false)
	private Instructor instructorMonitor;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="status")
	private String status;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;
	
	
	public InstructorTraining() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Instructor getInstructorTrainee() {
		return instructorTrainee;
	}


	public void setInstructorTrainee(Instructor instructorTrainee) {
		this.instructorTrainee = instructorTrainee;
	}


	public LocalDate getTrainingDate() {
		return trainingDate;
	}


	public void setTrainingDate(LocalDate trainingDate) {
		this.trainingDate = trainingDate;
	}


	public Long getHours() {
		return hours;
	}


	public void setHours(Long hours) {
		this.hours = hours;
	}


	public String getTrainingName() {
		return trainingName;
	}


	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}


	public Instructor getInstructorMonitor() {
		return instructorMonitor;
	}


	public void setInstructorMonitor(Instructor instructorMonitor) {
		this.instructorMonitor = instructorMonitor;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
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


	
			
}
