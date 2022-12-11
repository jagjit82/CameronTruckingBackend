package com.cameron.driver.education.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.StudentClassroom;
import com.cameron.driver.education.repository.StudentClassroomRepository;

@RestController
@RequestMapping({ "/studentclassroom" })
public class StudentClassroomController {
	@Autowired
	private StudentClassroomRepository studentClassroomRepository;

	@GetMapping("/all")
	public ResponseEntity<List<StudentClassroom>> getAllStudentClassroomClassrooms() {
		return ResponseEntity.ok().body(studentClassroomRepository.findAll());
	}
	@GetMapping("/studentclassroom/{id}")
	public ResponseEntity<Object> getStudentClassroomById(@PathVariable(value = "id") Long studentClassroomId)
			throws ResourceNotFoundException {
		Optional<StudentClassroom> studentClassroom = studentClassroomRepository.findById(studentClassroomId);
				if(studentClassroom.isPresent()) {
					return ResponseEntity.status(HttpStatus.OK).body(studentClassroom.get());	
				}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("StudentClassroom Not Found");
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<String> createStudentClassroom(@RequestBody StudentClassroom studentClassroom) {
		try {
			studentClassroom.setStatus(STATUS.ACTIVE.toString());
			studentClassroomRepository.save(studentClassroom);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}

	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteStudentClassroom(@PathVariable(value = "id") Long studentClassroomId)
			throws ResourceNotFoundException {
		Optional<StudentClassroom> studentClassroom = studentClassroomRepository.findById(studentClassroomId);
				if(studentClassroom.isPresent()) {
					studentClassroomRepository.delete(studentClassroom.get());	
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
}
