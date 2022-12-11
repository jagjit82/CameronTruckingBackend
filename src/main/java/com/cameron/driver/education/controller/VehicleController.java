package com.cameron.driver.education.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
import com.cameron.driver.education.model.Reminders;
import com.cameron.driver.education.model.Student;
import com.cameron.driver.education.model.StudentPayment;
import com.cameron.driver.education.model.StudentResult;
import com.cameron.driver.education.model.StudentVO;
import com.cameron.driver.education.model.Vehicle;
import com.cameron.driver.education.model.VehicleLog;
import com.cameron.driver.education.repository.CompanyRepository;
import com.cameron.driver.education.repository.RemindersRepository;
import com.cameron.driver.education.repository.StudentPaymentRepository;
import com.cameron.driver.education.repository.VehicleRepository;
import com.cameron.driver.education.specs.VehicleSpecs;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
	@Autowired
	@Lazy
	private VehicleRepository vehicleRepository;

	@Autowired
	private CompanyRepository companyRepostory;
	@Autowired
	private RemindersRepository remindersRepository;

	@GetMapping("/all/{pageNum}/{sortField}/{sortOrder}/{company}")
	public List<Vehicle> getAllVehicles(@PathVariable(value = "pageNum") Long pageNum,
			@PathVariable(value = "sortField") String sortField, @PathVariable(value = "sortOrder") String sortOrder,
			@PathVariable(value = "company") String company) {

		int pageNumber = 0;
		Pageable page = null;
		if (pageNum == null || pageNum == 0) {
			pageNumber = 0;
		} else {
			pageNumber = pageNum.intValue()-1;
		}
		Company companyObj = companyRepostory.findCompanyByName(company);
		if (sortOrder.equalsIgnoreCase("ascending")) {
			page = PageRequest.of(pageNumber, CommonConstants.pageSize, Sort.by(sortField).ascending());
			;
		} else if (sortOrder.equalsIgnoreCase("descending")) {
			page = PageRequest.of(pageNumber, CommonConstants.pageSize, Sort.by(sortField).descending());
		}
		Page<Vehicle> vehicleList = vehicleRepository.findAll(VehicleSpecs.getVehiclesByCompanySpec(companyObj,STATUS.ACTIVE.toString()), page);
		List<Vehicle> vehicleLs = vehicleList.getContent();

		return vehicleLs;
	}

	@GetMapping("/allapprovals/{company}")
	public List<Vehicle> getAllVehicles(
			@PathVariable(value = "company") String company) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		List<Vehicle> vehicleLs  = vehicleRepository.findAll(VehicleSpecs.getVehiclesByCompanySpec(companyObj,STATUS.DELETE.toString()));
		
		return vehicleLs;
	}
	@GetMapping("/rejectvehicle/{id}")
	public ResponseEntity<String> rejectVehicle(@PathVariable(value = "id") Long vehicleId)
			throws ResourceNotFoundException {
		Optional<Vehicle> vehicle= vehicleRepository.findById(vehicleId);
				if(vehicle.isPresent()) {
					String userName = CommonService.getUsername();
					String userRole = CommonService.getUserRole();
					if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
						Vehicle veh = vehicle.get();
						veh.setStatus(STATUS.ACTIVE.toString());
						vehicleRepository.save(veh);
					}
						
				return new ResponseEntity<>(HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Vehicle>> getAllVehicles() {
		return ResponseEntity.ok().body(vehicleRepository.findAll());
	}

	@GetMapping("/countInsuranceRecords")
	public ResponseEntity<Long> countInsuranceRecords() {
		List<Vehicle> insuranceExpList = vehicleRepository.insuranceExpiryRecordsCurrentMonth();
		Long insuranceExpListCount = 0l;
		if (CollectionUtils.isNotEmpty(insuranceExpList)) {
			insuranceExpListCount = Long.valueOf(insuranceExpList.size());
		}
		return ResponseEntity.ok().body(Long.valueOf(insuranceExpListCount));
		// return lsFleetLog;
	}

	@GetMapping("/insuranceExpiryRecords")
	public ResponseEntity<List<Vehicle>> insuranceExpiryRecords() {
		return ResponseEntity.ok().body(vehicleRepository.insuranceExpiryRecordsCurrentMonth());
	}

	@GetMapping("/permitExpiryRecords")
	public ResponseEntity<List<Vehicle>> permitExpiryRecords() {
		return ResponseEntity.ok().body(vehicleRepository.permitExpiryRecordsCurrentMonth());
	}

	@GetMapping("/fitnessExpiryRecords")
	public ResponseEntity<List<Vehicle>> fitnessExpiryRecords() {
		return ResponseEntity.ok().body(vehicleRepository.fitnessExpiryRecordsCurrentMonth());
	}

	@PostMapping("/search")
	public List<Vehicle> searchVehicle(@RequestBody Vehicle vehicle) {
		int pageNumber = 0;

		Long lng = null;// new Long(vehicle.getPageNum());
		pageNumber = lng.intValue();
		Pageable page = PageRequest.of(pageNumber, CommonConstants.pageSize, Sort.by("vehicleNo").ascending());
		Page<Vehicle> pageVehicle = vehicleRepository.findAll(new Specification<Vehicle>() {
			@Override
			public Predicate toPredicate(Root<Vehicle> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (vehicle.getInsuranceDate() != null) {
					predicates.add(criteriaBuilder
							.and(criteriaBuilder.equal(root.get("fitnessDate"), vehicle.getInsuranceDate())));
				}
				return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
		return pageVehicle.getContent();
	}

	@PostMapping("/pageCount")
	public ResponseEntity<Long> countVehiclePages(@RequestBody Vehicle vehicle) {
		Pageable page = PageRequest.of(0, CommonConstants.pageSize);
		Page<Vehicle> pageVehicle = vehicleRepository.findAll(new Specification<Vehicle>() {
			@Override
			public Predicate toPredicate(Root<Vehicle> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				/*
				 * if(vehicle.getInsuranceDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "insuranceDate"), truck.getInsuranceDate().plusDays(1)))); }
				 * if(vehicle.getPermitDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "permitDate"), truck.getPermitDate().plusDays(1)))); }
				 * if(vehicle.getFitnessDate()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(
				 * "fitnessDate"), truck.getFitnessDate().plusDays(1)))); }
				 * if(vehicle.getTruckNum()!=null){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("truckNum")
				 * , truck.getTruckNum()))); } if(!StringUtils.isEmpty(truck.getMisc())){
				 * predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("truckNum"),
				 * "%"+truck.getMisc()+"%"))); }
				 */

				return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, page);
		return ResponseEntity.ok().body(Long.valueOf(pageVehicle.getTotalPages()));
		// return lsFleetLog;
	}

	@GetMapping("/count/{company}")
	public ResponseEntity<Long> countVehicles(@PathVariable(value = "company") String company) {
		Company companyObj = companyRepostory.findCompanyByName(company);
		long totalPages = vehicleRepository.count(VehicleSpecs.getVehiclesByCompanySpec(companyObj,STATUS.ACTIVE.toString()));
		return ResponseEntity.ok().body(Long.valueOf(totalPages));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getVehicleById(@PathVariable(value = "id") Long vehicleId)
			throws ResourceNotFoundException {
		Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
		if (vehicle.isPresent()) {
			return ResponseEntity.ok(vehicle.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle Not Found");
	}

	@PostMapping("/create")
	public ResponseEntity<String> createVehicle(@RequestBody Vehicle vehicle) {
		try {
			vehicle.setCreatedDate(LocalDateTime.now());
			vehicle.setModifiedDate(LocalDateTime.now());
			vehicle.setStatus(STATUS.ACTIVE.toString());
			vehicleRepository.save(vehicle);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateTruck(@RequestBody Vehicle vehicle) throws ResourceNotFoundException {
		try {
			vehicle.setModifiedDate(LocalDateTime.now());
			vehicleRepository.save(vehicle);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findByVehicleName/{vehicleNum}")
	public ResponseEntity<Object> getVehicleByNum(@PathVariable(value = "vehicleNum") Long vehicleNum)
			throws ResourceNotFoundException {
		Vehicle vehicle = vehicleRepository.findByVehicleNo(vehicleNum);
		if (vehicle == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle Not Found");
		}
		return ResponseEntity.ok().body(vehicle);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteVehicle(@PathVariable(value = "id") Long vehicleId)
			throws ResourceNotFoundException {
		Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
		if (vehicle.isPresent()) {
			String userName = CommonService.getUsername();
			String userRole = CommonService.getUserRole();
			if(ROLES.ROLE_ADMIN.toString().equals(CommonService.getUserRole())) {
				vehicleRepository.delete(vehicle.get());
			}else {
				Vehicle veh = vehicle.get();
				veh.setStatus(STATUS.DELETE.toString());
				vehicleRepository.save(veh);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/vehiclesCvipAndPlateExpiry/{company}")
	public ResponseEntity<List<Vehicle>> getVehiclesCvipAndPlateExpiry(@PathVariable(value = "company") String company) {
		List<Reminders> reminderLs= remindersRepository.findAll();
		if(reminderLs.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Reminders reminder =reminderLs.stream().findFirst().get();
		Company companyObj = companyRepostory.findCompanyByName(company);
		LocalDate cvipEndDate =LocalDate.now().plusDays(reminder.getCvipDate());
		LocalDate plateNoEndtartDate=LocalDate.now().plusDays(reminder.getPlateNoExp());
		LocalDate vehicleInsuranceEndDate= LocalDate.now().plusDays(reminder.getVehicleInsuranceExpiry());
		LocalDate startDate= LocalDate.now();
		List<Vehicle> vehicleList = vehicleRepository.getStudentExpiryFieldsNotifications(companyObj.getId(), cvipEndDate, plateNoEndtartDate, vehicleInsuranceEndDate, startDate);
		return ResponseEntity.ok().body(vehicleList);
	}

	@GetMapping("/countVehiclesCvipAndPlateExpiry/{company}")
	public ResponseEntity<Integer> getCountVehiclesCvipAndPlateExpiry(@PathVariable(value = "company") String company) {
		List<Reminders> reminderLs= remindersRepository.findAll();
		if(reminderLs.isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		Reminders reminder =reminderLs.stream().findFirst().get();
		Company companyObj = companyRepostory.findCompanyByName(company);
		LocalDate cvipEndDate =LocalDate.now().plusDays(reminder.getCvipDate());
		LocalDate plateNoEndtartDate=LocalDate.now().plusDays(reminder.getPlateNoExp());
		LocalDate vehicleInsuranceEndDate= LocalDate.now().plusDays(reminder.getVehicleInsuranceExpiry());
		LocalDate startDate= LocalDate.now();
		List<Vehicle> vehicleList = vehicleRepository.getStudentExpiryFieldsNotifications(companyObj.getId(), cvipEndDate, plateNoEndtartDate, vehicleInsuranceEndDate, startDate);
		return ResponseEntity.ok().body(vehicleList!=null?vehicleList.size():0);
	}

}
