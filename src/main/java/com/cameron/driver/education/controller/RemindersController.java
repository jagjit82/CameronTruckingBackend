package com.cameron.driver.education.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cameron.driver.education.constant.CommonConstants;
import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.Reminders;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.RemindersRepository;
import com.cameron.driver.education.specs.GenericSpecs;

@RestController
@RequestMapping({ "/reminders" })
public class RemindersController {
	@Autowired
	private RemindersRepository remindersRepository;
	@Autowired
	private CompanyRepository companyRepository;

	@PostMapping("/create")
	public ResponseEntity<String> createReminder(@RequestBody Reminders reminders) {
		try {
			reminders.setStatus(STATUS.ACTIVE.toString());
			reminders.setCreatedDate(LocalDateTime.now());
			reminders.setModifiedDate(LocalDateTime.now());
			remindersRepository.save(reminders);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateReminders(@RequestBody Reminders reminders) throws ResourceNotFoundException {
		try {
			reminders.setModifiedDate(LocalDateTime.now());
			remindersRepository.save(reminders);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/all/{company}")
	public ResponseEntity<Reminders> getAllReminders(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepository.findCompanyByName(company);
		List<Reminders> ls = remindersRepository.findAll(GenericSpecs.getReminderByCompanySpec(companyObj));
		Reminders reminders=null;
		if(ls.stream().findFirst().isPresent()) {
			reminders=ls.stream().findFirst().get();
		}
		return ResponseEntity.ok().body(reminders);
	}

	
	
}
