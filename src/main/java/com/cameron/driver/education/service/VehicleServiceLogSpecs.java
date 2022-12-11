package com.cameron.driver.education.service;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.cameron.driver.education.model.VehicleLog;

public final class VehicleServiceLogSpecs {


public static Specification<VehicleLog> findFleetLogs() {
    return new Specification<VehicleLog>() {
    	public Predicate toPredicate(Root<VehicleLog> root, CriteriaQuery<?> query,
    	        CriteriaBuilder builder) {

    	     LocalDate date = LocalDate.now();
    	     return builder.lessThan(root.get("birthday"), date);
    	  }
  };
}
}