package com.cameron.driver.education.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cameron.driver.education.commonservice.CommonService;
import com.cameron.driver.education.constant.CommonConstants;
import com.cameron.driver.education.constant.ROLES;
import com.cameron.driver.education.constant.STATUS;
import com.cameron.driver.education.exception.ResourceNotFoundException;
import com.cameron.driver.education.model.Company;
import com.cameron.driver.education.model.Message;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.MessageRepository;
import com.cameron.driver.education.specs.GenericSpecs;

@RestController
@RequestMapping("/message")
public class MessageController {
	@Autowired
	@Lazy
	private MessageRepository messageRepository;

	@Autowired
	private CompanyRepository companyRepostory;
	
	
	@GetMapping("/all/{company}")
	public List<Message> getAllMessages(
			@PathVariable(value = "company") String company) {

		Company companyObj = companyRepostory.findCompanyByName(company);
		Sort sort = Sort.by("id").descending();
		List<Message> messageList = messageRepository.findAll(GenericSpecs.getMessagesByCompanySpec(companyObj),sort);
		return messageList;
	}
	
	@GetMapping("/countdata/{company}")
	public ResponseEntity<Long> countMessages(@PathVariable(value = "company") String company ) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		return ResponseEntity.ok().body(messageRepository.count(GenericSpecs.getMessagesByCompanySpec(companyObj)));
	}
	
	@GetMapping("/alldata/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<Message> getAllMessageList(
			@PathVariable(value = "pageNum") Long pageNum,@PathVariable(value = "sortField") String sortField ,@PathVariable(value = "sortOrder") String sortOrder,@PathVariable(value = "company") String company  ) {

		int pageNumber = 0;
		sortField="id";
		Pageable page = null;
		if(pageNum==null || pageNum==0) {
			pageNumber=0;
		}else
		{
			pageNumber=pageNum.intValue()-1;
		}
		//Sort sort = Sort.by("id").descending();
		Company companyObj = companyRepostory.findCompanyByName(company);
		if(sortOrder.equalsIgnoreCase("ascending")) {
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).ascending());
		}else if(sortOrder.equalsIgnoreCase("descending")){
			page= PageRequest.of(pageNumber, CommonConstants.pageSize,Sort.by(sortField).descending());
		}	
		Page<Message> messageList = messageRepository.findAll(GenericSpecs.getMessagesByCompanySpec(companyObj),page);
		return messageList.getContent();
	}

	@GetMapping("/messages/{id}")
	public List<Message>  getMessagesById(@PathVariable(value = "id") Long messageId) {
		List<Message> messageList = messageRepository.findAll(GenericSpecs.getMessagesById(messageId));
		return messageList;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getMessageById(@PathVariable(value = "id") Long messageId)
			throws ResourceNotFoundException {
		Optional<Message> message = messageRepository.findById(messageId);
		if (message.isPresent()) {
			return ResponseEntity.ok(message.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message Not Found");
	}

	
	@GetMapping("updatestatus/{id}")
	public ResponseEntity<Object> updateStatus(@PathVariable(value = "id") Long messageId)
			throws ResourceNotFoundException {
		Optional<Message> message = messageRepository.findById(messageId);
		if (message.isPresent()) {
			Message mess=message.get();
			mess.setStatus(STATUS.UNREAD.toString());
			messageRepository.save(mess);
			return ResponseEntity.ok(message.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message Not Found");
	}
	@PostMapping("/create")
	public ResponseEntity<String> createMessage(@RequestBody Message message) {
		try {
			message.setCreatedDate(LocalDateTime.now());
			message.setModifiedDate(LocalDateTime.now());
			message.setStatus(STATUS.UNREAD.toString());
			messageRepository.save(message);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateMessage(@RequestBody Message message) throws ResourceNotFoundException {
		try {
			message.setModifiedDate(LocalDateTime.now());
			messageRepository.save(message);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<String> deleteMessage(@PathVariable(value = "id") Long messageId)
			throws ResourceNotFoundException {
		try {
				messageRepository.deleteMessages(messageId);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			return new ResponseEntity<>(HttpStatus.OK);
		
	}

	
	}
