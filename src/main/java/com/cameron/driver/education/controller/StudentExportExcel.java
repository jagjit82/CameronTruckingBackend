package com.cameron.driver.education.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.StudentRepository;

@RestController
@RequestMapping("/studentExpImp")
public class StudentExportExcel {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CompanyRepository companyRepostory;


	
	@PostMapping("/import")
	public String importStudents(@RequestParam("imageFile") MultipartFile file, @RequestParam("company") String company)
			throws IOException {
		Map<String, Integer> headerMap = new HashMap<>();
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator itr = sheet.rowIterator();
		Student student = null;
		System.out.println(company);
		Company companyObj = companyRepostory.findCompanyByName(company);
		Row headerRow = (Row)itr.next();
		headerMap=populateHeaderMap(headerRow); 
		
		while (itr.hasNext()) {
			Row row = (Row) itr.next();
			// Iterator<Cell> cite = row.cellIterator();
			// while(cite.hasNext()){
			// Cell c = cite.next();
			System.out.println("cell 0 " + row.getCell(1));
			// Optional<Test>
			// testOptional=testRepostory.findOne(GenericSpecs.checkTestAvailable(row.getCell(1).toString(),
			// companyObj));
			// if(testOptional.isPresent()) {
			student = instantiateStudent(headerMap,row);
				student.setCreatedDate(LocalDateTime.now());
				student.setStatus(STATUS.ACTIVE.toString());
				student.setCompany(companyObj);
				student.setStartTime(LocalTime.now());
				student.setEndTime(LocalTime.now());
				student.setIssueDate(LocalDate.now());
				student.setRegistrationDate(LocalDate.now());
				student.setLicenseExpiry(LocalDate.now());
				studentRepository.save(student);
			}
		
		return "Imported Successfully!!";
		}
	

	private Student instantiateStudent(Map<String,Integer> map,Row row) {
		Student student = new Student();
		student.setAddress(row.getCell(((Integer)map.get("Address:")).intValue()).toString());
		student.setCity(row.getCell(((Integer)map.get("City:")).intValue()).toString());
		student.setCourse(row.getCell(((Integer)map.get("Course:")).intValue()).toString());
		student.setDateOfBirth(formateDate(row.getCell(((Integer)map.get("Date of Birth:")).intValue()).toString()));
		student.setDiscount(0);
		student.setEmergencyContact(row.getCell(((Integer)map.get("Phone:")).intValue()).toString());
		student.setPhoneNo(row.getCell(((Integer)map.get("Phone:")).intValue()).toString());
		student.setFirstName(row.getCell(((Integer)map.get("First Name:")).intValue()).toString());
		student.setLastName(row.getCell(((Integer)map.get("Name:")).intValue()).toString());
		student.setLicense(row.getCell(((Integer)map.get("Driver Lic. No:")).intValue()).toString());
		student.setPostalCode(row.getCell(((Integer)map.get("Postal Code:")).intValue()).toString());
		int cellIndex =map.get("Amount Due:");
		Double val =Double.valueOf(row.getCell(cellIndex).toString());
		student.setTotalAmount(val.longValue());
		student.setRegistrationDate(LocalDate.now());
		return student;
	
	}

	private Map<String,Integer> populateHeaderMap(Row row) {
		Map updatedMap = new HashMap<String, Integer>();
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("Address:", 0);
		map.put("Advertising:", 0);
		map.put("Amount Due:", 0);
		map.put("City:", 0);
		map.put("Class of Lic.:", 0);
		map.put("Company Name:", 0);
		map.put("Course:", 0);
		map.put("Date of Birth:", 0);
		map.put("Driver Lic. No:", 0);
		map.put("First Name:", 0);
		map.put("Name:", 0);
		map.put("Phone:", 0);
		map.put("Postal Code:", 0);
		map.put("Province:", 0);
		map.put("Total Paid:", 0);

		int maxCellNo=row.getLastCellNum();
		map.forEach((key,value)->{
			for(int i =0;i<maxCellNo;i++) {
				if(key.toString().equalsIgnoreCase(row.getCell(i).toString())) {
					updatedMap.put(key, i);
				}
			}
		});
		return updatedMap;
	}
	
	private LocalDate formateDate(String date) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		 LocalDate localDate = LocalDate.parse(date, formatter);
		 return localDate;
	}
}
