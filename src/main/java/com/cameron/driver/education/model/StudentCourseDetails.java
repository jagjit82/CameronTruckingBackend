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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name = "student_course_details")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property="id")
public class StudentCourseDetails{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	//@JsonBackReference
    @JoinColumn(name = "FK_COURSE_ID", insertable = true, updatable = true ,nullable=false)
	private StudentCourse studentCourse;
	
	@Column(name = "roadtest_date", nullable = false)
	private LocalDate roadTestDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StudentCourse getStudentCourse() {
		return studentCourse;
	}

	public void setStudentCourse(StudentCourse studentCourse) {
		this.studentCourse = studentCourse;
	}

	public LocalDate getRoadTestDate() {
		return roadTestDate;
	}

	public void setRoadTestDate(LocalDate roadTestDate) {
		this.roadTestDate = roadTestDate;
	}
	
	
		
	}
