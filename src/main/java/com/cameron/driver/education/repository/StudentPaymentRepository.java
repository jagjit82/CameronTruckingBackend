package com.cameron.driver.education.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cameron.driver.education.model.StudentPayment;

@Repository
public interface StudentPaymentRepository extends JpaRepository<StudentPayment, Long>,JpaSpecificationExecutor<StudentPayment>{

	@Query("select sum(sp.payment) from StudentPayment sp where sp.student.id=:studentId")
	String getTotalPaymentByStudent(@Param("studentId") Long studentId); 
	
	@Query("select sum(sp.payment) from StudentPayment sp where sp.student.id=:studentId and sp.id!=:studentPaymentId")
	String getTotalPaymentByStudentForEdit(@Param("studentId") Long studentId,@Param("studentPaymentId") Long studentPaymentId);
	
	
	//@Query("select stud,studPay,sum(studPay.payment) from StudentPayment studPay left join Student stud on stud.id=studPay.student.id where studPay.paymentDate<:paymentDate and stud.company.id=:company group by studPay.student.id")
	//List<Object[]> getStudentandPaymentsReminder(@Param("company") long company,@Param("paymentDate") LocalDate paymentDate);

	@Query("select studPay,(select sum(sp1.payment) from StudentPayment sp1 where sp1.student.id=studPay.student.id group by sp1.student.id)  from StudentPayment studPay where studPay.id in (select max(id) from StudentPayment studPay where studPay.student.company.id=:company group by studPay.student.id) and studPay.paymentDate<:paymentDate")
	List<Object[]> getStudentandPaymentsReminder(@Param("company") long company,@Param("paymentDate") LocalDate paymentDate);

}
