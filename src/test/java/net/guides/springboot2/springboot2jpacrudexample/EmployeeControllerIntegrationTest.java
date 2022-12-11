package net.guides.springboot2.springboot2jpacrudexample;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.cameron.driver.education.Application;

@RunWith(SpringRunner.class)
@Ignore
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGetAllEmployees() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees",
				HttpMethod.GET, entity, String.class);
		
		assertNotNull(response.getBody());
	}

//	@Test
//	public void testGetEmployeeById() {
//		Users employee = restTemplate.getForObject(getRootUrl() + "/employees/1", Users.class);
//		System.out.println(employee.getFirstName());
//		assertNotNull(employee);
//	}
//
//	@Test
//	public void testCreateEmployee() {
//		Users employee = new Users();
//		employee.setEmailId("admin@gmail.com");
//		employee.setFirstName("admin");
//		employee.setLastName("admin");
//
//		ResponseEntity<Users> postResponse = restTemplate.postForEntity(getRootUrl() + "/employees", employee, Users.class);
//		assertNotNull(postResponse);
//		assertNotNull(postResponse.getBody());
//	}
//
//	@Test
//	public void testUpdateEmployee() {
//		int id = 1;
//		Users employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Users.class);
//		employee.setFirstName("admin1");
//		employee.setLastName("admin2");
//
//		restTemplate.put(getRootUrl() + "/employees/" + id, employee);
//
//		Users updatedEmployee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Users.class);
//		assertNotNull(updatedEmployee);
//	}
//
//	@Test
//	public void testDeleteEmployee() {
//		int id = 2;
//		Users employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Users.class);
//		assertNotNull(employee);
//
//		restTemplate.delete(getRootUrl() + "/employees/" + id);
//
//		try {
//			employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Users.class);
//		} catch (final HttpClientErrorException e) {
//			assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
//		}
//	}
}
