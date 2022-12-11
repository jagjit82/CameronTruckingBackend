	package com.cameron.driver.education.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.TestResultDetail;

@Repository
public interface TestResultDetailRepository extends JpaRepository<TestResultDetail,Long>,JpaSpecificationExecutor<TestResultDetail>{

	@Query("SELECT trd,sum(correct) FROM TestResultDetail trd where trd.testResult.test.company.id=:id group by trd.testResult.id")
	public List<Object[]> getTestResult(@Param("id") long id);
	
}
