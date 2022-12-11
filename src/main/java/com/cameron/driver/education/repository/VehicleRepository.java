	package com.cameron.driver.education.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>,JpaSpecificationExecutor<Vehicle>,VehicleCustomRepository{

	Vehicle findByVehicleNo(Long vehicleNum);

	
	@Query("select vehicle from Vehicle vehicle where ((vehicle.cvipDate>:startDate and vehicle.cvipDate<:cvipEndDate) or "
			+ "(vehicle.plateNoExp>:startDate and vehicle.plateNoExp<:plateNoEndDate) or "
			+ "(vehicle.insuranceDate>:startDate and vehicle.insuranceDate<:vehicleInsuranceEndDate))"
			+ " and  vehicle.company.id=:company")
	List<Vehicle> getStudentExpiryFieldsNotifications(@Param("company") long company,
			@Param("cvipEndDate") LocalDate cvipEndDate,@Param("plateNoEndDate") LocalDate plateNoEndDate,
			@Param("vehicleInsuranceEndDate") LocalDate vehicleInsuranceEndDate,@Param("startDate") LocalDate startDate);

}
