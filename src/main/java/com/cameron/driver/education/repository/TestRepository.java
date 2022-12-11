	package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.Test;

@Repository
public interface TestRepository extends JpaRepository<Test,Long>,JpaSpecificationExecutor<Test>{

  public Test findByName(String name);
	
}
