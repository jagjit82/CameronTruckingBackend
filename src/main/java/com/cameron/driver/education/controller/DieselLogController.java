 package com.cameron.driver.education.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.model.Vehicle;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.DieselLogRepository;
import com.cameron.driver.education.specs.VehicleSpecs;


@RestController
@RequestMapping("/dieselLog")
public class DieselLogController {
	@Autowired
	@Lazy
	private DieselLogRepository dieselLogRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	@GetMapping("/all/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<DieselLog> getAllDieselLogs(@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
		
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
		Page<DieselLog> dieselLogList =	dieselLogRepository.findAll(VehicleSpecs.getDieselLogsByCompanySpec(companyObj,STATUS.ACTIVE.toString()),page);
		List<DieselLog> dieselLogLs = dieselLogList.getContent();
		
		return dieselLogLs;
	}
	
	@GetMapping("/allapprovals/{company}")
	public List<DieselLog> getAllDieselLogs(@PathVariable(value = "company") String company  ) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		List<DieselLog> dieselLogLs =	dieselLogRepository.findAll(VehicleSpecs.getDieselLogsByCompanySpec(companyObj,STATUS.DELETE.toString()));
		return dieselLogLs;
	}
	
	@GetMapping("/rejectdieselog/{id}")
	public ResponseEntity<String> rejectDieselLog(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		Optional<DieselLog> dieselLog= dieselLogRepository.findById(studentId);
				if(dieselLog.isPresent()) {
					String userName = CommonService.getUsername();
					String userRole = CommonService.getUserRole();
					if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
						DieselLog diesel =dieselLog.get();
						diesel.setStatus(STATUS.ACTIVE.toString());
						dieselLogRepository.save(diesel);
					}
						
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<DieselLog>> getAllDieselLogs() {
		return ResponseEntity.ok().body(dieselLogRepository.findAll());
	}
	
	
	
	@PostMapping("/pageCount")
	public ResponseEntity<Long> countDieselLogPages( @RequestBody DieselLog dieselLog) {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		Page<DieselLog> pageDieselLog = dieselLogRepository.findAll(new Specification<DieselLog>() {
	           @Override
	           public Predicate toPredicate(Root<DieselLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	               List<Predicate> predicates = new ArrayList<>();
				/*
				 * if(dieselLog.getInsuranceDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "insuranceDate"), truck.getInsuranceDate().plusDays(1)))); }
				 * if(dieselLog.getPermitDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "permitDate"), truck.getPermitDate().plusDays(1)))); }
				 * if(dieselLog.getFitnessDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "fitnessDate"), truck.getFitnessDate().plusDays(1)))); }
				 * if(dieselLog.getTruckNum()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("truckNum")
				 * , truck.getTruckNum()))); } if(!StringUtils.isEmpty(truck.getMisc())){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("truckNum"),
				 * "%"+truck.getMisc()+"%"))); }
				 */
	       	       
	       	       return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
	           }
	       },page);
		 return ResponseEntity.ok().body(Long.valueOf(pageDieselLog.getTotalPages()));
	//	return lsFleetLog;
	}

	@GetMapping("/count")
	public ResponseEntity<Long> countDieselLogs() {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		int totalPages = dieselLogRepository.findAll(page).getTotalPages();
		return ResponseEntity.ok().body(Long.valueOf(totalPages));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getDieselLogById(@PathVariable(value = "id") Long dieselLogId)
			throws ResourceNotFoundException {
		Optional<DieselLog> dieselLog= dieselLogRepository.findById(dieselLogId);
		if(dieselLog.isPresent()) {
			return ResponseEntity.ok(dieselLog.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DieselLog Not Found");
	}

	@PostMapping("/create")
	public ResponseEntity<String>  createDieselLog( @RequestBody DieselLog dieselLog) {
		try {
			dieselLog.setCreatedDate(LocalDateTime.now());
			dieselLog.setModifiedDate(LocalDateTime.now());
			dieselLog.setStatus(STATUS.ACTIVE.toString());
			dieselLogRepository.save(dieselLog);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/update")
	public ResponseEntity<String>  updateTruck(@RequestBody DieselLog dieselLog) throws ResourceNotFoundException {
		try {
		dieselLog.setModifiedDate(LocalDateTime.now());
		dieselLogRepository.save(dieselLog);
		return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findByDieselLogName/{dieselLogNum}")
	public ResponseEntity<Object> getDieselLogByNum(@PathVariable(value = "dieselLogNum") Long dieselLogNum)
			throws ResourceNotFoundException {
		DieselLog dieselLog = null;//dieselLogRepository.findByDieselLogNo(dieselLogNum);
		if(dieselLog==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DieselLog Not Found");
		}
		return ResponseEntity.ok().body(dieselLog);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String>  deleteDieselLog(@PathVariable(value = "id") Long dieselLogId)
			throws ResourceNotFoundException {
		Optional<DieselLog> dieselLog= dieselLogRepository.findById(dieselLogId);
		if(dieselLog.isPresent()) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				dieselLogRepository.delete(dieselLog.get());
			}else {
				DieselLog dieselLogObj = dieselLog.get();
				dieselLogObj.setStatus(STATUS.DELETE.toString());
				dieselLogRepository.save(dieselLogObj);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
