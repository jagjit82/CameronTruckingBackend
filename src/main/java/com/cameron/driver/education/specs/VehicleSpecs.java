package com.cameron.driver.education.specs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.DieselLog;
import com.cameron.driver.education.model.InstructorMonitoring;
import com.cameron.driver.education.model.InstructorTraining;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.model.Vehicle;
import com.cameron.driver.education.model.VehicleLog;

public class VehicleSpecs {
	public static Specification<Vehicle> getVehiclesByCompanySpec(Company company,String status) {
		return new Specification<Vehicle>() {
            @Override
            public Predicate toPredicate(Root<Vehicle> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate vehiclePred = criteriaBuilder.equal(root.<Vehicle> get("company").get("id"), company.getId());
           	Predicate vehiclePred1 = criteriaBuilder.equal(root.<Vehicle> get("status"), status);
    		
    			return criteriaBuilder.and(vehiclePred,vehiclePred1);
            }
        };
    }
	public static Specification<VehicleLog> getvehicleLogsByCompanySpec(Company company,String status) {
		return new Specification<VehicleLog>() {
            @Override
            public Predicate toPredicate(Root<VehicleLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate vehiclePred = criteriaBuilder.equal(root.<VehicleLog> get("vehicle").get("company").get("id"), company.getId());
           	Predicate vehiclePred1 = criteriaBuilder.equal(root.<VehicleLog> get("status"), status);
    		
           	return criteriaBuilder.and(vehiclePred,vehiclePred1);
            }
        };
    }
	
	public static Specification<DieselLog> getDieselLogsByCompanySpec(Company company,String status) {
		return new Specification<DieselLog>() {
            @Override
            public Predicate toPredicate(Root<DieselLog> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate dieselLog = criteriaBuilder.equal(root.<DieselLog> get("vehicle").get("company").get("id"), company.getId());
           	Predicate dieselLog1 = criteriaBuilder.equal(root.<DieselLog> get("status"), status);
    		
           	return criteriaBuilder.and(dieselLog,dieselLog1);
            }
        };
    }
	
	public static Specification<InstructorMonitoring> getInstructorMonitoringsByCompanySpec(Company company) {
		return new Specification<InstructorMonitoring>() {
            @Override
            public Predicate toPredicate(Root<InstructorMonitoring> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate instrucPred = criteriaBuilder.equal(root.<InstructorMonitoring> get("instructorTrainee").get("company").get("id"), company.getId());
           	Predicate instrucPred1 = criteriaBuilder.equal(root.<InstructorMonitoring> get("status"), STATUS.ACTIVE.toString());
    			return criteriaBuilder.and(instrucPred,instrucPred1);
            }
        };
    }
	public static Specification<InstructorTraining> getInstructorTrainingsByCompanySpec(Company company) {
		return new Specification<InstructorTraining>() {
            @Override
            public Predicate toPredicate(Root<InstructorTraining> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
           	Predicate instPred = criteriaBuilder.equal(root.<InstructorTraining> get("instructorTrainee").get("company").get("id"), company.getId());
         	Predicate instrucPred1 = criteriaBuilder.equal(root.<InstructorTraining> get("status"), STATUS.ACTIVE.toString());
			return criteriaBuilder.and(instPred,instrucPred1);
        
    	    }
        };
    }
	
}
