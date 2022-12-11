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
import com.cameron.driver.education.model.ActivityLog;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.repository.ActivityLogRepository;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.specs.ActivityLogSpecs;

@RestController
@RequestMapping("/activitylog")
public class ActivityLogController {
	@Autowired
	@Lazy
	private ActivityLogRepository activityLogRepository;

	@Autowired
	private CompanyRepository companyRepostory;
	
	@GetMapping("/all/{company}")
	public List<ActivityLog> getAllActivityLogs(
			@PathVariable(value = "company") String company) {

		Company companyObj = companyRepostory.findCompanyByName(company);
		List<ActivityLog> activityLogList = activityLogRepository.findAll(ActivityLogSpecs.getActivityLogsByCompanySpec(companyObj));
		return activityLogList;
	}

	@GetMapping("/all")
	public ResponseEntity<List<ActivityLog>> getAllActivityLogs() {
		return ResponseEntity.ok().body(activityLogRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getActivityLogById(@PathVariable(value = "id") Long activityLogId)
			throws ResourceNotFoundException {
		Optional<ActivityLog> activityLog = activityLogRepository.findById(activityLogId);
		if (activityLog.isPresent()) {
			return ResponseEntity.ok(activityLog.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ActivityLog Not Found");
	}

	@PostMapping("/create")
	public ResponseEntity<String> createActivityLog(@RequestBody ActivityLog activityLog) {
		try {
			activityLog.setCreatedDate(LocalDateTime.now());
			activityLog.setModifiedDate(LocalDateTime.now());
			activityLog.setStatus(STATUS.ACTIVE.toString());
			activityLog.setReportedBy(CommonService.getUsername());
			activityLogRepository.save(activityLog);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateActivityLog(@RequestBody ActivityLog activityLog) throws ResourceNotFoundException {
		try {
			activityLog.setModifiedDate(LocalDateTime.now());
			activityLogRepository.save(activityLog);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteActivityLog(@PathVariable(value = "id") Long activityLogId)
			throws ResourceNotFoundException {
		Optional<ActivityLog> activityLog = activityLogRepository.findById(activityLogId);
		if (activityLog.isPresent()) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				activityLogRepository.delete(activityLog.get());
			}else {
				ActivityLog veh = activityLog.get();
				veh.setStatus(STATUS.DELETE.toString());
				activityLogRepository.save(veh);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	}
