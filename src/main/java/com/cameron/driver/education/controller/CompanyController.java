package com.cameron.driver.education.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.CompanyDetails;
import com.cameron.driver.education.model.Reminders;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.model.StudentPayment;
import com.cameron.driver.education.model.StudentVO;
import com.cameron.driver.education.repository.CompanyDetailsRepository;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.RemindersRepository;
import com.cameron.driver.education.specs.GenericSpecs;

@RestController
@RequestMapping({ "/company" })
public class CompanyController {
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyDetailsRepository companyDetailsRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	@Autowired
	private RemindersRepository reminderRepostory;

	
	@GetMapping("/all")
	public ResponseEntity<List<Company>> getAllCompanies() {
		List<Company> lsCompany = companyRepository.findAll();
		lsCompany=lsCompany.stream().filter(company->STATUS.ACTIVE.toString().equals(company.getStatus())).collect(Collectors.toList());
		return ResponseEntity.ok().body(lsCompany);
	}
	@GetMapping("/find/{name}")
	public ResponseEntity<Object> getCompanyByName(@PathVariable(value = "name") String name)
			throws ResourceNotFoundException {
		Company company= companyRepository.findCompanyByName(name);
		if(company==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle Not Found");
		}
		return ResponseEntity.ok().body(company);
	}
	
	@GetMapping("/allCompanyDetails/{company}")
	public ResponseEntity<CompanyDetails> getAllCompanyDetails(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		List<CompanyDetails> compdetailsList = companyDetailsRepository.findAll(GenericSpecs.getCompanDetailsByCompanySpec(companyObj));
		CompanyDetails companyDetails=null;
		if(compdetailsList.stream().findFirst().isPresent()) {
			companyDetails=compdetailsList.stream().findFirst().get();
		}
		
		return ResponseEntity.ok().body(companyDetails);
	}
	@GetMapping("/{id}")
	public ResponseEntity<Object> getCompanyDetailsById(@PathVariable(value = "id") Long companyDetailsId)
			throws ResourceNotFoundException {
		Optional<CompanyDetails> companyDetails = companyDetailsRepository.findById(companyDetailsId);
				if(companyDetails.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(companyDetails.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CompanyDetails Not Found");
	}
	
	@PostMapping("/create")
	public ResponseEntity<String> createCompanyDetails(@RequestBody CompanyDetails companyDetails) {
		try {
			companyDetails.setStatus(STATUS.ACTIVE.toString());
			companyDetails.setCreatedDate(LocalDateTime.now());
			companyDetails.setModifiedDate(LocalDateTime.now());
			companyDetailsRepository.save(companyDetails);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateCompanyDetails(@RequestBody CompanyDetails companyDetails) throws ResourceNotFoundException {
		try {
			companyDetails.setModifiedDate(LocalDateTime.now());
			companyDetailsRepository.save(companyDetails);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCompanyDetails(@PathVariable(value = "id") Long companyDetailsId)
			throws ResourceNotFoundException {
		Optional<CompanyDetails> companyDetails = companyDetailsRepository.findById(companyDetailsId);
				if(companyDetails.isPresent()) {
					companyDetailsRepository.delete(companyDetails.get());	
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/allCompanyDetailsReminder/{company}")
	public ResponseEntity<List<CompanyDetails>> getAllCompanyDetailsReminder(@PathVariable(value = "company") String company) {
		List<Reminders> reminderLs= reminderRepostory.findAll();
		if(reminderLs.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Reminders reminder =reminderLs.stream().findFirst().get();
		Company companyObj = companyRepostory.findCompanyByName(company);
		LocalDate businessLicenseExpiry =LocalDate.now().plusDays(reminder.getBusinessLicense());
		LocalDate schoolLicenseExpiry =LocalDate.now().plusDays(reminder.getSchoolLicense());
		LocalDate today=LocalDate.now();
		List<CompanyDetails> listObj = companyDetailsRepository.getCompanyDetailsReminder(companyObj.getId(), today, businessLicenseExpiry, schoolLicenseExpiry);
		return ResponseEntity.ok().body(listObj);
	}
	@GetMapping("/countAllCompanyDetailsReminder/{company}")
	public ResponseEntity<Integer> countAllCompanyDetailsReminder(@PathVariable(value = "company") String company) {
		List<Reminders> reminderLs= reminderRepostory.findAll();
		if(reminderLs.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Reminders reminder =reminderLs.stream().findFirst().get();
		Company companyObj = companyRepostory.findCompanyByName(company);
		LocalDate businessLicenseExpiry =LocalDate.now().plusDays(reminder.getBusinessLicense());
		LocalDate schoolLicenseExpiry =LocalDate.now().plusDays(reminder.getSchoolLicense());
		LocalDate today=LocalDate.now();
		Integer listObj = companyDetailsRepository.countCompanyDetailsReminder(companyObj.getId(), today, businessLicenseExpiry, schoolLicenseExpiry);
		return ResponseEntity.ok().body(listObj);
	}
}
