package com.dev.talking.spring.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.talking.spring.model.StudentDto;
import com.dev.talking.spring.model.StudentStatus;
import com.dev.talking.spring.model.assembler.StudentModelAssembler;
import com.dev.talking.spring.model.request.CreateStudentRequest;
import com.dev.talking.spring.model.request.UpdateStudentRequest;
import com.dev.talking.spring.service.StudentService;

@RestController
@RequestMapping({"/api/v1/students", "/api/v1/student"})
public class StudentController {

	private final StudentService studentService;
	private final StudentModelAssembler studentModelAssembler;

	public StudentController(StudentService studentService, StudentModelAssembler studentModelAssembler) {
		this.studentService = studentService;
		this.studentModelAssembler = studentModelAssembler;
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<StudentDto>>> getStudents(
			@RequestParam(required = false) StudentStatus status
	) {
		List<EntityModel<StudentDto>> students = studentService.getStudents(status)
				.stream()
				.map(studentModelAssembler::toModel)
				.toList();

		CollectionModel<EntityModel<StudentDto>> response = CollectionModel.of(
				students,
				linkTo(methodOn(StudentController.class).getStudents(status)).withSelfRel(),
				linkTo(StudentController.class).withRel("all-students"),
				Link.of(linkTo(StudentController.class).toUri().toString() + "{?status}", "filter-by-status").withType("GET"),
				linkTo(methodOn(StudentController.class).getStudents(StudentStatus.ACTIVE)).withRel("active-students"),
				linkTo(methodOn(StudentController.class).getStudents(StudentStatus.INACTIVE)).withRel("inactive-students")
		);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<StudentDto>> getStudentById(@PathVariable Long id) {
		StudentDto student = studentService.getStudentById(id);
		return ok(student);
	}

	@PostMapping
	public ResponseEntity<EntityModel<StudentDto>> createStudent(@Valid @RequestBody CreateStudentRequest request) {
		StudentDto student = studentService.createStudent(request);
		EntityModel<StudentDto> response = studentModelAssembler.toModel(student);
		URI location = response.getRequiredLink(IanaLinkRelations.SELF).toUri();

		return ResponseEntity.created(location).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<StudentDto>> updateStudent(
			@PathVariable Long id,
			@Valid @RequestBody UpdateStudentRequest request
	) {
		StudentDto student = studentService.updateStudent(id, request);
		return ok(student);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<EntityModel<StudentDto>> deactivateStudent(@PathVariable Long id) {
		StudentDto student = studentService.deactivateStudent(id);
		return ok(student);
	}

	private ResponseEntity<EntityModel<StudentDto>> ok(StudentDto student) {
		return ResponseEntity.ok(studentModelAssembler.toModel(student));
	}
}
