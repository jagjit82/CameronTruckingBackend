package com.cameron.driver.education.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cameron.driver.education.model.VehicleLog;
import com.cameron.driver.education.repository.VehicleLogRepository;
@Service
public class VehicleSericeLogImpl implements VehicleLogService {
	@Autowired
	private VehicleLogRepository vehicleServiceRepository;
	
	@Override
	public List<VehicleLog> searchVehicleServiceLog(VehicleLog vehicleServiceLog){
		List<VehicleLog> vehicleServiceLogs = vehicleServiceRepository.searchVehicleSeriveLog(vehicleServiceLog);
		return vehicleServiceLogs;
	}	
	
}
