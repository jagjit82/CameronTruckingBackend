package com.cameron.driver.education.controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cameron.driver.education.common.CommonUtils;
import com.cameron.driver.education.commonservice.CommonService;
import com.cameron.driver.education.constant.CommonConstants;
import com.cameron.driver.education.constant.ROLES;
import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.model.StudentCourse;
import com.cameron.driver.education.model.StudentCourseDetails;
import com.cameron.driver.education.model.StudentLog;
import com.cameron.driver.education.model.StudentPayment;
import com.cameron.driver.education.model.StudentResult;
import com.cameron.driver.education.model.StudentSearch;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.StudentCourseDetailsRepository;
import com.cameron.driver.education.repository.StudentCourseRepository;
import com.cameron.driver.education.repository.StudentLogRepository;
import com.cameron.driver.education.repository.StudentPaymentRepository;
import com.cameron.driver.education.repository.StudentRepository;
import com.cameron.driver.education.repository.StudentResultRepository;
import com.cameron.driver.education.specs.GenericSpecs;

@RestController
@RequestMapping({ "/student" })
public class StudentController {
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentPaymentRepository studentPaymentRepository;
	
	@Autowired
	private StudentLogRepository studentLogRepository;
	
	@Autowired
	private StudentResultRepository studentResultRepository;
	
	@Autowired
	private StudentCourseRepository studentCourseRepository;
	
	@Autowired
	private StudentCourseDetailsRepository studentCourseDetailsRepository;

	@Autowired
	private CompanyRepository companyRepository;
	@GetMapping("/all/{company}")
	public ResponseEntity<List<Student>> getAllStudents(@PathVariable(value = "company") String company ) {
		Company companyObj = companyRepository.findCompanyByName(company);
		return ResponseEntity.ok().body(studentRepository.findAll(GenericSpecs.getStudentByCompanySpec(companyObj, STATUS.ACTIVE.toString())));
	}
	
	@GetMapping("/count/{company}")
	public ResponseEntity<Long> countAllStudents(@PathVariable(value = "company") String company ) {
		Company companyObj = companyRepository.findCompanyByName(company);
		return ResponseEntity.ok().body(studentRepository.count(GenericSpecs.getStudentByCompanySpec(companyObj, STATUS.ACTIVE.toString())));
	}
	
	@GetMapping("/all/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<Student> getStudents(@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
		
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
		Page<Student> studentList =	studentRepository.findAll(GenericSpecs.getStudentByCompanySpec(companyObj,STATUS.ACTIVE.toString()),page);
		return studentList.getContent();
	}
	
	@GetMapping("/allapprovals/{company}")
	public List<Student> getStudents(@PathVariable(value = "company") String company  ) {
		Company companyObj = companyRepository.findCompanyByName(company);
		List<Student> studentLs =	studentRepository.findAll(GenericSpecs.getStudentByCompanySpec(companyObj,STATUS.DELETE.toString()));
		studentLs.forEach(
				stud->{
					Hibernate.initialize(stud.getStudentResult());
					});
		return studentLs;
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object> getStudentById(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		Optional<Student> student = studentRepository.findById(studentId);
				if(student.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(student.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Not Found");
	}
	
	@GetMapping("email/{email}")
	public ResponseEntity<Object> getStudentByEmail(@PathVariable(value = "email") String email)
			throws ResourceNotFoundException {
		Optional<Student> student = studentRepository.findOne(GenericSpecs.getStudentByEmailSpec(email));
				if(student.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(student.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Not Found");
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<String> createStudent(@RequestBody Student student) {
		try {
			student.setStatus(STATUS.ACTIVE.toString());
			student.setCreatedDate(LocalDateTime.now());
			student.setModifiedDate(LocalDateTime.now());
			//student.setPassowrd(encryptPassword(student.getPassowrd()));
			Student studObj = studentRepository.save(student);
			studentPaymentRepository.save(initiateStudPayment(studObj));
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	@PutMapping("/update")
	public ResponseEntity<String> updateStudent(@RequestBody Student student) throws ResourceNotFoundException {
		try {
			studentRepository.save(student);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteStudent(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		Optional<Student> student = studentRepository.findById(studentId);
				if(student.isPresent()) {
					String userName = CommonService.getUsername();
					String userRole = CommonService.getUserRole();
					if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
						studentRepository.delete(student.get());
					}else {
						Student stud =student.get();
						stud.setStatus(STATUS.DELETE.toString());
						studentRepository.save(stud);
					}
						
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/rejectstudent/{id}")
	public ResponseEntity<String> rejectStudent(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		Optional<Student> student = studentRepository.findById(studentId);
				if(student.isPresent()) {
					String userName = CommonService.getUsername();
					String userRole = CommonService.getUserRole();
					if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
						Student stud =student.get();
						stud.setStatus(STATUS.ACTIVE.toString());
						studentRepository.save(stud);
					}
						
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/ACTIVATE/{id}")
	public ResponseEntity<String> activateStudent(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		Optional<Student> student = studentRepository.findById(studentId);
		if(student.isPresent()) {
			Student emp=student.get();
			emp.setStatus(STATUS.ACTIVE.toString());
			studentRepository.save(emp);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/DEACTIVATE/{id}")
	public ResponseEntity<String> deactivateStudent(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		Optional<Student> student = studentRepository.findById(studentId);
		if(student.isPresent()) {
			Student emp=student.get();
			emp.setStatus(STATUS.INACTIVE.toString());
			studentRepository.save(emp);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	private String encryptPassword(String pwd) {
		BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
		return bcryptEncoder.encode(pwd);
	}
	
	private StudentPayment initiateStudPayment(Student stud) {
		StudentPayment studPay = new StudentPayment();
		studPay.setCreatedDate(LocalDateTime.now());
		studPay.setModifiedDate(LocalDateTime.now());
		studPay.setStatus(STATUS.ACTIVE.toString());
		studPay.setPayment(stud.getPaymentsReceived());
		studPay.setPaymentDate(LocalDate.now());
		studPay.setRemarks("First Pay Cheque");
		studPay.setStudent(stud);
		return studPay;
		
	}
	
	@PostMapping("/createstudentresult")
	public ResponseEntity<String> createStudentResult(@RequestBody StudentResult studentResult) {
		try {
			 studentResult.setStatus(STATUS.ACTIVE.toString());
			 studentResult.setCreatedDate(LocalDateTime.now());
			 studentResult.setModifiedDate(LocalDateTime.now());
			//studentPayment.setPassowrd(encryptPassword(studentPayment.getPassowrd()));
			studentResultRepository.save( studentResult);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	@PutMapping("/updatestudentresult")
	public ResponseEntity<String> updateStudentResult(@RequestBody StudentResult studentResult) throws ResourceNotFoundException {
		try {
			studentResultRepository.save(studentResult);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	
	@DeleteMapping("/studentresult/{id}")
	public ResponseEntity<String> deleteStudentResult(@PathVariable(value = "id") Long studentResultId)
			throws ResourceNotFoundException {
		Optional<StudentResult> studentResult = studentResultRepository.findById(studentResultId);
				if(studentResult.isPresent()) {
					String userName = CommonService.getUsername();
					String userRole = CommonService.getUserRole();
					if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
						studentResultRepository.delete(studentResult.get());
					}else {
						StudentResult studRes =studentResult.get();
						studRes.setStatus(STATUS.DELETE.toString());
						studentResultRepository.save(studRes);
					}
						
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/studentresult/rejectDeletion/{id}")
	public ResponseEntity<String>  rejectStudResults(@PathVariable(value = "id") Long id ) {
			Optional<StudentResult> studentResult = studentResultRepository.findById(id);
			if(studentResult.isPresent()) {
				String userName = CommonService.getUsername();
				String userRole = CommonService.getUserRole();
				if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
					StudentResult studRes =studentResult.get();
					studRes.setStatus(STATUS.ACTIVE.toString());
					studentResultRepository.save(studRes);
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
	@GetMapping("/studentresult/{id}")
	public ResponseEntity<Object> getStudentResultById(@PathVariable(value = "id") Long studentResultId)
			throws ResourceNotFoundException {
		Optional<StudentResult> student = studentResultRepository.findById(studentResultId);
				if(student.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(student.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Not Found");
	}
	@GetMapping("/studentresult/all/{company}/{pageNumber}")
	public List<StudentResult> getStudentResults(@PathVariable(value = "company") String company ,@PathVariable(value = "pageNumber") int pageNumber) {
		if(pageNumber>0) {
			pageNumber=pageNumber-1;
		}
		Company companyObj = companyRepository.findCompanyByName(company);
		Pageable page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by("id").descending());
		Page<StudentResult> studentLs =	studentResultRepository.findAll(GenericSpecs.getStudentResultByCompanySpec(companyObj,STATUS.ACTIVE.toString()),page);
		return studentLs.getContent();
	}
	@GetMapping("/studentresult/count/{company}")
	public long getStudentResultCount(@PathVariable(value = "company") String company  ) {
		Company companyObj = companyRepository.findCompanyByName(company);
		return	studentResultRepository.count(GenericSpecs.getStudentResultByCompanySpec(companyObj,STATUS.ACTIVE.toString()));
		}
	
	@GetMapping("/studentresult/alldelete/{company}")
	public List<StudentResult> getDeleteStudentResults(@PathVariable(value = "company") String company  ) {
		try {
		Company companyObj = companyRepository.findCompanyByName(company);
		List<StudentResult> studentLs =	studentResultRepository.findAll(GenericSpecs.getStudentResultByCompanySpec(companyObj,STATUS.DELETE.toString()));
		return studentLs;
	}catch (Exception e) {
		// TODO: handle exception
	}
	return null;
	}
	@PostMapping("/searchStudent")
	public List<Student> getSearchStudentResults(@RequestBody StudentSearch searchStudent) {
		StudentSearch srchStudent =CommonUtils.studentDateFixes(searchStudent);
		int pageNumber = 0;
		if(srchStudent.getPageNumber()>0) {
		pageNumber = srchStudent.getPageNumber()-1;
		}
		Company companyObj = companyRepository.findCompanyByName(srchStudent.getCompany());
		Pageable page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by("id").descending());
		Page<Student> studentLs =	studentRepository.findAll(GenericSpecs.getSearchStudentResultByCompanySpec(srchStudent,companyObj.getId()),page);
		return studentLs.getContent();
		
	}
	@PostMapping("/searchStudentCount")
	public long searchStudentCount(@RequestBody StudentSearch studentSearch){
		StudentSearch srchStudent =CommonUtils.studentDateFixes(studentSearch);
		Company companyObj = companyRepository.findCompanyByName(srchStudent.getCompany());
		long count =studentRepository.count(GenericSpecs.getSearchStudentResultByCompanySpec(srchStudent,companyObj.getId()));
		System.out.println(count);
		return count;
	}
	
	@PostMapping("/createstudentcourse")
	public ResponseEntity<String> createStudentCourse(@RequestBody StudentCourse studentCourseDetails) {
		try {
			 studentCourseDetails.setStatus(STATUS.ACTIVE.toString());
			 studentCourseDetails.setCreatedDate(LocalDateTime.now());
			 studentCourseDetails.setModifiedDate(LocalDateTime.now());
			//studentPayment.setPassowrd(encryptPassword(studentPayment.getPassowrd()));
			studentCourseRepository.save( studentCourseDetails);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	@PutMapping("/updatestudentcourse")
	public ResponseEntity<String> updateStudentCourse(@RequestBody StudentCourse studentCourseDetails) throws ResourceNotFoundException {
		try {
			studentCourseRepository.save(studentCourseDetails);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	
	@DeleteMapping("/studentcourse/{id}")
	public ResponseEntity<String> deleteStudentCourse(@PathVariable(value = "id") Long studentCourseId)
			throws ResourceNotFoundException {
		Optional<StudentCourse> studentCourseDetails = studentCourseRepository.findById(studentCourseId);
				if(studentCourseDetails.isPresent()) {
					String userName = CommonService.getUsername();
					String userRole = CommonService.getUserRole();
					if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
						studentCourseRepository.delete(studentCourseDetails.get());
					}else {
						StudentCourse studRes =studentCourseDetails.get();
						studRes.setStatus(STATUS.DELETE.toString());
						studentCourseRepository.save(studRes);
					}
						
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/studentcourse/rejectDeletion/{id}")
	public ResponseEntity<String>  rejectStudentCourse(@PathVariable(value = "id") Long id ) {
			Optional<StudentCourse> studentCourseDetails = studentCourseRepository.findById(id);
			if(studentCourseDetails.isPresent()) {
				String userName = CommonService.getUsername();
				String userRole = CommonService.getUserRole();
				if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
					StudentCourse studRes =studentCourseDetails.get();
					studRes.setStatus(STATUS.ACTIVE.toString());
					studentCourseRepository.save(studRes);
				}
				return new ResponseEntity<>(HttpStatus.OK);
			}else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
	@GetMapping("/studentcourse/{id}")
	public ResponseEntity<Object> getStudentCourseById(@PathVariable(value = "id") Long studentCourseId)
			throws ResourceNotFoundException {
		Optional<StudentCourse> student = studentCourseRepository.findById(studentCourseId);
				if(student.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(student.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Not Found");
	}
	@GetMapping("/studentcourse/all/{company}")
	public List<StudentCourse> getStudentCourses(@PathVariable(value = "company") String company  ) {
		Company companyObj = companyRepository.findCompanyByName(company);
		List<StudentCourse> studentLs =	studentCourseRepository.findAll(GenericSpecs.getStudentCourseByCompanySpec(companyObj,STATUS.ACTIVE.toString()));
		studentLs.forEach(obj->{
			Hibernate.initialize(obj.getStudCourseDetList());
		});
		
		return studentLs;
	}
	
	@GetMapping("/studentcourse/alldelete/{company}")
	public List<StudentCourse> getDeleteStudentCourses(@PathVariable(value = "company") String company  ) {
		try {
		Company companyObj = companyRepository.findCompanyByName(company);
		List<StudentCourse> studentLs =	studentCourseRepository.findAll(GenericSpecs.getStudentCourseByCompanySpec(companyObj,STATUS.DELETE.toString()));
		return studentLs;
	}catch (Exception e) {
		// TODO: handle exception
	}
	return null;
	}

	//student course details below
	@PostMapping("/createstudentcoursedetails")
	public ResponseEntity<String> createStudentCourseDetails(@RequestBody StudentCourseDetails studentCourseDetails) {
		try {
			 studentCourseDetailsRepository.save( studentCourseDetails);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
		
	@DeleteMapping("/studentcoursedetails/{id}")
	public ResponseEntity<String> deleteStudentCourseDetails(@PathVariable(value = "id") Long studentCourseId)
			throws ResourceNotFoundException {
		Optional<StudentCourseDetails> studentCourseDetails = studentCourseDetailsRepository.findById(studentCourseId);
				if(studentCourseDetails.isPresent()) {
						studentCourseDetailsRepository.delete(studentCourseDetails.get());
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	@GetMapping("/studentcoursedetails/all/{studentcourseid}")
	public List<StudentCourseDetails> getStudentCoursesDetails(@PathVariable(value = "company") String company,@PathVariable(value = "studentcourseid") Long studentCourseId    ) {
		
		List<StudentCourseDetails> studentLs =	studentCourseDetailsRepository.findAll(GenericSpecs.getStudentCourseDetailsByCompanySpec(studentCourseId));
		return studentLs;
	}

	//Student Log changes here
	@PostMapping("/createstudentlog")
	public ResponseEntity<String> createStudentLog(@RequestBody StudentLog studentLog) {
		try {
			DecimalFormat format = new DecimalFormat("#.##");
			long minutes = studentLog.getStartTime().until(studentLog.getEndTime(), ChronoUnit.MINUTES);
			double hours = Double.valueOf(format.format(minutes/60.0));
			studentLog.setHours(hours);
			 studentLog.setStatus(STATUS.ACTIVE.toString());
			 studentLog.setCreatedDate(LocalDateTime.now());
			 studentLog.setModifiedDate(LocalDateTime.now());
			//studentPayment.setPassowrd(encryptPassword(studentPayment.getPassowrd()));
			studentLogRepository.save( studentLog);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	@PutMapping("/updatestudentlog")
	public ResponseEntity<String> updateStudentLog(@RequestBody StudentLog studentLog) throws ResourceNotFoundException {
		try {
			DecimalFormat format = new DecimalFormat("#.##");
			long minutes = studentLog.getStartTime().until(studentLog.getEndTime(), ChronoUnit.MINUTES);
			double hours = Double.valueOf(format.format(minutes/60.0));
			studentLog.setHours(hours);
			studentLogRepository.save(studentLog);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	
	@DeleteMapping("/studentlog/{id}")
	public ResponseEntity<String> deleteStudentLog(@PathVariable(value = "id") Long studentLogId)
			throws ResourceNotFoundException {
		Optional<StudentLog> studentLog = studentLogRepository.findById(studentLogId);
				if(studentLog.isPresent()) {
					String userName = CommonService.getUsername();
					String userRole = CommonService.getUserRole();
					if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
						studentLogRepository.delete(studentLog.get());
					}else {
						StudentLog studRes =studentLog.get();
						studRes.setStatus(STATUS.DELETE.toString());
						studentLogRepository.save(studRes);
					}
						
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/studenthourlog/{id}")
	public ResponseEntity<Object> getStudentHourById(@PathVariable(value = "id") Long studentId)
			throws ResourceNotFoundException {
		String hours= studentLogRepository.calculateHours(studentId);
			return ResponseEntity.status(HttpStatus.OK).body(hours);	
	}
	
	@GetMapping("/studentlog/{id}")
	public ResponseEntity<Object> getStudentLogById(@PathVariable(value = "id") Long studentLogId)
			throws ResourceNotFoundException {
		Optional<StudentLog> student = studentLogRepository.findById(studentLogId);
				if(student.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(student.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Not Found");
	}
	@GetMapping("/studentlog/all/{company}")
	public List<StudentLog> getStudentLogs(@PathVariable(value = "company") String company  ) {
		Company companyObj = companyRepository.findCompanyByName(company);
		List<StudentLog> studentLs =	studentLogRepository.findAll(GenericSpecs.getStudentLogByCompanySpec(companyObj,STATUS.ACTIVE.toString()));
		return studentLs;
	}
	
		
}
