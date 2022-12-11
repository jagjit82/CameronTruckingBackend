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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cameron.driver.education.commonservice.CommonService;
import com.cameron.driver.education.constant.CommonConstants;
import com.cameron.driver.education.constant.ROLES;
import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.InstructorMonitoring;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.InstructorMonitoringRepository;
import com.cameron.driver.education.specs.VehicleSpecs;


@RestController
@RequestMapping("/instructorMonitoring")
public class InstructorMonitoringController {
	@Autowired
	@Lazy
	private InstructorMonitoringRepository instructorMonitoringRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	@GetMapping("/all/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<InstructorMonitoring> getAllInstructorMonitorings(@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
		
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
		Page<InstructorMonitoring> instructorMonitoringList =	instructorMonitoringRepository.findAll(VehicleSpecs.getInstructorMonitoringsByCompanySpec(companyObj),page);
		List<InstructorMonitoring> instructorMonitoringLs = instructorMonitoringList.getContent();
		
		return instructorMonitoringLs;
	}

	@GetMapping("/all")
	public ResponseEntity<List<InstructorMonitoring>> getAllInstructorMonitorings() {
		return ResponseEntity.ok().body(instructorMonitoringRepository.findAll());
	}
	
	
	
	@PostMapping("/pageCount")
	public ResponseEntity<Long> countInstructorMonitoringPages( @RequestBody InstructorMonitoring instructorMonitoring) {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		Page<InstructorMonitoring> pageInstructorMonitoring = instructorMonitoringRepository.findAll(new Specification<InstructorMonitoring>() {
	           @Override
	           public Predicate toPredicate(Root<InstructorMonitoring> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	               List<Predicate> predicates = new ArrayList<>();
				/*
				 * if(instructorMonitoring.getInsuranceDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "insuranceDate"), truck.getInsuranceDate().plusDays(1)))); }
				 * if(instructorMonitoring.getPermitDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "permitDate"), truck.getPermitDate().plusDays(1)))); }
				 * if(instructorMonitoring.getFitnessDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "fitnessDate"), truck.getFitnessDate().plusDays(1)))); }
				 * if(instructorMonitoring.getTruckNum()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("truckNum")
				 * , truck.getTruckNum()))); } if(!StringUtils.isEmpty(truck.getMisc())){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("truckNum"),
				 * "%"+truck.getMisc()+"%"))); }
				 */
	       	       
	       	       return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
	           }
	       },page);
		 return ResponseEntity.ok().body(Long.valueOf(pageInstructorMonitoring.getTotalPages()));
	//	return lsFleetLog;
	}

	@GetMapping("/count")
	public ResponseEntity<Long> countInstructorMonitorings() {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		int totalPages = instructorMonitoringRepository.findAll(page).getTotalPages();
		return ResponseEntity.ok().body(Long.valueOf(totalPages));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getInstructorMonitoringById(@PathVariable(value = "id") Long instructorMonitoringId)
			throws ResourceNotFoundException {
		Optional<InstructorMonitoring> instructorMonitoring= instructorMonitoringRepository.findById(instructorMonitoringId);
		if(instructorMonitoring.isPresent()) {
			return ResponseEntity.ok(instructorMonitoring.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("InstructorMonitoring Not Found");
	}

	@PostMapping("/create")
	public ResponseEntity<String>  createInstructorMonitoring( @RequestBody InstructorMonitoring instructorMonitoring) {
		try {
			instructorMonitoring.setCreatedDate(LocalDateTime.now());
			instructorMonitoring.setModifiedDate(LocalDateTime.now());
			instructorMonitoring.setStatus(STATUS.ACTIVE.toString());
			instructorMonitoringRepository.save(instructorMonitoring);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/update")
	public ResponseEntity<String>  updateTruck(@RequestBody InstructorMonitoring instructorMonitoring) throws ResourceNotFoundException {
		try {
		instructorMonitoring.setModifiedDate(LocalDateTime.now());
		instructorMonitoringRepository.save(instructorMonitoring);
		return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findByInstructorMonitoringName/{instructorMonitoringNum}")
	public ResponseEntity<Object> getInstructorMonitoringByNum(@PathVariable(value = "instructorMonitoringNum") Long instructorMonitoringNum)
			throws ResourceNotFoundException {
		InstructorMonitoring instructorMonitoring = null;//instructorMonitoringRepository.findByInstructorMonitoringNo(instructorMonitoringNum);
		if(instructorMonitoring==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("InstructorMonitoring Not Found");
		}
		return ResponseEntity.ok().body(instructorMonitoring);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String>  deleteInstructorMonitoring(@PathVariable(value = "id") Long instructorMonitoringId)
			throws ResourceNotFoundException {
		Optional<InstructorMonitoring> instructorMonitoring= instructorMonitoringRepository.findById(instructorMonitoringId);
		if(instructorMonitoring.isPresent()) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				instructorMonitoringRepository.delete(instructorMonitoring.get());
			}else {
				InstructorMonitoring insMonitor =instructorMonitoring.get();
				insMonitor.setStatus(STATUS.DELETE.toString());
				instructorMonitoringRepository.save(insMonitor);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
