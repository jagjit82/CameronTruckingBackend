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
import com.cameron.driver.education.model.Instructor;
import com.cameron.driver.education.model.InstructorLog;
import com.cameron.driver.education.model.InstructorLogSearch;
import com.cameron.driver.education.model.InstructorVO;
import com.cameron.driver.education.model.Student;

public class InstructorSpecs {
	public static Specification<Instructor> getInstructorsByCompanySpec(long id) {
		return new Specification<Instructor>() {
            @Override
            public Predicate toPredicate(Root<Instructor> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate instructorPred = criteriaBuilder.equal(root.<Instructor> get("company").get("id"), id);
    		
    			return instructorPred;
            }
        };
    }
	public static Specification<InstructorLog> getInstructorLogByCompanySpec(long companyId, String status) {
		return new Specification<InstructorLog>() {
            @Override
            public Predicate toPredicate(Root<InstructorLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
            	Predicate vehiclePred = criteriaBuilder.equal(root.<InstructorLog> get("instructor").get("company").get("id"), companyId);
            	Predicate vehiclePred1 = criteriaBuilder.equal(root.<InstructorLog> get("status"), STATUS.ACTIVE.toString());
            	return criteriaBuilder.and(vehiclePred,vehiclePred1);	
           	}
        };
    }
	public static Specification<Instructor> getInstructorsByCompanySpec(InstructorVO instructorVO) {
		return new Specification<Instructor>() {
            @Override
            public Predicate toPredicate(Root<Instructor> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
            	Predicate vehiclePred = criteriaBuilder.equal(root.<Instructor> get("company").get("company"), instructorVO.getCompany());
            	Predicate vehiclePred1 = criteriaBuilder.equal(root.<Instructor> get("status"), STATUS.ACTIVE.toString());
            	return criteriaBuilder.and(vehiclePred,vehiclePred1);	
           	}
        };
    }
	public static Specification<Instructor> getInstructorsByInstructorSpec(InstructorVO instructorVO) {
		return new Specification<Instructor>() {
            @Override
            public Predicate toPredicate(Root<Instructor> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
            	List<Predicate> lsPred = new ArrayList<Predicate>();
            	if(instructorVO.getSearchInstructor()!=null) {
            		lsPred.add(criteriaBuilder.or(criteriaBuilder.like(root.get("firstName"), "%"+instructorVO.getSearchInstructor()+"%"),criteriaBuilder.like(root.get("lastName"), "%"+instructorVO.getSearchInstructor()+"%")));
            	}
            	lsPred.add(criteriaBuilder.equal(root.<Instructor> get("company").get("company"), instructorVO.getCompany()));
            	return criteriaBuilder.and(lsPred.toArray(new Predicate[lsPred.size()]));	
           	}
        };
    }
	
	public static Specification<InstructorLog> getInstructorLogsBySearch(InstructorLogSearch instructorVO,Long id) {
		return new Specification<InstructorLog>() {
            @Override
            public Predicate toPredicate(Root<InstructorLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
            	List<Predicate> lsPred = new ArrayList<Predicate>();
            	Predicate vehiclePred1=null;
            	lsPred.add(criteriaBuilder.equal(root.<InstructorLog> get("instructor").get("company").get("id"), id));
            	if(instructorVO.getInstructor()!=null) {
            		vehiclePred1 = criteriaBuilder.equal(root.<InstructorLog> get("instructor").get("id"), instructorVO.getInstructor().getId());
            		lsPred.add(vehiclePred1);
            	}
                	if(instructorVO.getStartDate()!=null) {
                		lsPred.add(criteriaBuilder.greaterThan(root.get("logDate"), instructorVO.getStartDate()));
                	}
                	if(instructorVO.getEndDate()!=null) {
                		lsPred.add(criteriaBuilder.lessThanOrEqualTo(root.get("logDate"), instructorVO.getEndDate()));
                	}
                	if(!StringUtils.isEmpty(instructorVO.getTrainingType())) {
                		lsPred.add(criteriaBuilder.equal(root.<InstructorLog> get("trainingType"), instructorVO.getTrainingType()));
                	}
                		
             		return criteriaBuilder.and(lsPred.toArray(new Predicate[lsPred.size()]));
                 }	
           	};
    }
}
