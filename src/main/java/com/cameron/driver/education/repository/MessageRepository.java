	package com.cameron.driver.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>,JpaSpecificationExecutor<Message>{

	@Modifying
	@Query("delete from Message mess where mess.id=:id or mess.referenceId=:id")
	Integer deleteMessages(@Param("id") long id);
	
}
