package com.cameron.driver.education.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

import com.cameron.driver.education.common.CommonUtils;
import com.cameron.driver.education.constant.CommonConstants;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.Expense;
import com.cameron.driver.education.model.ExpenseLog;
import com.cameron.driver.education.model.ExpenseLogSearch;
import com.cameron.driver.education.model.StudentSearch;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.ExpenseLogCustomRepository;
import com.cameron.driver.education.repository.ExpenseLogRepository;
import com.cameron.driver.education.repository.ExpenseRepository;
import com.cameron.driver.education.specs.GenericSpecs;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private ExpenseLogRepository expenseLogRepository;
	
	@Autowired
	private ExpenseLogCustomRepository expenseLogCustomRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	
	@GetMapping("/all")
	public ResponseEntity<List<Expense>> getAllExpenses() {
		return ResponseEntity.ok().body(expenseRepository.findAll());
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getExpenseById(@PathVariable(value = "id") Long expenseId)
			throws ResourceNotFoundException {
		Optional<Expense> expense = expenseRepository.findById(expenseId);
		if (expense.isPresent()) {
			return ResponseEntity.ok(expense.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense Not Found");
	}

	@PostMapping("/create")
	public ResponseEntity<String> createExpense(@RequestBody Expense expense) {
		try {
			expenseRepository.save(expense);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateTruck(@RequestBody Expense expense) throws ResourceNotFoundException {
		try {
			expenseRepository.save(expense);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteExpense(@PathVariable(value = "id") Long expenseId)
			throws ResourceNotFoundException {
		Optional<Expense> expense = expenseRepository.findById(expenseId);
		if (expense.isPresent()) {
				expenseRepository.delete(expense.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	/* Expense Log start here */
	@PostMapping("log/search")
	public List<ExpenseLog> searchExpenseLog(@RequestBody ExpenseLogSearch expenseLog){
		ExpenseLogSearch expenseLogSearch=CommonUtils.expenseLogDateFixes(expenseLog);
		int pageNumber = expenseLogSearch.getPageNumber();
		if(pageNumber>0) {
			pageNumber=pageNumber-1;
		}
		Company companyObj = companyRepostory.findCompanyByName(expenseLogSearch.getCompany());
		Pageable page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by("id").descending());
		Page<ExpenseLog> expenseLogList =	expenseLogRepository.findAll(GenericSpecs.searchExpenseLogSpec(expenseLogSearch,companyObj),page);
		return expenseLogList.getContent();
	}
	
	@PostMapping("log/count")
	public long searchExpenseLogCount(@RequestBody ExpenseLogSearch expenseLog){
		ExpenseLogSearch expenseLogSearch=CommonUtils.expenseLogDateFixes(expenseLog);
		Company companyObj = companyRepostory.findCompanyByName(expenseLogSearch.getCompany());
		return expenseLogRepository.count(GenericSpecs.searchExpenseLogSpec(expenseLogSearch,companyObj));
	}
	
	@PostMapping("log/expensesum")
	public String sumExpenseLog(@RequestBody ExpenseLogSearch expenseLog){
		Company companyObj = companyRepostory.findCompanyByName(expenseLog.getCompany());
		Double val =expenseLogCustomRepository.sumExpenseLog(expenseLog, companyObj);
		if(val==null) {
			return null;
		}
		DecimalFormat df = new DecimalFormat("#.##");
		String formatted = df.format(val); 
		return formatted;
	}
	@PostMapping("log/incomesum")
	public String sumIncomeLog(@RequestBody ExpenseLogSearch expenseLog){
		Company companyObj = companyRepostory.findCompanyByName(expenseLog.getCompany());
		Double val =expenseLogCustomRepository.sumIncomeLog(expenseLog, companyObj);
		if(val==null) {
			return null;
		}
		DecimalFormat df = new DecimalFormat("#.##");
		String formatted = df.format(val); 
		return formatted;
	}
	
	@GetMapping("log/all")
	public ResponseEntity<List<ExpenseLog>> getAllExpenseLogs() {
		return ResponseEntity.ok().body(expenseLogRepository.findAll());
	}
	
	@GetMapping("log/{id}")
	public ResponseEntity<Object> getExpenseLogById(@PathVariable(value = "id") Long expenseLogId)
			throws ResourceNotFoundException {
		Optional<ExpenseLog> expenseLog = expenseLogRepository.findById(expenseLogId);
		if (expenseLog.isPresent()) {
			return ResponseEntity.ok(expenseLog.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense Not Found");
	}

	@PostMapping("log/create")
	public ResponseEntity<String> createExpenseLog(@RequestBody ExpenseLog expenseLog) {
		try {
			expenseLog.setCreatedDate(LocalDateTime.now());
			expenseLog.setExpenseDate(expenseLog.getExpenseDate().plusDays(1));
			expenseLogRepository.save(expenseLog);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("log/update")
	public ResponseEntity<String> updateExpenseLog(@RequestBody ExpenseLog expenseLog) throws ResourceNotFoundException {
		try {
			expenseLogRepository.save(expenseLog);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


	@DeleteMapping("log/{id}")
	public ResponseEntity<String> deleteExpenseLog(@PathVariable(value = "id") Long expenseLogId)
			throws ResourceNotFoundException {
		Optional<ExpenseLog> expenseLog = expenseLogRepository.findById(expenseLogId);
		if (expenseLog.isPresent()) {
				expenseLogRepository.delete(expenseLog.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/* Expense Log ends here */

}
