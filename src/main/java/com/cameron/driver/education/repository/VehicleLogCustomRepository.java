package com.cameron.driver.education.repository;

import java.util.List;

import com.cameron.driver.education.model.VehicleLog;

public interface VehicleLogCustomRepository {

	List<VehicleLog> searchVehicleSeriveLog(VehicleLog fleetLog);
	

}
