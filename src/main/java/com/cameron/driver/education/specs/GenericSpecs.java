package com.cameron.driver.education.specs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.CompanyDetails;
import com.cameron.driver.education.model.EmployeeHourLog;
import com.cameron.driver.education.model.Employee_User;
import com.cameron.driver.education.model.ExpenseLog;
import com.cameron.driver.education.model.ExpenseLogSearch;
import com.cameron.driver.education.model.FollowUpPhone;
import com.cameron.driver.education.model.Message;
import com.cameron.driver.education.model.PhoneLog;
import com.cameron.driver.education.model.PhoneLogSearch;
import com.cameron.driver.education.model.Question;
import com.cameron.driver.education.model.Reminders;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.model.StudentCourse;
import com.cameron.driver.education.model.StudentCourseDetails;
import com.cameron.driver.education.model.StudentLog;
import com.cameron.driver.education.model.StudentPayment;
import com.cameron.driver.education.model.StudentResult;
import com.cameron.driver.education.model.StudentSearch;
import com.cameron.driver.education.model.Test;
import com.cameron.driver.education.model.TestResult;
import com.cameron.driver.education.model.TestResultDetail;

public class GenericSpecs {
	public static Specification<PhoneLog> getPhoneLogByCompanySpec(Company company) {
		return new Specification<PhoneLog>() {
            @Override
            public Predicate toPredicate(Root<PhoneLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate phoneLog = criteriaBuilder.equal(root.<PhoneLog> get("company").get("id"), company.getId());
           	Predicate phoneLog1 = criteriaBuilder.equal(root.<PhoneLog> get("status"), STATUS.OPEN.toString());
           	Predicate phoneLog2 = criteriaBuilder.equal(root.<PhoneLog> get("status"), STATUS.CLOSED.toString());
    		return criteriaBuilder.and(phoneLog,criteriaBuilder.or(phoneLog1,phoneLog2));
            }
        };
    }
	public static Specification<PhoneLog> searchPhoneLogSpec(PhoneLogSearch phoneLog,Company company) {
		return new Specification<PhoneLog>() {
            @Override
            public Predicate toPredicate(Root<PhoneLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
            List<Predicate> predList = new ArrayList<Predicate>();
    	
            if(phoneLog.getCallerType()!=null) {
            	predList.add(criteriaBuilder.equal(root.<PhoneLog> get("callerType"), phoneLog.getCallerType()));
            	
            }
            if(phoneLog.getName()!=null) {
            	predList.add(criteriaBuilder.like(root.get("name"), "%"+phoneLog.getName()+"%"));
            }
            if(phoneLog.getPhoneNo()!=null) {
            	predList.add(criteriaBuilder.like(root.get("phoneNo"), "%"+phoneLog.getPhoneNo()+"%"));
            }
            if(phoneLog.getCallStartDate()!=null) {
            	predList.add(criteriaBuilder.greaterThan(root.get("callDate"), phoneLog.getCallStartDate()));
            }
            if(phoneLog.getCallEndDate()!=null) {
            	predList.add(criteriaBuilder.lessThan(root.get("callDate"), phoneLog.getCallEndDate()));
            }
            if(phoneLog.getStatus()!=null) {
            	predList.add(criteriaBuilder.equal(root.<PhoneLog> get("status"), phoneLog.getStatus()));
            }
           	predList.add(criteriaBuilder.equal(root.<PhoneLog> get("company").get("id"), company.getId()));
         	return criteriaBuilder.and(predList.toArray(new Predicate[predList.size()]));
            }
        };
    }
	
	public static Specification<ExpenseLog> searchExpenseLogSpec(ExpenseLogSearch expenseLog,Company company) {
		return new Specification<ExpenseLog>() {
            @Override
            public Predicate toPredicate(Root<ExpenseLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
            List<Predicate> predList = new ArrayList<Predicate>();
    	
            if(expenseLog.getPaymentMode()!=null) {
            	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("paymentMode"), expenseLog.getPaymentMode()));
            	
            }
            if(expenseLog.getExpense()!=null) {
            	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("expense").get("id"), expenseLog.getExpense().getId()));
            	
            }
            if(expenseLog.getExpenseStartDate()!=null) {
            	predList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expenseDate"), expenseLog.getExpenseStartDate()));
            }
            if(expenseLog.getExpenseEndDate()!=null) {
            	predList.add(criteriaBuilder.lessThanOrEqualTo(root.get("expenseDate"), expenseLog.getExpenseEndDate()));
            }
            if(expenseLog.getStatus()!=null) {
            	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("status"), expenseLog.getStatus()));
            }
            if(expenseLog.getAccountType()!=null) {
            	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("accountType"), expenseLog.getAccountType()));
            }
           	predList.add(criteriaBuilder.equal(root.<ExpenseLog> get("company").get("id"), company.getId()));
         	return criteriaBuilder.and(predList.toArray(new Predicate[predList.size()]));
            }
        };
    }
	
	public static Specification<FollowUpPhone> getFollowUpPhoneByCompanySpec(Company company,long phoneLogId) {
		return new Specification<FollowUpPhone>() {
            @Override
            public Predicate toPredicate(Root<FollowUpPhone> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate followUpPhone = criteriaBuilder.equal(root.<FollowUpPhone> get("phoneLog").get("company").get("id"), company.getId());
           	Predicate followUpPhone1 = criteriaBuilder.equal(root.<FollowUpPhone> get("phoneLog").get("id"), phoneLogId);
           	Predicate followUpPhone2 = criteriaBuilder.equal(root.<FollowUpPhone> get("status"), STATUS.ACTIVE.toString());
    		return criteriaBuilder.and(followUpPhone,followUpPhone1,followUpPhone2);
            }
        };
    }
	public static Specification<Student> getStudentByCompanySpec(Company company,String status) {
		return new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studPred = criteriaBuilder.equal(root.<Student> get("company").get("id"), company.getId());
           	Predicate studPred1 = criteriaBuilder.equal(root.<Student> get("status"), status);
    		
    			return criteriaBuilder.and(studPred,studPred1);
            }
        };
    }
	public static Specification<Student> getStudentByEmailSpec(String email) {
		return new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studPred = criteriaBuilder.equal(root.<Student> get("email"), email);
           	return studPred;
            }
        };
    }
	
	
	public static Specification<StudentPayment> getStudentPaymentByCompanySpec(Company company,String status) {
		return new Specification<StudentPayment>() {
            @Override
            public Predicate toPredicate(Root<StudentPayment> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studPred = criteriaBuilder.equal(root.<StudentPayment> get("student").get("company").get("id"), company.getId());
           	Predicate studPred1 = criteriaBuilder.equal(root.<StudentPayment> get("status"), status);
    		
    			return criteriaBuilder.and(studPred,studPred1);
            }
        };
    }
	
	public static Specification<Reminders> getReminderByCompanySpec(Company company) {
		return new Specification<Reminders>() {
            @Override
            public Predicate toPredicate(Root<Reminders> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate reminderPred = criteriaBuilder.equal(root.<Reminders> get("company").get("id"), company.getId());
    		
    			return reminderPred;
            }
        };
    }
	
	public static Specification<StudentResult> getStudentResultByCompanySpec(Company company,String status) {
		return new Specification<StudentResult>() {
            @Override
            public Predicate toPredicate(Root<StudentResult> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studResPred = criteriaBuilder.equal(root.<StudentResult> get("student").get("company").get("id"), company.getId());
           	Predicate studResPred1 = criteriaBuilder.equal(root.<StudentPayment> get("status"), status);
    		
    			return criteriaBuilder.and(studResPred,studResPred1);
            }
        };
    }
	
	public static Specification<StudentLog> getStudentLogByCompanySpec(Company company,String status) {
		return new Specification<StudentLog>() {
            @Override
            public Predicate toPredicate(Root<StudentLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studResPred = criteriaBuilder.equal(root.<StudentLog> get("student").get("company").get("id"), company.getId());
           	Predicate studResPred1 = criteriaBuilder.equal(root.<StudentLog> get("status"), status);
    		
    			return criteriaBuilder.and(studResPred,studResPred1);
            }
        };
    }
	public static Specification<StudentCourse> getStudentCourseByCompanySpec(Company company,String status) {
		return new Specification<StudentCourse>() {
            @Override
            public Predicate toPredicate(Root<StudentCourse> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studResPred = criteriaBuilder.equal(root.<StudentCourse> get("student").get("company").get("id"), company.getId());
           	Predicate studResPred1 = criteriaBuilder.equal(root.<StudentCourse> get("status"), status);
    		
    			return criteriaBuilder.and(studResPred,studResPred1);
            }
        };
    }
	
	public static Specification<StudentCourseDetails> getStudentCourseDetailsByCompanySpec(Long studentCourseId) {
		return new Specification<StudentCourseDetails>() {
            @Override
            public Predicate toPredicate(Root<StudentCourseDetails> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studResPred = criteriaBuilder.equal(root.<StudentCourseDetails> get("studentCourse").get("id"), studentCourseId);
           
    			return criteriaBuilder.and(studResPred);
            }
        };
    }
	public static Specification<EmployeeHourLog> getEmployeeHourLogByCompanySpec(Company company,String status) {
		return new Specification<EmployeeHourLog>() {
            @Override
            public Predicate toPredicate(Root<EmployeeHourLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studResPred = criteriaBuilder.equal(root.<EmployeeHourLog> get("employeeUser").get("company").get("id"), company.getId());
           	Predicate studResPred1 = criteriaBuilder.equal(root.<EmployeeHourLog> get("status"), status);
    		
    			return criteriaBuilder.and(studResPred,studResPred1);
            }
        };
    }
	public static Specification<Employee_User> getEmployeeCompanySpec(Company company,String status) {
		return new Specification<Employee_User>() {
            @Override
            public Predicate toPredicate(Root<Employee_User> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate studResPred = criteriaBuilder.equal(root.<Employee_User> get("company").get("id"), company.getId());
           	Predicate studResPred1 = criteriaBuilder.equal(root.<Employee_User> get("status"), status);
    		
    			return criteriaBuilder.and(studResPred,studResPred1);
            }
        };
    }
	public static Specification<CompanyDetails> getCompanDetailsByCompanySpec(Company company) {
		return new Specification<CompanyDetails>() {
            @Override
            public Predicate toPredicate(Root<CompanyDetails> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate reminderPred = criteriaBuilder.equal(root.<CompanyDetails> get("company").get("id"), company.getId());
    		
    			return reminderPred;
            }
        };
    }
	
	public static Specification<Message> getMessagesByCompanySpec(Company company) {
		return new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate messagePred = criteriaBuilder.equal(root.<Message> get("company").get("id"), company.getId());
           	Predicate messagePred1 = criteriaBuilder.equal(root.<Message> get("referenceId"), 0);
    		
    			return criteriaBuilder.and(messagePred,messagePred1);
            }
        };
    }
	
	public static Specification<Test> getTestsByCompanySpec(Company company) {
		return new Specification<Test>() {
            @Override
            public Predicate toPredicate(Root<Test> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate messagePred = criteriaBuilder.equal(root.<Test> get("company").get("id"), company.getId());
           	return messagePred;
            }
        };
    }
	public static Specification<Test> checkTestAvailable(String test,Company company) {
		return new Specification<Test>() {
            @Override
            public Predicate toPredicate(Root<Test> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate testPred = criteriaBuilder.equal(root.<Test>get("company").get("id"), company.getId());
           	Predicate testPred1 = criteriaBuilder.equal(root.<Test> get("name"),test);
           	return criteriaBuilder.and(testPred,testPred1);
            }
        };
    }
	
	public static Specification<TestResult> getTestResulysByMailTest(String mail,String test) {
		return new Specification<TestResult>() {
            @Override
            public Predicate toPredicate(Root<TestResult> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate testResPred1 = criteriaBuilder.equal(root.<TestResult> get("student").get("email"), mail);
           	Predicate testResPred2 = criteriaBuilder.equal(root.<TestResult> get("test").get("id"), test);
           	return criteriaBuilder.and(testResPred1,testResPred2);
            }
        };
    }
	public static Specification<TestResult> getTestResultsByCompanySpec(Company company) {
		return new Specification<TestResult>() {
            @Override
            public Predicate toPredicate(Root<TestResult> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate messagePred = criteriaBuilder.equal(root.<TestResult>get("test").get("company").get("id"), company.getId());
           	return messagePred;
            }
        };
    }
	public static Specification<TestResultDetail> getTestResultDetailByCompanySpec(Long testResID) {
		return new Specification<TestResultDetail>() {
            @Override
            public Predicate toPredicate(Root<TestResultDetail> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate testResPred = criteriaBuilder.equal(root.<TestResultDetail>get("testResult").get("id"), testResID);
           	return testResPred;
            }
        };
    }
	
	public static Specification<Question> getQuestionssByCompanySpec(Company company) {
		return new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate testPred = criteriaBuilder.equal(root.<Question> get("test").get("company").get("id"), company.getId());
           	return testPred;
            }
        };
    }
	public static Specification<Question> getQuestionsCritieria(Question question) {
		return new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
            	
           	List<Predicate> lsPred = new ArrayList<Predicate>();
           	
           	if(question.getQuestn()!=null) {
           		lsPred.add(criteriaBuilder.equal(root.get("questn"), question.getQuestn()));
           	}
           	if(question.getOptionA()!=null) {
           		lsPred.add(criteriaBuilder.equal(root.get("optionA"), question.getOptionA()));
           	}
           	if(question.getOptionB()!=null) {
           		lsPred.add(criteriaBuilder.equal(root.get("optionB"), question.getOptionB()));
           	}
           	if(question.getOptionC()!=null) {
           		lsPred.add(criteriaBuilder.equal(root.get("optionC"), question.getOptionC()));
           	}
           	if(question.getOptionD()!=null) {
           		lsPred.add(criteriaBuilder.equal(root.get("optionD"), question.getOptionD()));
           	}
           	if(question.getCorrectAnswer()!=null) {
           		lsPred.add(criteriaBuilder.equal(root.get("correctAnswer"), question.getCorrectAnswer()));
           	}
           	if(question.getCorrectAnswer()!=null) {
           		lsPred.add(criteriaBuilder.equal(root.get("test").get("id"), question.getTest().getId()));
           	}
           		return criteriaBuilder.and(lsPred.toArray(new Predicate[lsPred.size()]));
           }
		};
    }
	public static Specification<Question> getQuestionsByTest(String id) {
		return new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate testPred = criteriaBuilder.equal(root.<Question> get("test").get("id"), id);
           	return testPred;
            }
        };
    }
	public static Specification<Message> getMessagesById(Long Id) {
		return new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate messagePred = criteriaBuilder.equal(root.<Message> get("id"), Id);
           	Predicate messagePred1 = criteriaBuilder.equal(root.<Message> get("referenceId"), Id);
    		
    			return criteriaBuilder.or(messagePred,messagePred1);
            }
        };
    }
	public static Specification<Student> getSearchStudentResultByCompanySpec(StudentSearch searchStudent,long companyId) {
		return new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
            List<Predicate> lsPred = new ArrayList<Predicate>();
           	lsPred.add(criteriaBuilder.equal(root.<Student> get("company").get("id"), companyId));
           	if(searchStudent.getStartDate()!=null) {
           		lsPred.add(criteriaBuilder.greaterThanOrEqualTo(root.get("registrationDate"), searchStudent.getStartDate()));
           	}
           	if(searchStudent.getEndDate()!=null) {
           		lsPred.add(criteriaBuilder.lessThanOrEqualTo(root.get("registrationDate"), searchStudent.getEndDate()));
           	}
           	if(!StringUtils.isEmpty(searchStudent.getName())) {
           		lsPred.add(criteriaBuilder.or(criteriaBuilder.like(root.get("firstName"), "%"+searchStudent.getName()+"%"),criteriaBuilder.like(root.get("lastName"), "%"+searchStudent.getName()+"%")));
           		
           	}
           	if(!StringUtils.isEmpty(searchStudent.getResult())) {
           		lsPred.add(criteriaBuilder.equal(root.<Student> get("studentResult").get("result"), searchStudent.getResult()));
           	}
           	if(!StringUtils.isEmpty(searchStudent.getReference())) {
           		lsPred.add(criteriaBuilder.equal(root.<Student> get("reference"), searchStudent.getReference()));
           	}
           	lsPred.add(criteriaBuilder.equal(root.<Student> get("status"), STATUS.ACTIVE.toString()));
           		return criteriaBuilder.and(lsPred.toArray(new Predicate[lsPred.size()]));
            }
        };
    }
	
	
	/*
	 * public static Specification<StudentPayment>
	 * getStudentPaymentsReminderByPayment(Company company,LocalDate localDate) {
	 * return new Specification<StudentPayment>() {
	 * 
	 * @Override public Predicate toPredicate(Root<StudentPayment> root,
	 * CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	 * Path<StudentPayment> dateCreatedPath = root.<StudentPayment>
	 * get("paymentDate"); Predicate studPaymentPred1 =
	 * criteriaBuilder.lessThan(dateCreatedPath,localDate); Predicate
	 * studPaymentPred2 = criteriaBuilder.equal(root.<Student>
	 * get("student").get("company").get("id"), company.getId()); return
	 * criteriaBuilder.and(studPaymentPred1,studPaymentPred2); } }; }
	 */
}
