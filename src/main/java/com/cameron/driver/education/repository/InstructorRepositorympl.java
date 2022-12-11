package com.cameron.driver.education.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cameron.driver.education.constant.CommonConstants;
import com.cameron.driver.education.model.Instructor;
@Transactional
@Repository
public class InstructorRepositorympl implements InstructorCustomRepository {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private InstructorRepository instructorRepository;

	
	/*
	 * @Override public List<Instructor> transportLicExpiryRecordsCurrentMonth() {
	 * // TODO Auto-generated method stub int pageNumber = 0; Pageable page =
	 * PageRequest.of(pageNumber,
	 * Integer.MAX_VALUE,Sort.by("firstName").ascending()); LocalDate localDate=
	 * LocalDate.now(); int month = LocalDate.now().getMonth().getValue(); int year
	 * = LocalDate.now().getYear(); LocalDate startDate = LocalDate.of(year, month,
	 * 1); LocalDate endDate = LocalDate.of(year, month, localDate.lengthOfMonth());
	 * System.out.println("start date "+startDate);
	 * System.out.println("end date "+endDate); Page<Instructor> pageInstructor=
	 * instructorRepository.findAll(new Specification<Instructor>() {
	 * 
	 * @Override public Predicate toPredicate(Root<Instructor> root,
	 * CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) { List<Predicate>
	 * predicates = new ArrayList<>();
	 * predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.
	 * get("transportLicenceExp"), startDate)));
	 * predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get
	 * ("transportLicenceExp"), endDate)));
	 * 
	 * return criteriaBuilder.and(predicates.toArray(new
	 * Predicate[predicates.size()])); } },page); return pageInstructor.getContent(); }
	 */
	/*
	 * public List<Instructor> nonTransportLicExpiryRecordsCurrentMonth() { // TODO
	 * Auto-generated method stub int pageNumber = 0; Pageable page =
	 * PageRequest.of(pageNumber,
	 * Integer.MAX_VALUE,Sort.by("firstName").ascending()); LocalDate localDate=
	 * LocalDate.now(); int month = LocalDate.now().getMonth().getValue(); int year
	 * = LocalDate.now().getYear(); LocalDate startDate = LocalDate.of(year, month,
	 * 1); LocalDate endDate = LocalDate.of(year, month, localDate.lengthOfMonth());
	 * System.out.println("start date "+startDate);
	 * System.out.println("end date "+endDate); Page<Instructor> pageInstructor =
	 * instructorRepository.findAll(new Specification<Instructor>() {
	 * 
	 * @Override public Predicate toPredicate(Root<Instructor> root,
	 * CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) { List<Predicate>
	 * predicates = new ArrayList<>();
	 * predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.
	 * get("nonTransportLicenceExp"), startDate)));
	 * predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get
	 * ("nonTransportLicenceExp"), endDate)));
	 * 
	 * return criteriaBuilder.and(predicates.toArray(new
	 * Predicate[predicates.size()])); } },page); return pageInstructor.getContent(); }
	 */
	
	/*
	 * @Override public Page<Instructor> searchInstructorByName(String
	 * instructorName,int pageNumber) { // TODO Auto-generated method stub Pageable page
	 * = PageRequest.of(pageNumber,
	 * CommonConstants.pageSize,Sort.by("firstName").ascending()); Page<Instructor>
	 * pageInstructor= instructorRepository.findAll(new Specification<Instructor>() {
	 * 
	 * @Override public Predicate toPredicate(Root<Instructor> root,
	 * CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) { List<Predicate>
	 * predicates = new ArrayList<>();
	 * predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("firstName")
	 * , "%"+instructorName+"%")));
	 * predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("lastName"),
	 * "%"+instructorName+"%")));
	 * 
	 * return criteriaBuilder.or(predicates.toArray(new
	 * Predicate[predicates.size()])); } },page); return pageInstructor; }
	 */
		
}
