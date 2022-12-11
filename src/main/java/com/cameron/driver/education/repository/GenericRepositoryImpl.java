package com.cameron.driver.education.repository;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@Repository
public class GenericRepositoryImpl implements GenericRepository {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Integer countTotalApprovals() {
		 Query countQuery = em.createNativeQuery(
				 "SELECT("
				 +   "(SELECT COUNT(*) FROM Student where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM Student_Payment where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM diesel_log where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM followup_phone where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM instructor where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM instructor_monitoring where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM instructor_training where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM phone_log where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM student_result where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM vehicle where status='DELETE')"
				 +" + (SELECT COUNT(*) FROM vehicle_log where status='DELETE')"
				 + ")"
				 );
		 Object obj = countQuery.getSingleResult();
	        BigInteger count = (BigInteger)countQuery.getSingleResult();        
	        return count.intValue();
		
		
	}
	
		
	
}
