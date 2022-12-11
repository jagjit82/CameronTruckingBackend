 package com.cameron.driver.education.controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
import com.cameron.driver.education.model.CompanyDetails;
import com.cameron.driver.education.model.Instructor;
import com.cameron.driver.education.model.InstructorLog;
import com.cameron.driver.education.model.InstructorLogSearch;
import com.cameron.driver.education.model.InstructorVO;
import com.cameron.driver.education.model.Reminders;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.InstructorLogRepository;
import com.cameron.driver.education.repository.InstructorRepository;
import com.cameron.driver.education.repository.RemindersRepository;
import com.cameron.driver.education.specs.GenericSpecs;
import com.cameron.driver.education.specs.InstructorSpecs;

@RestController
@RequestMapping("/instructor")
public class InstructorController {
	@Autowired
	private InstructorRepository instructorRepository;
	
	@Autowired
	private RemindersRepository reminderRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private InstructorLogRepository instructorLogRepository;
	
	
	@GetMapping("/all/{company}")
	public List<Instructor> getInstructors(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepository.findCompanyByName(company);
		Sort sort = Sort.by("id").descending();
		List<Instructor> instructorLs =	instructorRepository.findAll(InstructorSpecs.getInstructorsByCompanySpec(companyObj.getId()),sort);
		//List<Instructor> instructorLs = instructorList.getContent();
	
		return instructorLs;
	}
	
	@GetMapping("/countdata/{company}")
	public ResponseEntity<Long> countAllInstructors(@PathVariable(value = "company") String company ) {
		Company companyObj = companyRepository.findCompanyByName(company);
		return ResponseEntity.ok().body(instructorRepository.count(InstructorSpecs.getInstructorsByCompanySpec(companyObj.getId())));
	}
	
	@GetMapping("/alldata/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<Instructor> getInstructors(@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
		int pageNumber = 0;
		sortField="id";
		Pageable page = null;
		if(pageNum==null || pageNum==0) {
			pageNumber=0;
		}else
		{
			pageNumber=pageNum.intValue()-1;
		}
		//Sort sort = Sort.by("id").descending();
		Company companyObj = companyRepository.findCompanyByName(company);
		if(sortOrder.equalsIgnoreCase("ascending")) {
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).ascending());
		}else if(sortOrder.equalsIgnoreCase("descending")){
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).descending());
		}	
		Page<Instructor> instructorLs =	instructorRepository.findAll(InstructorSpecs.getInstructorsByCompanySpec(companyObj.getId()),page);
		//List<Instructor> instructorLs = instructorList.getContent();
	
		return instructorLs.getContent();
	}
	
	@PostMapping("/searchinstructorLogs")
	public List<InstructorLog> getAllInstructorLogs(@RequestBody InstructorLogSearch instructorLogVO) {
		Company companyObj = companyRepository.findCompanyByName(instructorLogVO.getCompany());
		List<InstructorLog> instructorLogList =	instructorLogRepository.findAll(InstructorSpecs.getInstructorLogsBySearch(instructorLogVO,companyObj.getId()));
			
		return instructorLogList;
	}
	
	@PostMapping("/sumhourinstructorLogssearch")
	public double sumAllInstructorLogs(@RequestBody InstructorLogSearch instructorLogVO) {
		Company companyObj = companyRepository.findCompanyByName(instructorLogVO.getCompany());
		List<InstructorLog> instructorLogList =	instructorLogRepository.findAll(InstructorSpecs.getInstructorLogsBySearch(instructorLogVO,companyObj.getId()));
			
		return instructorLogList.stream().mapToDouble(obj->obj.getHours()).sum();
	}
	
	@PostMapping("/all")
	public List<Instructor> getAllInstructors(@RequestBody InstructorVO instructorVO) {
		int pageNumber = 0;
		Pageable page = PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by("id").ascending());
		List<Instructor> instructorList=null; 
		if(instructorVO.getPageNum() == 0) {
			pageNumber=0;
		}else
		{
			pageNumber=instructorVO.getPageNum();
		}
		Sort sort = Sort.by("id").descending();
		if(StringUtils.isEmpty(instructorVO.getSearchInstructor()) &&  !StringUtils.isEmpty(instructorVO.getCompany()!=null)) {
			
			
			instructorList =    instructorRepository.findAll(InstructorSpecs.getInstructorsByCompanySpec(instructorVO),sort);
			
		}else {
		  
		  instructorList =
		  instructorRepository.findAll(InstructorSpecs.getInstructorsByInstructorSpec(instructorVO),sort);
		  }
		 
		return instructorList;
	}
	
	@PostMapping("/count")
	public ResponseEntity<Long> countInstructors(@RequestBody InstructorVO instructorVO) {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		Page<Instructor> instructorList=null;
		int totalPages = 0;
		if(instructorVO.getSearchInstructor()==null || StringUtils.isEmpty(instructorVO.getSearchInstructor())){
			totalPages = instructorRepository.findAll(page).getTotalPages();
		}else {
			instructorList = null;//instructorRepository.searchInstructorByName(instructorVO.getSearchInstructor(), instructorVO.getPageNum());
			totalPages=instructorList.getTotalPages();
		}
		return ResponseEntity.ok().body(Long.valueOf(totalPages));
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object> getInstructorById(@PathVariable(value = "id") Long instructorId)
			throws ResourceNotFoundException {
		Optional<Instructor> instructor = instructorRepository.findById(instructorId);
			if(instructor.isPresent()) {
			   return ResponseEntity.ok(instructor.get());
			}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instructor Not Found");
	}
	

	@PostMapping("/create")
	public ResponseEntity<String> createInstructor( @RequestBody Instructor instructor) {
		try {
			instructor.setCreatedDate(LocalDateTime.now());
			instructor.setModifiedDate(LocalDateTime.now());
			instructor.setStatus(STATUS.ACTIVE.toString());
			instructorRepository.save(instructor);
		return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateInstructor(@RequestBody Instructor instructor) throws ResourceNotFoundException {
		try {
			instructorRepository.save(instructor);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Long> deleteInstructor(@PathVariable(value = "id") Long instructorId)
			throws ResourceNotFoundException {
		Instructor instructor = instructorRepository.findById(instructorId)
				.orElse(null);
		if(instructor!=null) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				instructorRepository.delete(instructor);
			}else {
				instructor.setStatus(STATUS.DELETE.toString());
				instructorRepository.save(instructor);
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/allInstructorExpiryReminder/{company}")
	public ResponseEntity<List<Instructor>> getAllInstructorExpiryReminder(@PathVariable(value = "company") String company) {
		List<Reminders> reminderLs= reminderRepository.findAll();
		if(reminderLs.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Reminders reminder =reminderLs.stream().findFirst().get();
		Company companyObj = companyRepository.findCompanyByName(company);
		LocalDate instructorLicenceExp =LocalDate.now().plusDays(reminder.getInstructorLicenceExp());
		LocalDate operatorLicenceExp =LocalDate.now().plusDays(reminder.getOperatorLicenceExp());
		LocalDate today=LocalDate.now();
		List<Instructor> listObj = instructorRepository.getInstructorExpiryNotifications(companyObj.getId(), today, instructorLicenceExp, operatorLicenceExp);
		return ResponseEntity.ok().body(listObj);
	}
	
	@GetMapping("/countInstructorExpiryReminder/{company}")
	public ResponseEntity<Integer> countAllInstructorExpiryReminder(@PathVariable(value = "company") String company) {
		List<Reminders> reminderLs= reminderRepository.findAll();
		if(reminderLs.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Reminders reminder =reminderLs.stream().findFirst().get();
		Company companyObj = companyRepository.findCompanyByName(company);
		LocalDate instructorLicenceExp =LocalDate.now().plusDays(reminder.getInstructorLicenceExp());
		LocalDate operatorLicenceExp =LocalDate.now().plusDays(reminder.getOperatorLicenceExp());
		LocalDate today=LocalDate.now();
		Integer listObj = instructorRepository.countInstructorExpiryNotifications(companyObj.getId(), today, instructorLicenceExp, operatorLicenceExp);
		return ResponseEntity.ok().body(listObj);
	}
	
	//Instructor Log goes below 
	
	@GetMapping("/allinstructorlog/{company}")
	public ResponseEntity<List<InstructorLog>> getAllInstructorLogs(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepository.findCompanyByName(company);
		return ResponseEntity.ok().body(instructorLogRepository.findAll(InstructorSpecs.getInstructorLogByCompanySpec(companyObj.getId(), STATUS.ACTIVE.toString())));
	}
	

	@PostMapping("/createinstructorlog")
	public ResponseEntity<String>  createInstructorLog( @RequestBody InstructorLog instructorLog) {
		try {
			DecimalFormat format = new DecimalFormat("#.##");
			long minutes = instructorLog.getStartTime().until(instructorLog.getEndTime(), ChronoUnit.MINUTES);
			double hours = Double.valueOf(format.format(minutes/60.0));
			instructorLog.setHours(hours);
			instructorLog.setCreatedDate(LocalDateTime.now());
			instructorLog.setModifiedDate(LocalDateTime.now());
			instructorLog.setStatus(STATUS.ACTIVE.toString());
			instructorLogRepository.save(instructorLog);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/updateinstructorlog")
	public ResponseEntity<String>  updateTruck(@RequestBody InstructorLog instructorLog) throws ResourceNotFoundException {
		try {
			DecimalFormat format = new DecimalFormat("#.##");
			long minutes = instructorLog.getStartTime().until(instructorLog.getEndTime(), ChronoUnit.MINUTES);
			double hours = Double.valueOf(format.format(minutes/60.0));
			instructorLog.setHours(hours);
		
		instructorLog.setModifiedDate(LocalDateTime.now());
		instructorLogRepository.save(instructorLog);
		return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("instructorlog/{id}")
	public ResponseEntity<String>  deleteInstructorLog(@PathVariable(value = "id") Long instructorLogId)
			throws ResourceNotFoundException {
		Optional<InstructorLog> instructorLog= instructorLogRepository.findById(instructorLogId);
		if(instructorLog.isPresent()) {
				instructorLogRepository.delete(instructorLog.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	@GetMapping("instructorlog/{id}")
	public ResponseEntity<Object>  getInstructorLog(@PathVariable(value = "id") Long instructorLogId)
			throws ResourceNotFoundException {
		Optional<InstructorLog> instructorLog= instructorLogRepository.findById(instructorLogId);
		if(instructorLog.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(instructorLog.get());
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	//Instructor Log ends here
	}
