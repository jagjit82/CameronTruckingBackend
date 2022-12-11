package com.cameron.driver.education.model;

import java.time.LocalDateTime;

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

import org.springframework.web.bind.annotation.CrossOrigin;

@Entity
@Table(name = "student_classroom")
@CrossOrigin("*")
public class StudentClassroom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "airbrakes", nullable = false)
	private boolean airBrakes;
	
	@Column(name = "module1", nullable = false)
	private boolean module1;
	
	@Column(name="module1_date")
	private LocalDateTime module1Date;
	
	@Column(name = "module2", nullable = false)
	private boolean module2;
	
	@Column(name="module2_date")
	private LocalDateTime module2Date;
	
	@Column(name = "module3", nullable = false)
	private boolean module3;
	
	@Column(name="module3_date")
	private LocalDateTime module3Date;
	
	@Column(name = "module4", nullable = false)
	private boolean module4;
	
	@Column(name="module4_date")
	private LocalDateTime module4Date;
	
	@Column(name = "module5", nullable = false)
	private boolean module5;
	
	@Column(name="module5_date")
	private LocalDateTime module5Date;
	
	@Column(name = "module6", nullable = false)
	private boolean module6;
	
	@Column(name="module6_date")
	private LocalDateTime module6Date;
	
	@Column(name = "module7", nullable = false)
	private boolean module7;
	
	@Column(name="module7_date")
	private LocalDateTime module7Date;
	
	@Column(name = "module8", nullable = false)
	private boolean module8;
	
	@Column(name="module8_date")
	private LocalDateTime module8Date;
	
	@Column(name = "module9", nullable = false)
	private boolean module9;
	
	@Column(name="module9_date")
	private LocalDateTime module9Date;
	
	@Column(name = "module10", nullable = false)
	private boolean module10;
	
	@Column(name="module10_date")
	private LocalDateTime module10Date;
	
	@Column(name="created_date")
	private LocalDateTime createdDate;
	
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;

	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_STUDENT_ID", insertable = true, updatable = true ,nullable=false)
	private Student student;

	public StudentClassroom() {
		
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

	public boolean isAirBrakes() {
		return airBrakes;
	}

	public void setAirBrakes(boolean airBrakes) {
		this.airBrakes = airBrakes;
	}

	public boolean isModule1() {
		return module1;
	}

	public void setModule1(boolean module1) {
		this.module1 = module1;
	}

	public LocalDateTime getModule1Date() {
		return module1Date;
	}

	public void setModule1Date(LocalDateTime module1Date) {
		this.module1Date = module1Date;
	}

	public boolean isModule2() {
		return module2;
	}

	public void setModule2(boolean module2) {
		this.module2 = module2;
	}

	public LocalDateTime getModule2Date() {
		return module2Date;
	}

	public void setModule2Date(LocalDateTime module2Date) {
		this.module2Date = module2Date;
	}

	public boolean isModule3() {
		return module3;
	}

	public void setModule3(boolean module3) {
		this.module3 = module3;
	}

	public LocalDateTime getModule3Date() {
		return module3Date;
	}

	public void setModule3Date(LocalDateTime module3Date) {
		this.module3Date = module3Date;
	}

	public boolean isModule4() {
		return module4;
	}

	public void setModule4(boolean module4) {
		this.module4 = module4;
	}

	public LocalDateTime getModule4Date() {
		return module4Date;
	}

	public void setModule4Date(LocalDateTime module4Date) {
		this.module4Date = module4Date;
	}

	public boolean isModule5() {
		return module5;
	}

	public void setModule5(boolean module5) {
		this.module5 = module5;
	}

	public LocalDateTime getModule5Date() {
		return module5Date;
	}

	public void setModule5Date(LocalDateTime module5Date) {
		this.module5Date = module5Date;
	}

	public boolean isModule6() {
		return module6;
	}

	public void setModule6(boolean module6) {
		this.module6 = module6;
	}

	public LocalDateTime getModule6Date() {
		return module6Date;
	}

	public void setModule6Date(LocalDateTime module6Date) {
		this.module6Date = module6Date;
	}

	public boolean isModule7() {
		return module7;
	}

	public void setModule7(boolean module7) {
		this.module7 = module7;
	}

	public LocalDateTime getModule7Date() {
		return module7Date;
	}

	public void setModule7Date(LocalDateTime module7Date) {
		this.module7Date = module7Date;
	}

	public boolean isModule8() {
		return module8;
	}

	public void setModule8(boolean module8) {
		this.module8 = module8;
	}

	public LocalDateTime getModule8Date() {
		return module8Date;
	}

	public void setModule8Date(LocalDateTime module8Date) {
		this.module8Date = module8Date;
	}

	public boolean isModule9() {
		return module9;
	}

	public void setModule9(boolean module9) {
		this.module9 = module9;
	}

	public LocalDateTime getModule9Date() {
		return module9Date;
	}

	public void setModule9Date(LocalDateTime module9Date) {
		this.module9Date = module9Date;
	}

	public boolean isModule10() {
		return module10;
	}

	public void setModule10(boolean module10) {
		this.module10 = module10;
	}

	public LocalDateTime getModule10Date() {
		return module10Date;
	}

	public void setModule10Date(LocalDateTime module10Date) {
		this.module10Date = module10Date;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	
}
