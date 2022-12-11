package com.cameron.driver.education.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.model.VehicleLog;
@Transactional
@Repository
public class VehicleLogRepositoryImpl implements VehicleLogCustomRepository {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<VehicleLog> searchVehicleSeriveLog(VehicleLog vehicleServiceLog) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
	    CriteriaQuery<VehicleLog> cq = qb.createQuery(VehicleLog.class);
	    Root<VehicleLog> vehicleServiceLogRoot = cq.from(VehicleLog.class);
	    // Constructing list of parameters
	    List<Predicate> predicates = new ArrayList<Predicate>();

	    //Adding predicates in case of parameter not being null
	    if (vehicleServiceLog.getVehicle()!= null) {
	        predicates.add(
	                qb.equal(vehicleServiceLogRoot.get("vehicle").get("id"), vehicleServiceLog.getVehicle().getId()));
	    }
	    if (vehicleServiceLog.getDiesel() != null) {
	        predicates.add(
	                qb.equal(vehicleServiceLogRoot.get("diesel"), vehicleServiceLog.getDiesel()));
	    }
	    if (vehicleServiceLog.getStatus()!= null) {
	        predicates.add(
	                qb.equal(vehicleServiceLogRoot.get("status"), vehicleServiceLog.getStatus()));
	    }
	    if (vehicleServiceLog.getRepairDate() != null) {
	        predicates.add(
	                qb.greaterThan(vehicleServiceLogRoot.get("repairDate"), vehicleServiceLog.getRepairDate()));
	    }
	    if (vehicleServiceLog.getRepairDate() != null) {
	        predicates.add(
	                qb.lessThanOrEqualTo(vehicleServiceLogRoot.get("repairDate"), vehicleServiceLog.getRepairDate()));
	    }
	    cq.select(vehicleServiceLogRoot)
	            .where(predicates.toArray(new Predicate[]{}));
	
	    javax.persistence.Query query = em.createQuery(cq);
		return query.getResultList();
		
	}
	
		
	
}
