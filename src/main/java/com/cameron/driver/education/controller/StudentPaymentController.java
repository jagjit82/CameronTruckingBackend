package com.cameron.driver.education.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cameron.driver.education.model.Reminders;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.model.StudentPayment;
import com.cameron.driver.education.model.StudentVO;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.RemindersRepository;
import com.cameron.driver.education.repository.StudentPaymentRepository;
import com.cameron.driver.education.specs.GenericSpecs;

@RestController
@RequestMapping({ "/studentPayment" })
public class StudentPaymentController {
	@Autowired
	private StudentPaymentRepository studentPaymentRepository;

	@Autowired
	private RemindersRepository remindersRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	
	@GetMapping("/all/{company}")
	public ResponseEntity<List<StudentPayment>> getAllStudentPayments(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		return ResponseEntity.ok().body(studentPaymentRepository.findAll(GenericSpecs.getStudentPaymentByCompanySpec(companyObj,STATUS.ACTIVE.toString())));
	}
	

	@GetMapping("/count/{company}")
	public ResponseEntity<Long> getAllStudentPaymentsCount(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		long count =studentPaymentRepository.count(GenericSpecs.getStudentPaymentByCompanySpec(companyObj,STATUS.ACTIVE.toString()));
		return ResponseEntity.ok().body(count);
	}

	@GetMapping("/all/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<StudentPayment> getAllStudentPayments(@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
		
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
		Company companyObj = companyRepostory.findCompanyByName(company);
		if(sortOrder.equalsIgnoreCase("ascending")) {
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).ascending());
		}else if(sortOrder.equalsIgnoreCase("descending")){
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).descending());
		}	
		Page<StudentPayment> studentList =	studentPaymentRepository.findAll(GenericSpecs.getStudentPaymentByCompanySpec(companyObj,STATUS.ACTIVE.toString()),page);
		return studentList.getContent();
	}
	
	
	@GetMapping("/allapprovals/{company}")
	public ResponseEntity<List<StudentPayment>> getAllApprovals(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		return ResponseEntity.ok().body(studentPaymentRepository.findAll(GenericSpecs.getStudentPaymentByCompanySpec(companyObj,STATUS.DELETE.toString())));
	}
	
	@GetMapping("/allStudentPaymentReminder/{company}")
	public ResponseEntity<List<StudentVO>> getAllStudentPaymentsReminder(@PathVariable(value = "company") String company) {
		List<Reminders> reminderLs= remindersRepository.findAll();
		if(reminderLs.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Reminders reminder =reminderLs.stream().findFirst().get();
		Company companyObj = companyRepostory.findCompanyByName(company);
		LocalDate paymentDateReminder =LocalDate.now().minusDays(reminder.getPayment());
		List<Object[]> listObj = studentPaymentRepository.getStudentandPaymentsReminder(companyObj.getId(), paymentDateReminder);
		List<StudentVO> studVOList=new ArrayList<StudentVO>();
		if(CollectionUtils.isNotEmpty(listObj)) {
			listObj.forEach(obj->{
			StudentPayment studentPayment =(StudentPayment)obj[0];
			long amtReceived=(long)obj[1];
			Student student= studentPayment.getStudent();
			double totalPayment=student.getTotalAmount()-(student.getTotalAmount()*student.getDiscount()/100);
			if((totalPayment-amtReceived)>0) {
				studVOList.add(new StudentVO(studentPayment,amtReceived));
			}
			});
		}
		return ResponseEntity.ok().body(studVOList);
	}
	
	@GetMapping("/countAllStudentPaymentReminder/{company}")
	public ResponseEntity<Integer> getCountAllStudentPaymentsReminder(@PathVariable(value = "company") String company) {
		List<Reminders> reminderLs= remindersRepository.findAll();
		if(reminderLs.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Reminders reminder =reminderLs.stream().findFirst().get();
		Company companyObj = companyRepostory.findCompanyByName(company);
		LocalDate paymentDateReminder =LocalDate.now().minusDays(reminder.getPayment());
		List<Object[]> listObj = studentPaymentRepository.getStudentandPaymentsReminder(companyObj.getId(), paymentDateReminder);
		List<StudentVO> studVOList=new ArrayList<StudentVO>();
		if(CollectionUtils.isNotEmpty(listObj)) {
			listObj.forEach(obj->{
			StudentPayment studentPayment =(StudentPayment)obj[0];
			long amtReceived=(long)obj[1];
			Student student= studentPayment.getStudent();
			double totalPayment=student.getTotalAmount()-(student.getTotalAmount()*student.getDiscount()/100);
			if((totalPayment-amtReceived)>0) {
				studVOList.add(new StudentVO(studentPayment,amtReceived));
			}
			});
		}
		return ResponseEntity.ok().body(studVOList!=null?studVOList.size():0);
	}
	@GetMapping("/totalPayments/{studentId}")
	public ResponseEntity<String> getTotalStudentPayment(@PathVariable(value = "studentId") Long studentId) {
		return ResponseEntity.ok().body(studentPaymentRepository.getTotalPaymentByStudent(studentId));
	}
	@GetMapping("/totalPaymentsForEdit/{studentId}/{studentPaymentId}")
	public ResponseEntity<String> getTotalStudentPaymentForEdit(@PathVariable(value = "studentId") Long studentId,@PathVariable(value = "studentPaymentId") Long studentPaymentId) {
		return ResponseEntity.ok().body(studentPaymentRepository.getTotalPaymentByStudentForEdit(studentId,studentPaymentId));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getStudentPaymentById(@PathVariable(value = "id") Long studentPaymentId)
			throws ResourceNotFoundException {
		Optional<StudentPayment> studentPayment = studentPaymentRepository.findById(studentPaymentId);
				if(studentPayment.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(studentPayment.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("StudentPayment Not Found");
	}
	
	@PostMapping("/create")
	public ResponseEntity<String> createStudentPayment(@RequestBody StudentPayment studentPayment) {
		try {
			studentPayment.setStatus(STATUS.ACTIVE.toString());
			studentPayment.setCreatedDate(LocalDateTime.now());
			studentPayment.setModifiedDate(LocalDateTime.now());
			//studentPayment.setPassowrd(encryptPassword(studentPayment.getPassowrd()));
			studentPaymentRepository.save(studentPayment);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	@PutMapping("/update")
	public ResponseEntity<String> updateStudentPayment(@RequestBody StudentPayment studentPayment) throws ResourceNotFoundException {
		try {
			studentPaymentRepository.save(studentPayment);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteStudentPayment(@PathVariable(value = "id") Long studentPaymentId)
			throws ResourceNotFoundException {
		Optional<StudentPayment> studentPayment = studentPaymentRepository.findById(studentPaymentId);
					if(studentPayment.isPresent()) {
						String userName = CommonService.getUsername();
						String userRole = CommonService.getUserRole();
						if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
							studentPaymentRepository.delete(studentPayment.get());
						}else {
							StudentPayment studPay =studentPayment.get();
							studPay.setStatus(STATUS.DELETE.toString());
							studentPaymentRepository.save(studPay);
						}
						return new ResponseEntity<>(HttpStatus.OK);
					}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("rejectStudentPayment/{id}")
	public ResponseEntity<String> rejectStudentPayment(@PathVariable(value = "id") Long studentPaymentId)
			throws ResourceNotFoundException {
		Optional<StudentPayment> studentPayment = studentPaymentRepository.findById(studentPaymentId);
					if(studentPayment.isPresent()) {
						String userName = CommonService.getUsername();
						String userRole = CommonService.getUserRole();
						if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
							StudentPayment studPay =studentPayment.get();
							studPay.setStatus(STATUS.ACTIVE.toString());
							studentPaymentRepository.save(studPay);
						}
						return new ResponseEntity<>(HttpStatus.OK);
					}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

		
}
