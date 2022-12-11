package com.cameron.driver.education.repository;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.cameron.driver.education.model.Vehicle;

@NoRepositoryBean
public interface VehicleCustomRepository {

	List<Vehicle> insuranceExpiryRecordsCurrentMonth();
	List<Vehicle> permitExpiryRecordsCurrentMonth();
	List<Vehicle> fitnessExpiryRecordsCurrentMonth();
}
