package com.cameron.driver.education.repository;

import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.ExpenseLogSearch;

public interface ExpenseLogCustomRepository {

	Double sumExpenseLog(ExpenseLogSearch expenseLog,Company company);
	Double sumIncomeLog(ExpenseLogSearch expenseLog,Company company);
	

}
