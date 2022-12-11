package com.cameron.driver.education.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.cameron.driver.education.model.ActivityLog;
import com.cameron.driver.education.model.Company;

public class ActivityLogSpecs {
	public static Specification<ActivityLog> getActivityLogsByCompanySpec(Company company) {
		return new Specification<ActivityLog>() {
            @Override
            public Predicate toPredicate(Root<ActivityLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate activityPred = criteriaBuilder.equal(root.<ActivityLog> get("company").get("id"), company.getId());
           		return activityPred;
            }
        };
    }
		
}
