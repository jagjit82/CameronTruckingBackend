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
@Table(name = "vehicle_log")
@CrossOrigin("*")
public class VehicleLog implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_VEHICLE_ID", insertable = true, updatable = true ,nullable=false)
	private Vehicle vehicle;
	@Column(name="repair_date",nullable=false)
	private LocalDate repairDate;
	@Column(name="repair_amount")
	private Double repairAmt;
	@Column(name="labour_amount")
	private Double labourAmt;
	@Column(name="mechanic_name")
	private String mechanicName;
	@Column(name="description")
	private String description;
	@Column(name="labout_parts")
	private String labourParts;
	@Column(name="diesel")
	private Long diesel;
	@Column(name="status")
	private String status;
	@Column(name="created_date")
	private LocalDateTime createdDate;
	@Column(name="modified_date")
	private LocalDateTime modifiedDate;
	
	
	public VehicleLog() {
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public Vehicle getTruck() {
		return vehicle;
	}

	public void setTruck(Vehicle truck) {
		this.vehicle = truck;
	}

	public Long getDiesel() {
		return diesel;
	}

	public void setDiesel(Long diesel) {
		this.diesel = diesel;
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

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public LocalDate getRepairDate() {
		return repairDate;
	}

	public void setRepairDate(LocalDate repairDate) {
		this.repairDate = repairDate;
	}

	public Double getRepairAmt() {
		return repairAmt;
	}

	public void setRepairAmt(Double repairAmt) {
		this.repairAmt = repairAmt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMechanicName() {
		return mechanicName;
	}

	public void setMechanicName(String mechanicName) {
		this.mechanicName = mechanicName;
	}

	public Double getLabourAmt() {
		return labourAmt;
	}

	public void setLabourAmt(Double labourAmt) {
		this.labourAmt = labourAmt;
	}

	public String getLabourParts() {
		return labourParts;
	}

	public void setLabourParts(String labourParts) {
		this.labourParts = labourParts;
	}

	
}
