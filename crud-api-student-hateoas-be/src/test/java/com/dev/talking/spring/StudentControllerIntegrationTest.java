package com.dev.talking.spring;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.dev.talking.spring.repository.StudentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {

	private static final String STUDENTS_API = "/api/v1/students";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StudentRepository studentRepository;

	@BeforeEach
	void setUp() {
		studentRepository.deleteAll();
	}

	@Test
	void shouldManageStudentLifecycleWithHateoasLinks() throws Exception {
		MvcResult createResult = mockMvc.perform(post(STUDENTS_API)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Anita",
								  "surname": "Liberatore",
								  "age": 26,
								  "email": "anita@example.com"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", endsWith("/api/v1/students/1")))
				.andExpect(jsonPath("$.name").value("Anita"))
				.andExpect(jsonPath("$.status").value("ACTIVE"))
				.andExpect(jsonPath("$._links.self.href", endsWith("/api/v1/students/1")))
				.andExpect(jsonPath("$._links.deactivate.href", endsWith("/api/v1/students/1")))
				.andReturn();

		Long studentId = extractStudentId(createResult);

		mockMvc.perform(get(STUDENTS_API))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.students", hasSize(1)))
				.andExpect(jsonPath("$._links.all-students.href", endsWith("/api/v1/students")))
				.andExpect(jsonPath("$._links.all-students.templated").doesNotExist())
				.andExpect(jsonPath("$._links.filter-by-status.href", endsWith("/api/v1/students{?status}")))
				.andExpect(jsonPath("$._links.filter-by-status.templated").value(true))
				.andExpect(jsonPath("$._links.active-students.href", endsWith("/api/v1/students?status=ACTIVE")));

		mockMvc.perform(put(STUDENTS_API + "/{id}", studentId)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Anita",
								  "surname": "Liberatore",
								  "age": 27,
								  "email": "anita.liberatore@example.com",
								  "status": "ACTIVE"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.age").value(27))
				.andExpect(jsonPath("$.email").value("anita.liberatore@example.com"));

		mockMvc.perform(delete(STUDENTS_API + "/{id}", studentId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("INACTIVE"))
				.andExpect(jsonPath("$._links.deactivate").doesNotExist());

		mockMvc.perform(get(STUDENTS_API).param("status", "INACTIVE"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.students", hasSize(1)))
				.andExpect(jsonPath("$._embedded.students[0].status").value("INACTIVE"));
	}

	private Long extractStudentId(MvcResult result) throws Exception {
		JsonNode responseBody = objectMapper.readTree(result.getResponse().getContentAsString());
		return responseBody.get("id").asLong();
	}
}
