package com.cameron.driver.education.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.EmployeeHourLog;
import com.cameron.driver.education.model.Employee_User;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.EmployeeHourLogRepository;
import com.cameron.driver.education.repository.EmployeeRepository;
import com.cameron.driver.education.repository.GenericRepository;
import com.cameron.driver.education.specs.GenericSpecs;

@RestController
@RequestMapping({ "/employee" })
public class EmployeeController {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeHourLogRepository employeeHourLogRepository;
	
	@Autowired
	private GenericRepository genericRepository;
	
	@Autowired
	private CompanyRepository companyRepository;


	@GetMapping(produces = "application/json")
	@RequestMapping({ "/validateLogin" })
	public Employee_User validateLogin() {
		System.out.println("---=====employee list inside validate login=>>");
		Employee_User emp = new Employee_User();
		emp.setUserName("Username Created");
		System.out.println("validated user");
		return emp;
	}


	@GetMapping("/all/{company}")
	public ResponseEntity<List<Employee_User>> getAllEmployees(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepository.findCompanyByName(company);
		List<Employee_User> employeeList=employeeRepository.findAll(GenericSpecs.getEmployeeCompanySpec(companyObj, STATUS.ACTIVE.toString()));
		System.out.println("---=====employee list=>>"+employeeList);
		return ResponseEntity.ok().body(employeeList);
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object> getEmployeeById(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Optional<Employee_User> employee = employeeRepository.findById(employeeId);
				if(employee.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(employee.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Not Found");
	}
	
	@GetMapping("/findByName/{userName}")
	public Employee_User getEmployeeByUsername(@PathVariable(value = "userName") String userName)
			 {
		Employee_User employee = employeeRepository.findEmployeeByName(userName);
		if(employee!=null) {
			return employee;
		}
		return null;
	}

	@PostMapping("/create")
	public ResponseEntity<String> createEmployee(@RequestBody Employee_User employee) {
		try {
			employee.setStatus(STATUS.ACTIVE.toString());
			employee.setPassowrd(encryptPassword(employee.getPassowrd()));
			employee.setCreatedDate(LocalDateTime.now());
			employee.setModifiedDate(LocalDateTime.now());
			employeeRepository.save(employee);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateEmployee(@RequestBody Employee_User employee) throws ResourceNotFoundException {
		try {
			employee.setModifiedDate(LocalDateTime.now());
			employeeRepository.save(employee);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Optional<Employee_User> employee = employeeRepository.findById(employeeId);
				if(employee.isPresent()) {
					employeeRepository.delete(employee.get());	
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/ACTIVATE/{id}")
	public ResponseEntity<String> activateEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Optional<Employee_User> employee = employeeRepository.findById(employeeId);
		if(employee.isPresent()) {
			Employee_User emp=employee.get();
			emp.setStatus(STATUS.ACTIVE.toString());
			employeeRepository.save(emp);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/DEACTIVATE/{id}")
	public ResponseEntity<String> deactivateEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Optional<Employee_User> employee = employeeRepository.findById(employeeId);
		if(employee.isPresent()) {
			Employee_User emp=employee.get();
			emp.setStatus(STATUS.INACTIVE.toString());
			employeeRepository.save(emp);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/countapprovals")
	public ResponseEntity<Integer> countApprovals()
			throws ResourceNotFoundException {
		Integer count=genericRepository.countTotalApprovals();
		return ResponseEntity.ok().body(count);
	}
	
	@GetMapping("/allemployeehourlog/{company}")
	public ResponseEntity<List<EmployeeHourLog>> getAllEmployeeLogs(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepository.findCompanyByName(company);
		return ResponseEntity.ok().body(employeeHourLogRepository.findAll(GenericSpecs.getEmployeeHourLogByCompanySpec(companyObj, STATUS.ACTIVE.toString())));
	}
	@GetMapping("/employeehourlog/{id}")
	public ResponseEntity<Object> getEmployeeLogById(@PathVariable(value = "id") Long employeeLogId)
			throws ResourceNotFoundException {
		Optional<EmployeeHourLog> employeeLog = employeeHourLogRepository.findById(employeeLogId);
				if(employeeLog.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(employeeLog.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee Hour Not Found");
	}
	
	@PostMapping("/createemployeehourlog")
	public ResponseEntity<String> createEmployeeHourLog(@RequestBody EmployeeHourLog employeeHorLog) {
		try {
			employeeHorLog.setStatus(STATUS.ACTIVE.toString());
			employeeHorLog.setCreatedDate(LocalDateTime.now());
			employeeHorLog.setModifiedDate(LocalDateTime.now());
			employeeHourLogRepository.save(employeeHorLog);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}

	@PutMapping("/updateemployeehourlog")
	public ResponseEntity<String> updateEmployeeHorLog(@RequestBody EmployeeHourLog employeeHourLog) throws ResourceNotFoundException {
		try {
			employeeHourLog.setModifiedDate(LocalDateTime.now());
			employeeHourLogRepository.save(employeeHourLog);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	@DeleteMapping("employeehourlog/{id}")
	public ResponseEntity<String> deleteEmployeeHourLog(@PathVariable(value = "id") Long employeeLogId)
			throws ResourceNotFoundException {
		Optional<EmployeeHourLog> employeeHourLog = employeeHourLogRepository.findById(employeeLogId);
				if(employeeHourLog.isPresent()) {
					employeeHourLogRepository.delete(employeeHourLog.get());	
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	private String encryptPassword(String pwd) {
		BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
		return bcryptEncoder.encode(pwd);
	}
	
	
}
