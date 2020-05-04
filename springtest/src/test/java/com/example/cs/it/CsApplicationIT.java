package com.example.cs.it;

import com.example.cs.CsApplication;
import com.example.cs.Employee;
import com.example.cs.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CsApplicationIT {

	@Test
	public void contextLoads() {
		System.out.println("start success");
	}

	@Autowired
	private MockMvc mvc;

	@Autowired
	private EmployeeRepository repository;

	@Test
	public void givenEmployees_whenGetEmployees_thenStatus200()
			throws Exception {

		Employee bob = new Employee();
		bob.setName("bob");
		repository.save(bob);

		mvc.perform(MockMvcRequestBuilders.get("/employees")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name", is("bob")));
	}
}
