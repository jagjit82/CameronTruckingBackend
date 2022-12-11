package com.cameron.driver.education.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cameron.driver.education.constant.ACCOUNTTYPE;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.ExpenseLog;
import com.cameron.driver.education.model.ExpenseLogSearch;
@Transactional
@Repository
public class ExpenseLogRepositoryImpl implements ExpenseLogCustomRepository {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Double sumExpenseLog(ExpenseLogSearch expenseLog,Company company) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	    CriteriaQuery<Double> cq = criteriaBuilder.createQuery(Double.class);
	    Root<ExpenseLog> root = cq.from(ExpenseLog.class);
	    // Constructing list of parameters
	    List<Predicate> predicates = new ArrayList<Predicate>();

	    List<Predicate> predList = new ArrayList<Predicate>();
    	
        if(expenseLog.getPaymentMode()!=null) {
        	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("paymentMode"), expenseLog.getPaymentMode()));
        	
        }
        if(expenseLog.getExpense()!=null) {
        	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("expense").get("id"), expenseLog.getExpense().getId()));
        	
        }
        if(expenseLog.getExpenseStartDate()!=null) {
        	predList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expenseDate"), expenseLog.getExpenseStartDate().plusDays(1)));
        }
        if(expenseLog.getExpenseEndDate()!=null) {
        	predList.add(criteriaBuilder.lessThanOrEqualTo(root.get("expenseDate"), expenseLog.getExpenseEndDate().plusDays(1)));
        }
        if(expenseLog.getStatus()!=null) {
        	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("status"), expenseLog.getStatus()));
        }
       	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("company").get("id"), company.getId()));
       	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("accountType"), ACCOUNTTYPE.EXPENSE.toString()));
	    cq.select(criteriaBuilder.sum(root.<Double>get("expenseAmount")))
	            .where(predList.toArray(new Predicate[]{}));
	
	    javax.persistence.Query query = em.createQuery(cq);
		return (Double)query.getSingleResult();
		
	}

	@Override
	public Double sumIncomeLog(ExpenseLogSearch expenseLog, Company company) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	    CriteriaQuery<Double> cq = criteriaBuilder.createQuery(Double.class);
	    Root<ExpenseLog> root = cq.from(ExpenseLog.class);
	    // Constructing list of parameters
	    List<Predicate> predicates = new ArrayList<Predicate>();

	    List<Predicate> predList = new ArrayList<Predicate>();
    	
        if(expenseLog.getPaymentMode()!=null) {
        	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("paymentMode"), expenseLog.getPaymentMode()));
        	
        }
        if(expenseLog.getExpense()!=null) {
        	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("expense").get("id"), expenseLog.getExpense().getId()));
        	
        }
        if(expenseLog.getExpenseStartDate()!=null) {
        	predList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expenseDate"), expenseLog.getExpenseStartDate().plusDays(1)));
        }
        if(expenseLog.getExpenseEndDate()!=null) {
        	predList.add(criteriaBuilder.lessThanOrEqualTo(root.get("expenseDate"), expenseLog.getExpenseEndDate().plusDays(1)));
        }
        if(expenseLog.getStatus()!=null) {
        	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("status"), expenseLog.getStatus()));
        }
       	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("company").get("id"), company.getId()));
       	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("accountType"), ACCOUNTTYPE.INCOME.toString()));
     	
	    cq.select(criteriaBuilder.sum(root.<Double>get("expenseAmount")))
	            .where(predList.toArray(new Predicate[]{}));
	
	    javax.persistence.Query query = em.createQuery(cq);
		return (Double)query.getSingleResult();
		

	}

	
		
	
}
