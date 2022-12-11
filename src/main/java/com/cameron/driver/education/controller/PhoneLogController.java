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
import com.cameron.driver.education.model.FollowUpPhone;
import com.cameron.driver.education.model.PhoneLog;
import com.cameron.driver.education.model.PhoneLogSearch;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.FollowUpPhoneRepository;
import com.cameron.driver.education.repository.PhoneLogRepository;
import com.cameron.driver.education.specs.GenericSpecs;


@RestController
@RequestMapping("/phoneLog")
public class PhoneLogController {
	@Autowired
	@Lazy
	private PhoneLogRepository phoneLogRepository;
	
	@Autowired
	private FollowUpPhoneRepository followUpphoneRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	@GetMapping("/all/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<PhoneLog> getAllPhoneLogs(@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
		
		int pageNumber = 0;
		Pageable page = null;
		if(pageNum==null || pageNum==0) {
			pageNumber=0;
		}else
		{
			pageNumber=pageNum.intValue()-1;
		}
		Sort sort = Sort.by("id").descending();
		Company companyObj = companyRepostory.findCompanyByName(company);
		if(sortOrder.equalsIgnoreCase("ascending")) {
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).ascending());;
		}else if(sortOrder.equalsIgnoreCase("descending")){
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).descending());
		}
		Page<PhoneLog> phoneLogList =	phoneLogRepository.findAll(GenericSpecs.getPhoneLogByCompanySpec(companyObj),page);
	//	List<PhoneLog> phoneLogLs = phoneLogList.getContent();
		
		return phoneLogList.getContent();
	}
	
	@PostMapping("/searchPhoneLog")
	public List<PhoneLog> searchPhoneLog(@RequestBody PhoneLogSearch phoneLog){
		
		int pageNumber = phoneLog.getPageNumber();
		if(pageNumber>0) {
			pageNumber=pageNumber-1;
		}
		Company companyObj = companyRepostory.findCompanyByName(phoneLog.getCompany());
		Pageable page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by("id").descending());
		Page<PhoneLog> phoneLogList =	phoneLogRepository.findAll(GenericSpecs.searchPhoneLogSpec(phoneLog,companyObj),page);
		return phoneLogList.getContent();
	}
	@PostMapping("/searchPhoneLogCount")
	public long searchPhoneLogCount(@RequestBody PhoneLogSearch phoneLog){
		Company companyObj = companyRepostory.findCompanyByName(phoneLog.getCompany());
		return phoneLogRepository.count(GenericSpecs.searchPhoneLogSpec(phoneLog,companyObj));
	}

	@GetMapping("/all")
	public ResponseEntity<List<PhoneLog>> getAllPhoneLogs() {
		return ResponseEntity.ok().body(phoneLogRepository.findAll());
	}
	
	@GetMapping("/count/{company}")
	public ResponseEntity<Long> countPhoneLogs(@PathVariable(value = "company") String company ) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		return ResponseEntity.ok().body(phoneLogRepository.count(GenericSpecs.getPhoneLogByCompanySpec(companyObj)));
	}
	
	@PostMapping("/pageCount")
	public ResponseEntity<Long> countPhoneLogPages( @RequestBody PhoneLog phoneLog) {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		Page<PhoneLog> pagePhoneLog = phoneLogRepository.findAll(new Specification<PhoneLog>() {
	           @Override
	           public Predicate toPredicate(Root<PhoneLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	               List<Predicate> predicates = new ArrayList<>();
				/*
				 * if(phoneLog.getInsuranceDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "insuranceDate"), truck.getInsuranceDate().plusDays(1)))); }
				 * if(phoneLog.getPermitDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "permitDate"), truck.getPermitDate().plusDays(1)))); }
				 * if(phoneLog.getFitnessDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "fitnessDate"), truck.getFitnessDate().plusDays(1)))); }
				 * if(phoneLog.getTruckNum()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("truckNum")
				 * , truck.getTruckNum()))); } if(!StringUtils.isEmpty(truck.getMisc())){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("truckNum"),
				 * "%"+truck.getMisc()+"%"))); }
				 */
	       	       
	       	       return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
	           }
	       },page);
		 return ResponseEntity.ok().body(Long.valueOf(pagePhoneLog.getTotalPages()));
	//	return lsFleetLog;
	}

	@GetMapping("/count")
	public ResponseEntity<Long> countPhoneLogs() {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		int totalPages = phoneLogRepository.findAll(page).getTotalPages();
		return ResponseEntity.ok().body(Long.valueOf(totalPages));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getPhoneLogById(@PathVariable(value = "id") Long phoneLogId)
			throws ResourceNotFoundException {
		Optional<PhoneLog> phoneLog= phoneLogRepository.findById(phoneLogId);
		if(phoneLog.isPresent()) {
			return ResponseEntity.ok(phoneLog.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PhoneLog Not Found");
	}


	@PostMapping("/create")
	public ResponseEntity<String>  createPhoneLog( @RequestBody PhoneLog phoneLog) {
		try {
			phoneLog.setCreatedDate(LocalDateTime.now());
			phoneLog.setModifiedDate(LocalDateTime.now());
			//phoneLog.setStatus(STATUS.ACTIVE.toString());
			phoneLogRepository.save(phoneLog);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/update")
	public ResponseEntity<String>  updatePhoneLog(@RequestBody PhoneLog phoneLog) throws ResourceNotFoundException {
		try {
		phoneLog.setModifiedDate(LocalDateTime.now());
		phoneLogRepository.save(phoneLog);
		return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findByPhoneLogName/{phoneLogNum}")
	public ResponseEntity<Object> getPhoneLogByNum(@PathVariable(value = "phoneLogNum") Long phoneLogNum)
			throws ResourceNotFoundException {
		PhoneLog phoneLog = null;//phoneLogRepository.findByPhoneLogNo(phoneLogNum);
		if(phoneLog==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PhoneLog Not Found");
		}
		return ResponseEntity.ok().body(phoneLog);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String>  deletePhoneLog(@PathVariable(value = "id") Long phoneLogId)
			throws ResourceNotFoundException {
		Optional<PhoneLog> phoneLog= phoneLogRepository.findById(phoneLogId);
		if(phoneLog.isPresent()) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				phoneLogRepository.delete(phoneLog.get());
			}else {
				PhoneLog phone =phoneLog.get();
				phone.setStatus(STATUS.DELETE.toString());
				phoneLogRepository.save(phone);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	

	@GetMapping("/allFollowUps")
	public ResponseEntity<List<FollowUpPhone>> getAllFollowUpPhone() {
		return ResponseEntity.ok().body(followUpphoneRepository.findAll());
	}
	@PostMapping("/createFollowUp")
	public ResponseEntity<String>  createFollowUpPhone( @RequestBody FollowUpPhone followUpPhone) {
		try {
			followUpPhone.setCreatedDate(LocalDateTime.now());
			followUpPhone.setModifiedDate(LocalDateTime.now());
			followUpPhone.setStatus(STATUS.ACTIVE.toString());
			followUpphoneRepository.save(followUpPhone);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/updateFollowUp")
	public ResponseEntity<String>  updateTruck(@RequestBody FollowUpPhone followUpPhone) throws ResourceNotFoundException {
		try {
			followUpPhone.setModifiedDate(LocalDateTime.now());
		followUpphoneRepository.save(followUpPhone);
		return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("followUp/{id}")
	public ResponseEntity<String>  deleteFollowUpPhone(@PathVariable(value = "id") Long followUpId)
			throws ResourceNotFoundException {
		Optional<FollowUpPhone> followUpPhone= followUpphoneRepository.findById(followUpId);
		if(followUpPhone.isPresent()) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				followUpphoneRepository.delete(followUpPhone.get());
			}else {
				FollowUpPhone phoneFollowUp =followUpPhone.get();
				phoneFollowUp.setStatus(STATUS.DELETE.toString());
				followUpphoneRepository.save(phoneFollowUp);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("followUp/{id}")
	public ResponseEntity<Object> getFollowUpPhoneById(@PathVariable(value = "id") Long followUpPhoneId)
			throws ResourceNotFoundException {
		Optional<FollowUpPhone> followUpPhone= followUpphoneRepository.findById(followUpPhoneId);
		if(followUpPhone.isPresent()) {
			return ResponseEntity.ok(followUpPhone.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PhoneLog Not Found");
	}
	
	@GetMapping("/allFollowUpPhone/{company}/{phoneLogId}")
	public List<FollowUpPhone> getAllFollowUpPhone(@PathVariable(value = "company") String company,@PathVariable(value = "phoneLogId") Long phoneLogId  ) {
		
		Company companyObj = companyRepostory.findCompanyByName(company);
		List<FollowUpPhone> followUpPhoneList =	followUpphoneRepository.findAll(GenericSpecs.getFollowUpPhoneByCompanySpec(companyObj,phoneLogId));
		return followUpPhoneList;
	}
}
