package com.cameron.driver.education.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.Question;
import com.cameron.driver.education.model.Test;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.QuestionRepository;
import com.cameron.driver.education.repository.TestRepository;
import com.cameron.driver.education.specs.GenericSpecs;
@RestController
@RequestMapping("/questions")
public class ExamExportExcel {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;

	@Autowired
	private TestRepository testRepostory;

	
	@PostMapping("/export")
	public ResponseEntity<InputStreamResource> exportQuestions( @RequestBody String company) {
			long ret = 10;
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet("Questions");
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			XSSFFont headerFont= workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontName("Arial");
			headerStyle.setFont(headerFont);
			
			System.out.println("inside fleet log method export---");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			HttpHeaders header = new HttpHeaders();
			//header.add("Content-Disposition", "attachment; filename=exportfleetlog.xlsx");
			header.add("Content-Type", "application/vnd.ms-excel");

			//This data needs to be written (Object[]);
			Map<String, Object[]> data = new HashMap();
			//List<FleetLog> fleetLogList = fleetLogRepository.findLogByStatus(STATUS.ACTIVE.toString());
			Company companyObj = companyRepostory.findCompanyByName(company);
			List<Question> questionList =	questionRepository.findAll(GenericSpecs.getQuestionssByCompanySpec(companyObj));
			
			List<String> lsHeader = new ArrayList<String>(Arrays.asList("Question","Exam", "optionA","optionB", "optionC","optionD","correctAnswer"));
			for(Question question:questionList) {
				data.put(String.valueOf(question.getId()), new Object[] {question.getQuestn(), question.getTest().getName(),question.getOptionA(),question.getOptionB(),question.getOptionC(),question.getOptionD(),question.getCorrectAnswer()});
			}
			Row rowHeader = sheet.createRow(0);
			for (int rn=0; rn<lsHeader.size(); rn++) {
				rowHeader.createCell(rn).setCellValue(lsHeader.get(rn));
				rowHeader.getCell(rn).setCellStyle(headerStyle);
				}
			//Iterate over data and write to sheet
			int rownum = 1;
			double incomeSum=0.0;
			double expenseSum=0.0;
		
			Set<String> keyset = data.keySet();
			for (String key : keyset)
			{
			    Row row = sheet.createRow(rownum++);
			    Object [] objArr = data.get(key);
			    int cellnum = 0;
			    for (Object obj : objArr)
			    {
			       Cell cell = row.createCell(cellnum++);
			       if(obj instanceof String)
			            cell.setCellValue((String)obj);
			        else if(obj instanceof Integer)
			            cell.setCellValue((Integer)obj);
			        else if(obj instanceof Long)
			            cell.setCellValue((Long)obj);
			    }
			}
			Row row = sheet.createRow(rownum++);
				try {
			    workbook.write(out);
			    out.close();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
			return ResponseEntity.ok().headers(header).body(new InputStreamResource(inputStream));
			
	}
	private String formatDouble(double sum) {
		DecimalFormat df = new DecimalFormat("#.##");
		String formatted = df.format(sum);
		return formatted;
	}
	
	@PostMapping("/import")
	 public void importQuestions(@RequestParam("imageFile") MultipartFile file,@RequestParam("company") String company) throws IOException {
		
		XSSFWorkbook workbook = new XSSFWorkbook (file.getInputStream());
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator itr = sheet.rowIterator();
		Question question=null;
		Test test=null;
		System.out.println(company);
		Company companyObj =companyRepostory.findCompanyByName(company);
		while(itr.hasNext()){
			Row row = (Row)itr.next();
			//Iterator<Cell> cite = row.cellIterator();
			//while(cite.hasNext()){
			//	Cell c = cite.next();
				System.out.println("cell 0 "+row.getCell(1));
				Optional<Test> testOptional=testRepostory.findOne(GenericSpecs.checkTestAvailable(row.getCell(1).toString(), companyObj));
				if(testOptional.isPresent()) {
				question = new Question(row.getCell(0).toString(),testOptional.get(),
						row.getCell(2).toString(),
						row.getCell(3).toString(),
						row.getCell(4).toString(),
						row.getCell(5).toString(),
						row.getCell(6).toString());
				Optional<Question> ques = questionRepository.findOne(GenericSpecs.getQuestionsCritieria(question));
				if(!ques.isPresent()) {
					question.setCreatedDate(LocalDateTime.now());
					question.setStatus(STATUS.ACTIVE.toString());
					questionRepository.save(question);
				}else {
					System.out.println("duplicate record found for row "+row.getCell(0));
				}
			}
			//}
			//System.out.println();
		}
		
	}

}
