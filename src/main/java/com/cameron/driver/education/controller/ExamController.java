package com.cameron.driver.education.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cameron.driver.education.constant.CommonConstants;
import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.Question;
import com.cameron.driver.education.model.Test;
import com.cameron.driver.education.model.TestResult;
import com.cameron.driver.education.model.TestResultDetail;
import com.cameron.driver.education.model.TestResultDetailVO;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.QuestionRepository;
import com.cameron.driver.education.repository.TestRepository;
import com.cameron.driver.education.repository.TestResultDetailRepository;
import com.cameron.driver.education.repository.TestResultRepository;
import com.cameron.driver.education.specs.GenericSpecs;

@RestController
@RequestMapping("/exam")
public class ExamController {
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private TestResultRepository testResultRepository;
	
	@Autowired
	private TestResultDetailRepository testResultDetailRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private CompanyRepository companyRepostory;
	
	@GetMapping("/all/{company}")
	public List<Test> getAllTests(
			@PathVariable(value = "company") String company) {

		Company companyObj = companyRepostory.findCompanyByName(company);
		List<Test> testList = testRepository.findAll(GenericSpecs.getTestsByCompanySpec(companyObj));
		return testList;
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getTestById(@PathVariable(value = "id") Long testId)
			throws ResourceNotFoundException {
		Optional<Test> test = testRepository.findById(testId);
		if (test.isPresent()) {
			return ResponseEntity.ok(test.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test Not Found");
	}

	
	@PostMapping("/create")
	public ResponseEntity<String> createTest(@RequestBody Test test) {
		try {
			test.setCreatedDate(LocalDateTime.now());
			test.setModifiedDate(LocalDateTime.now());
			test.setStatus(STATUS.ACTIVE.toString());
			testRepository.save(test);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateTest(@RequestBody Test test) throws ResourceNotFoundException {
		try {
			test.setModifiedDate(LocalDateTime.now());
			testRepository.save(test);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<String> deleteTest(@PathVariable(value = "id") Long testId)
			throws ResourceNotFoundException {
		try {
			Optional<Test> test = testRepository.findById(testId);
			if (test.isPresent()) {
				testRepository.delete(test.get());
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			return new ResponseEntity<>(HttpStatus.OK);
		
	}
	//Question starts here
	@GetMapping("/allQuestion/{company}")
	public List<Question> getAllQuestions(
			@PathVariable(value = "company") String company) {

		Company companyObj = companyRepostory.findCompanyByName(company);
		List<Question> questionList = questionRepository.findAll(GenericSpecs.getQuestionssByCompanySpec(companyObj));
		return questionList;
	}

	@GetMapping("/testResultDetail/{company}")
	public List<TestResultDetailVO> getTestResults(
			@PathVariable(value = "company") String company) {

		Company companyObj = companyRepostory.findCompanyByName(company);
		List<TestResultDetailVO> testresultVOList=null;
		List<Object[]> testResultDetailList = testResultDetailRepository.getTestResult(companyObj.getId());
		if(!CollectionUtils.isEmpty(testResultDetailList)) {
			testresultVOList=new ArrayList<TestResultDetailVO>();
			for(Object[] objArr:testResultDetailList) {
				TestResultDetailVO testResVo = new TestResultDetailVO();
				testResVo.setTestResultDetail((TestResultDetail)objArr[0]);
				testResVo.setCorrectAnswers(Long.valueOf(objArr[1].toString()));
				testresultVOList.add(testResVo);
			}
		}
		return testresultVOList;
	}
	
	@GetMapping("/countQues/{company}")
	public ResponseEntity<Long> countAllQues(@PathVariable(value = "company") String company ) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		return ResponseEntity.ok().body(questionRepository.count(GenericSpecs.getQuestionssByCompanySpec(companyObj)));
	}
	
	@GetMapping("/allQuestions/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<Question> getAllQuestion(
			@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {
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
		Page<Question> questionList = questionRepository.findAll(GenericSpecs.getQuestionssByCompanySpec(companyObj),page);
		return questionList.getContent();
	}

	@GetMapping("/allTestQuestion/{test}")
	public List<Question> getAllTestQuestions(
			@PathVariable(value = "test") String test) {
		List<Question> questionList = questionRepository.findAll(GenericSpecs.getQuestionsByTest(test));
		return questionList;
	}
	@GetMapping("/testQuestionCount/{test}")
	public Long getTestQuestionCount(
			@PathVariable(value = "test") String test) {
		long questionCount = questionRepository.count(GenericSpecs.getQuestionsByTest(test));
		return questionCount;
	}
	
	@GetMapping("question/{id}")
	public ResponseEntity<Object> getQuestionById(@PathVariable(value = "id") Long questionId)
			throws ResourceNotFoundException {
		Optional<Question> test = questionRepository.findById(questionId);
		if (test.isPresent()) {
			return ResponseEntity.ok(test.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question Not Found");
	}

	
	@PostMapping("/createQuestion")
	public ResponseEntity<String> createQuestion(@RequestBody Question question) {
		try {
			question.setCreatedDate(LocalDateTime.now());
			question.setStatus(STATUS.ACTIVE.toString());
			questionRepository.save(question);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/questionUpdate")
	public ResponseEntity<String> updateQuestion(@RequestBody Question question) throws ResourceNotFoundException {
		try {
			questionRepository.save(question);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/question/{id}")
	@Transactional
	public ResponseEntity<String> deleteQuestion(@PathVariable(value = "id") Long questionId)
			throws ResourceNotFoundException {
		try {
			Optional<Question> question = questionRepository.findById(questionId);
			if (question.isPresent()) {
				questionRepository.delete(question.get());
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	//Test Result starts here
	@GetMapping("/alltestresult/{company}")
	public List<Test> getAllTestResults(
			@PathVariable(value = "company") String company) {

		Company companyObj = companyRepostory.findCompanyByName(company);
		List<Test> testList = testRepository.findAll(GenericSpecs.getTestsByCompanySpec(companyObj));
		return testList;
	}

		
	@DeleteMapping("/testresult/{id}")
	@Transactional
	public ResponseEntity<String> deleteTestResult(@PathVariable(value = "id") Long testId)
			throws ResourceNotFoundException {
		try {
			Optional<TestResult> testRes = testResultRepository.findById(testId);
			if (testRes.isPresent()) {
				testResultRepository.delete(testRes.get());
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			return new ResponseEntity<>(HttpStatus.OK);
		
	}
	@GetMapping("/testresult/{id}")
	public ResponseEntity<Object> getTestResultById(@PathVariable(value = "id") Long testResId)
			throws ResourceNotFoundException {
		Optional<TestResult> testRes = testResultRepository.findById(testResId);
		if (testRes.isPresent()) {
			return ResponseEntity.ok(testRes.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test Result Not Found");
	}
	

	@GetMapping("/testresultbymailtest/{mail}/{test}")
	public ResponseEntity<Object> getTestResultByMailTest(@PathVariable(value = "mail") String mail,@PathVariable(value = "test") String test)
			throws ResourceNotFoundException {
		Optional<TestResult> testRes = testResultRepository.findOne(GenericSpecs.getTestResulysByMailTest(mail,test));
		if (testRes.isPresent()) {
			return ResponseEntity.ok(testRes.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test Result Not Found");
	}

	
	@PostMapping("/testresultcreate")
	public ResponseEntity<Object> createTestResult(@RequestBody TestResult testResult) {
		try {
			testResult.setCreatedDate(LocalDateTime.now());
			testResult.setModifiedDate(LocalDateTime.now());
			testResult.setStatus(STATUS.INCOMPLETE.toString());
			TestResult testRes = testResultRepository.save(testResult);
			return ResponseEntity.ok(testRes);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("testresultUpdate")
	public ResponseEntity<String> updateTestResult(@RequestBody TestResult testResult) throws ResourceNotFoundException {
		try {
			testResultRepository.save(testResult);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	//end test result here
	
	//Test Result Detail starts here
	
	@PostMapping("testresultdetailcreate")
	public ResponseEntity<String> createTestResultDetail(@RequestBody TestResultDetail testResultDetail) {
		try {
			testResultDetail.setCreatedDate(LocalDateTime.now());
			testResultDetail.setModifiedDate(LocalDateTime.now());
			testResultDetailRepository.save(testResultDetail);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("testresultdetailupdate")
	public ResponseEntity<String> updateTestResultDetail(@RequestBody TestResultDetail testResultDetail) throws ResourceNotFoundException {
		try {
			testResultDetailRepository.save(testResultDetail);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/testresultdetail/{id}")
	public List<TestResultDetail> getTestResultDetailById(@PathVariable(value = "id") Long testResId)
			throws ResourceNotFoundException {
		List<TestResultDetail> testResDetLs = testResultDetailRepository.findAll(GenericSpecs.getTestResultDetailByCompanySpec(testResId));
		return testResDetLs;
	}
	
	
	//end test result here
	}
