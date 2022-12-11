package com.cameron.driver.education.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cameron.driver.education.model.Vehicle;
@Transactional
@Repository
public class VehicleRepositoryImpl implements VehicleCustomRepository {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	@Lazy
	private VehicleRepository vehicleRepository;

	
	@Override
	public List<Vehicle> insuranceExpiryRecordsCurrentMonth() {
		// TODO Auto-generated method stub
		int pageNumber = 0;
		Pageable page = PageRequest.of(pageNumber, Integer.MAX_VALUE,Sort.by("truckNum").ascending());
		LocalDate localDate= LocalDate.now();
		int month = LocalDate.now().getMonth().getValue();
		int year = LocalDate.now().getYear();
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = LocalDate.of(year, month, localDate.lengthOfMonth());
		System.out.println("start date "+startDate);
		System.out.println("end date "+endDate);
		/*
		 * // Page<Vehicle> pageTruck = vehicleRepository.findAll(new
		 * Specification<Vehicle>() {
		 * 
		 * @Override public Predicate toPredicate(Root<Vehicle> root, CriteriaQuery<?>
		 * query, CriteriaBuilder criteriaBuilder) { List<Predicate> predicates = new
		 * ArrayList<>();
		 * predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.
		 * get("insuranceDate"), startDate)));
		 * predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get
		 * ("insuranceDate"), endDate)));
		 * 
		 * return criteriaBuilder.and(predicates.toArray(new
		 * Predicate[predicates.size()])); } },page);
		 */
		return null;//pageTruck.getContent();
	}
	public List<Vehicle> permitExpiryRecordsCurrentMonth() {
		// TODO Auto-generated method stub
		int pageNumber = 0;
		Pageable page = PageRequest.of(pageNumber, Integer.MAX_VALUE,Sort.by("truckNum").ascending());
		LocalDate localDate= LocalDate.now();
		int month = LocalDate.now().getMonth().getValue();
		int year = LocalDate.now().getYear();
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = LocalDate.of(year, month, localDate.lengthOfMonth());
		System.out.println("start date "+startDate);
		System.out.println("end date "+endDate);
		/*
		 * Page<Vehicle> pageTruck = vehicleRepository.findAll(new
		 * Specification<Vehicle>() {
		 * 
		 * @Override public Predicate toPredicate(Root<Vehicle> root, CriteriaQuery<?>
		 * query, CriteriaBuilder criteriaBuilder) { List<Predicate> predicates = new
		 * ArrayList<>();
		 * predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.
		 * get("permitDate"), startDate)));
		 * predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get
		 * ("permitDate"), endDate)));
		 * 
		 * return criteriaBuilder.and(predicates.toArray(new
		 * Predicate[predicates.size()])); } },page); return pageTruck.getContent();
		 */
		return null;
	}
	public List<Vehicle> fitnessExpiryRecordsCurrentMonth() {
		// TODO Auto-generated method stub
		int pageNumber = 0;
		Pageable page = PageRequest.of(pageNumber, Integer.MAX_VALUE,Sort.by("truckNum").ascending());
		LocalDate localDate= LocalDate.now();
		int month = LocalDate.now().getMonth().getValue();
		int year = LocalDate.now().getYear();
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = LocalDate.of(year, month, localDate.lengthOfMonth());
		System.out.println("start date "+startDate);
		System.out.println("end date "+endDate);
		/*
		 * Page<Vehicle> pageTruck = vehicleRepository.findAll(new
		 * Specification<Vehicle>() {
		 * 
		 * @Override public Predicate toPredicate(Root<Vehicle> root, CriteriaQuery<?>
		 * query, CriteriaBuilder criteriaBuilder) { List<Predicate> predicates = new
		 * ArrayList<>();
		 * predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThan(root.get(
		 * "fitnessDate"), startDate)));
		 * predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get(
		 * "fitnessDate"), endDate)));
		 * 
		 * return criteriaBuilder.and(predicates.toArray(new
		 * Predicate[predicates.size()])); } },page); return pageTruck.getContent();
		 */
		return null;
	}
	
		
}
