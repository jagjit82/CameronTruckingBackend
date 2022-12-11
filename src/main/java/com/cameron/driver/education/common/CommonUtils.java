package com.cameron.driver.education.common;

import java.time.LocalDate;

import com.cameron.driver.education.model.ExpenseLogSearch;
import com.cameron.driver.education.model.StudentSearch;

public class CommonUtils {

	public static LocalDate incrementDateByDay(LocalDate localDate) {
		return localDate!=null?localDate.plusDays(1):null;
	}
	
	public static StudentSearch studentDateFixes(StudentSearch studentSearch) {
		studentSearch.setStartDate(incrementDateByDay(studentSearch.getStartDate()));
		studentSearch.setEndDate(incrementDateByDay(studentSearch.getEndDate()));
		return studentSearch;
	}
	
	public static ExpenseLogSearch expenseLogDateFixes(ExpenseLogSearch expenseLogSearch) {
		expenseLogSearch.setExpenseStartDate(incrementDateByDay(expenseLogSearch.getExpenseStartDate()));
		expenseLogSearch.setExpenseEndDate(incrementDateByDay(expenseLogSearch.getExpenseEndDate()));
		return expenseLogSearch;
	}
}
