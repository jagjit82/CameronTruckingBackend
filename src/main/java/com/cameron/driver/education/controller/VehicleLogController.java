 package com.cameron.driver.education.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cameron.driver.education.commonservice.CommonService;
import com.cameron.driver.education.constant.CommonConstants;
import com.cameron.driver.education.constant.ROLES;
import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.DieselLog;
import com.cameron.driver.education.model.Vehicle;
import com.cameron.driver.education.model.VehicleLog;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.VehicleLogRepository;
import com.cameron.driver.education.specs.VehicleSpecs;

@RestController
@RequestMapping("/vehicleLog")
public class VehicleLogController {
	@Autowired
	@Lazy
	private VehicleLogRepository vehicleLogRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	@GetMapping("/all/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<VehicleLog> getAllvehicleLogs(@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
		
		int pageNumber = 0;
		Pageable page = null;
		if(pageNum==null || pageNum==0) {
			pageNumber=0;
		}else
		{
			pageNumber=pageNum.intValue();
		}
		Company companyObj = companyRepostory.findCompanyByName(company);
		if(sortOrder.equalsIgnoreCase("ascending")) {
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).ascending());;
		}else if(sortOrder.equalsIgnoreCase("descending")){
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).descending());
		}
		Page<VehicleLog> vehicleLogList =	vehicleLogRepository.findAll(VehicleSpecs.getvehicleLogsByCompanySpec(companyObj,STATUS.ACTIVE.toString()),page);
		List<VehicleLog> vehicleLogLs = vehicleLogList.getContent();
		
		return vehicleLogLs;
	}

	@GetMapping("/allapprovals/{company}")
	public List<VehicleLog> getAllvehicleApprovalsLogs(@PathVariable(value = "company") String company  ) {
		
		Company companyObj = companyRepostory.findCompanyByName(company);
		List<VehicleLog> vehicleLogLs =	vehicleLogRepository.findAll(VehicleSpecs.getvehicleLogsByCompanySpec(companyObj,STATUS.DELETE.toString()));
		return vehicleLogLs;
	}
	@GetMapping("/all")
	public ResponseEntity<List<VehicleLog>> getAllVehicleLogs() {
		return ResponseEntity.ok().body(vehicleLogRepository.findAll());
	}
	
	@GetMapping("/rejectvehiclelog/{id}")
	public ResponseEntity<String> rejectVehicleDieselLog(@PathVariable(value = "id") Long vehicleId)
			throws ResourceNotFoundException {
		Optional<VehicleLog> vehicleLog= vehicleLogRepository.findById(vehicleId);
				if(vehicleLog.isPresent()) {
					String userName = CommonService.getUsername();
					String userRole = CommonService.getUserRole();
					if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
						VehicleLog vehicle = vehicleLog.get();
						vehicle.setStatus(STATUS.ACTIVE.toString());
						vehicleLogRepository.save(vehicle);
					}
						
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
		
	
	@GetMapping("/count")
	public ResponseEntity<Long> countvehicleLogs() {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		int totalPages = vehicleLogRepository.findAll(page).getTotalPages();
		return ResponseEntity.ok().body(Long.valueOf(totalPages));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getVehicleLogById(@PathVariable(value = "id") Long vehicleLogId)
			throws ResourceNotFoundException {
		Optional<VehicleLog> vehicleLog= vehicleLogRepository.findById(vehicleLogId);
		if(vehicleLog.isPresent()) {
			return ResponseEntity.ok(vehicleLog.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("VehicleLog Not Found");
	}

	@PostMapping("/create")
	public ResponseEntity<String>  createVehicleLog( @RequestBody VehicleLog vehicleLog) {
		try {
			vehicleLog.setCreatedDate(LocalDateTime.now());
			vehicleLog.setModifiedDate(LocalDateTime.now());
			vehicleLog.setStatus(STATUS.ACTIVE.toString());
			vehicleLogRepository.save(vehicleLog);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/update")
	public ResponseEntity<String>  updateVehicleLog(@RequestBody VehicleLog vehicleLog) throws ResourceNotFoundException {
		try {
		vehicleLog.setModifiedDate(LocalDateTime.now());
		vehicleLogRepository.save(vehicleLog);
		return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	
	@DeleteMapping("/{id}")
	public ResponseEntity<String>  deleteVehicleLog(@PathVariable(value = "id") Long vehicleLogId)
			throws ResourceNotFoundException {
		Optional<VehicleLog> vehicleLog= vehicleLogRepository.findById(vehicleLogId);
		if(vehicleLog.isPresent()) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				vehicleLogRepository.delete(vehicleLog.get());
			}else {
				VehicleLog veh = vehicleLog.get();
				veh.setStatus(STATUS.DELETE.toString());
				vehicleLogRepository.save(veh);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
