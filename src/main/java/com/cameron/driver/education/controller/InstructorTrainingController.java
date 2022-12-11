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
import com.cameron.driver.education.model.InstructorTraining;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.InstructorTrainingRepository;
import com.cameron.driver.education.specs.VehicleSpecs;


@RestController
@RequestMapping("/instructorTraining")
public class InstructorTrainingController {
	@Autowired
	@Lazy
	private InstructorTrainingRepository instructorTrainingRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	@GetMapping("/all/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<InstructorTraining> getAllInstructorTrainings(@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
		
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
		Page<InstructorTraining> instructorTrainingList =	instructorTrainingRepository.findAll(VehicleSpecs.getInstructorTrainingsByCompanySpec(companyObj),page);
		List<InstructorTraining> instructorTrainingLs = instructorTrainingList.getContent();
		
		return instructorTrainingLs;
	}

	@GetMapping("/all")
	public ResponseEntity<List<InstructorTraining>> getAllInstructorTrainings() {
		return ResponseEntity.ok().body(instructorTrainingRepository.findAll());
	}
	
	
	
	@PostMapping("/pageCount")
	public ResponseEntity<Long> countInstructorTrainingPages( @RequestBody InstructorTraining instructorTraining) {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		Page<InstructorTraining> pageInstructorTraining = instructorTrainingRepository.findAll(new Specification<InstructorTraining>() {
	           @Override
	           public Predicate toPredicate(Root<InstructorTraining> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	               List<Predicate> predicates = new ArrayList<>();
				/*
				 * if(instructorTraining.getInsuranceDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "insuranceDate"), truck.getInsuranceDate().plusDays(1)))); }
				 * if(instructorTraining.getPermitDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "permitDate"), truck.getPermitDate().plusDays(1)))); }
				 * if(instructorTraining.getFitnessDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "fitnessDate"), truck.getFitnessDate().plusDays(1)))); }
				 * if(instructorTraining.getTruckNum()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("truckNum")
				 * , truck.getTruckNum()))); } if(!StringUtils.isEmpty(truck.getMisc())){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("truckNum"),
				 * "%"+truck.getMisc()+"%"))); }
				 */
	       	       
	       	       return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
	           }
	       },page);
		 return ResponseEntity.ok().body(Long.valueOf(pageInstructorTraining.getTotalPages()));
	//	return lsFleetLog;
	}

	@GetMapping("/count")
	public ResponseEntity<Long> countInstructorTrainings() {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		int totalPages = instructorTrainingRepository.findAll(page).getTotalPages();
		return ResponseEntity.ok().body(Long.valueOf(totalPages));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getInstructorTrainingById(@PathVariable(value = "id") Long instructorTrainingId)
			throws ResourceNotFoundException {
		Optional<InstructorTraining> instructorTraining= instructorTrainingRepository.findById(instructorTrainingId);
		if(instructorTraining.isPresent()) {
			return ResponseEntity.ok(instructorTraining.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("InstructorTraining Not Found");
	}

	@PostMapping("/create")
	public ResponseEntity<String>  createInstructorTraining( @RequestBody InstructorTraining instructorTraining) {
		try {
			instructorTraining.setCreatedDate(LocalDateTime.now());
			instructorTraining.setModifiedDate(LocalDateTime.now());
			instructorTraining.setStatus(STATUS.ACTIVE.toString());
			instructorTrainingRepository.save(instructorTraining);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/update")
	public ResponseEntity<String>  updateTruck(@RequestBody InstructorTraining instructorTraining) throws ResourceNotFoundException {
		try {
		instructorTraining.setModifiedDate(LocalDateTime.now());
		instructorTrainingRepository.save(instructorTraining);
		return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findByInstructorTrainingName/{instructorTrainingNum}")
	public ResponseEntity<Object> getInstructorTrainingByNum(@PathVariable(value = "instructorTrainingNum") Long instructorTrainingNum)
			throws ResourceNotFoundException {
		InstructorTraining instructorTraining = null;//instructorTrainingRepository.findByInstructorTrainingNo(instructorTrainingNum);
		if(instructorTraining==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("InstructorTraining Not Found");
		}
		return ResponseEntity.ok().body(instructorTraining);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String>  deleteInstructorTraining(@PathVariable(value = "id") Long instructorTrainingId)
			throws ResourceNotFoundException {
		Optional<InstructorTraining> instructorTraining= instructorTrainingRepository.findById(instructorTrainingId);
		if(instructorTraining.isPresent()) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				instructorTrainingRepository.delete(instructorTraining.get());
			}else {
				InstructorTraining insTraining =instructorTraining.get();
				insTraining.setStatus(STATUS.DELETE.toString());
				instructorTrainingRepository.save(insTraining);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
