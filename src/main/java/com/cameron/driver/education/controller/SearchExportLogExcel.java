package com.cameron.driver.education.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cameron.driver.education.commonservice.CommonService;
import com.cameron.driver.education.constant.ACCOUNTTYPE;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.ExpenseLog;
import com.cameron.driver.education.model.ExpenseLogSearch;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.ExpenseLogRepository;
import com.cameron.driver.education.specs.GenericSpecs;
@RestController
@RequestMapping("/export")
public class SearchExportLogExcel {
	
	@Autowired
	private ExpenseLogRepository expenseLogRepository;
	
	@Autowired
	private CompanyRepository companyRepostory;


	
	@PostMapping("/expenseLog")
	public ResponseEntity<InputStreamResource> exportExpenseLog( @RequestBody ExpenseLogSearch expenseLog) {
			long ret = 10;
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFSheet sheet = workbook.createSheet("Expenses");
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			XSSFFont headerFont= workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontName("Arial");
			headerStyle.setFont(headerFont);
			
			XSSFCellStyle unpaidStyle = workbook.createCellStyle();
			XSSFFont unpaidFont= workbook.createFont();
			unpaidFont.setBold(true);
			unpaidFont.setColor(IndexedColors.RED.index);
			unpaidStyle.setFont(unpaidFont);
			
			
			System.out.println("inside fleet log method export---");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			HttpHeaders header = new HttpHeaders();
			//header.add("Content-Disposition", "attachment; filename=exportfleetlog.xlsx");
			header.add("Content-Type", "application/vnd.ms-excel");

			//This data needs to be written (Object[]);
			Map<String, Object[]> data = new HashMap();
			//List<FleetLog> fleetLogList = fleetLogRepository.findLogByStatus(STATUS.ACTIVE.toString());
			Company companyObj = companyRepostory.findCompanyByName(expenseLog.getCompany());
			List<ExpenseLog> expenseLogList =	expenseLogRepository.findAll(GenericSpecs.searchExpenseLogSpec(expenseLog,companyObj));
			
			List<String> lsHeader = new ArrayList<String>(Arrays.asList("Expense Name","Account Type", "Expense Amount($)","Income Amount($)", "Date","Status","Mode","Description"));
			for(ExpenseLog expenseLogObj:expenseLogList ) {
				data.put(String.valueOf(expenseLogObj.getId()), new Object[] {expenseLogObj.getExpense().getExpenseName(), expenseLogObj.getAccountType(),expenseLogObj.getExpenseAmount(),expenseLogObj.getExpenseDate(),expenseLogObj.getStatus(),expenseLogObj.getPaymentMode(),expenseLogObj.getDescription()});
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
			       if(((String)objArr[4]).equalsIgnoreCase("NOT PAID")){
			    	   cell.setCellStyle(unpaidStyle);
				    }
			       if(obj instanceof String)
			            cell.setCellValue((String)obj);
			        else if(obj instanceof Integer)
			            cell.setCellValue((Integer)obj);
			        else if(obj instanceof Long)
			            cell.setCellValue((Long)obj);
			        else if(obj instanceof Double) {
			        	if(((String)objArr[1]).equalsIgnoreCase(ACCOUNTTYPE.INCOME.toString())) {
			        		cell = row.createCell(cellnum++);
			        		cell.setCellValue("$"+(Double)obj);
			        		incomeSum+=(Double)obj;
			        	}else  if(((String)objArr[1]).equalsIgnoreCase(ACCOUNTTYPE.EXPENSE.toString())) {
			        		cell.setCellValue("$"+(Double)obj);
			        		cell = row.createCell(cellnum++);
			        		expenseSum+=(Double)obj;
			        	}
			       		
			        }
			        else if(obj instanceof LocalDate) {
			        	Date dateUtil= Date.from(((LocalDate)obj).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			        	cell.setCellValue(CommonService.formatDate(dateUtil));
			        }
			        else if(obj instanceof LocalDateTime) {
			        	Date dateUtil= Date.from(((LocalDateTime)obj).atZone(ZoneId.systemDefault()).toInstant());
			        	//System.out.println(CommonService.formatDate(dateUtil)+"  ---88888");
			        	cell.setCellValue(CommonService.formatDateTime(dateUtil));
			        }
			            
			    }
			}
			Row row = sheet.createRow(rownum++);
			Cell cell2 = row.createCell(2);
			Cell cell3 = row.createCell(3);
			row.getCell(2).setCellStyle(headerStyle);
			cell2.setCellValue("$"+formatDouble(expenseSum));
			row.getCell(3).setCellStyle(headerStyle);
			cell3.setCellValue("$"+formatDouble(incomeSum));
			
			
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
	
	@PostMapping("/yearExpenseLog")
	public ResponseEntity<InputStreamResource> exportYearlyExpenseLog(@RequestBody ExpenseLogSearch expenseLogSearch) {
		Company companyObj = companyRepostory.findCompanyByName(expenseLogSearch.getCompany());
			List<Integer> monthList=Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12);
			XSSFWorkbook workbook = new XSSFWorkbook(); 
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			XSSFFont headerFont= workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontName("Arial");
			headerStyle.setFont(headerFont);
			
			XSSFCellStyle unpaidStyle = workbook.createCellStyle();
			XSSFFont unpaidFont= workbook.createFont();
			unpaidFont.setBold(true);
			unpaidFont.setColor(IndexedColors.RED.getIndex());
			unpaidStyle.setFont(unpaidFont);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			HttpHeaders header = new HttpHeaders();
			for(Integer month:monthList) {
				ExpenseLogSearch expenseLog = new ExpenseLogSearch();
				LocalDate startDate = LocalDate.of(LocalDate.now().getYear(),month,01);
				expenseLog.setExpenseStartDate(startDate);
				expenseLog.setExpenseEndDate(LocalDate.of(LocalDate.now().getYear(),month,month).with(TemporalAdjusters.lastDayOfMonth()));
				List<ExpenseLog> expenseLogList =	expenseLogRepository.findAll(GenericSpecs.searchExpenseLogSpec(expenseLog,companyObj));
				long ret = 10;
				if(!CollectionUtils.isEmpty(expenseLogList)) {
					XSSFSheet sheet = workbook.createSheet(startDate.getMonth().name()+" "+startDate.getYear());
					System.out.println("inside fleet log method export---");
					//header.add("Content-Disposition", "attachment; filename=exportfleetlog.xlsx");
					header.add("Content-Type", "application/vnd.ms-excel");
		
					//This data needs to be written (Object[]);
					Map<String, Object[]> data = new HashMap();
					//List<FleetLog> fleetLogList = fleetLogRepository.findLogByStatus(STATUS.ACTIVE.toString());
					
					List<String> lsHeader = new ArrayList<String>(Arrays.asList("Expense Name","Account Type", "Expense Amount($)","Income Amount($)", "Date","Status","Mode","Description"));
					for(ExpenseLog expenseLogObj:expenseLogList ) {
						data.put(String.valueOf(expenseLogObj.getId()), new Object[] {expenseLogObj.getExpense().getExpenseName(), expenseLogObj.getAccountType(),expenseLogObj.getExpenseAmount(),expenseLogObj.getExpenseDate(),expenseLogObj.getStatus(),expenseLogObj.getPaymentMode(),expenseLogObj.getDescription()});
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
					    	if(((String)objArr[4]).equalsIgnoreCase("NOT PAID")){
					    		cell.setCellStyle(unpaidStyle);
					    	}
					       if(obj instanceof String)
					            cell.setCellValue((String)obj);
					        else if(obj instanceof Integer)
					            cell.setCellValue((Integer)obj);
					        else if(obj instanceof Long)
					            cell.setCellValue((Long)obj);
					        else if(obj instanceof Double) {
					        	if(((String)objArr[1]).equalsIgnoreCase(ACCOUNTTYPE.INCOME.toString())) {
					        		cell = row.createCell(cellnum++);
					        		cell.setCellValue("$"+(Double)obj);
					        		incomeSum+=(Double)obj;
					        	}else  if(((String)objArr[1]).equalsIgnoreCase(ACCOUNTTYPE.EXPENSE.toString())) {
					        		cell.setCellValue("$"+(Double)obj);
					        		cell = row.createCell(cellnum++);
					        		expenseSum+=(Double)obj;
					        	}
					       		
					        }
					        else if(obj instanceof LocalDate) {
					        	Date dateUtil= Date.from(((LocalDate)obj).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
					        	cell.setCellValue(CommonService.formatDate(dateUtil));
					        }
					        else if(obj instanceof LocalDateTime) {
					        	Date dateUtil= Date.from(((LocalDateTime)obj).atZone(ZoneId.systemDefault()).toInstant());
					        	//System.out.println(CommonService.formatDate(dateUtil)+"  ---88888");
					        	cell.setCellValue(CommonService.formatDateTime(dateUtil));
					        }
					            
					    }
					}
					Row row = sheet.createRow(rownum++);
					Cell cell2 = row.createCell(2);
					Cell cell3 = row.createCell(3);
					row.getCell(2).setCellStyle(headerStyle);
					cell2.setCellValue("$"+formatDouble(expenseSum));
					row.getCell(3).setCellStyle(headerStyle);
					cell3.setCellValue("$"+formatDouble(incomeSum));
					
				 }
				}
				try {
			    workbook.write(out);
			    out.close();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
			return ResponseEntity.ok().headers(header).body(new InputStreamResource(inputStream));
			
	}

}
