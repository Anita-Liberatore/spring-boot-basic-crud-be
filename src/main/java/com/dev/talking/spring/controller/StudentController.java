package com.dev.talking.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.dev.talking.spring.entity.Student;
import com.dev.talking.spring.service.StudentService;

@RestController
@RequestMapping(path="api/v1/student")
public class StudentController {


	@Autowired
	private StudentService studentService;

	@GetMapping
	public ResponseEntity<?> getStudents() {	

		List<Student> studentList = studentService.getStudents();
		return new ResponseEntity<>(studentList, HttpStatus.OK);

	}

	// build create Student REST API
	@PostMapping()
	public ResponseEntity<Student> saveStudent(@RequestBody Student student){
		return new ResponseEntity<Student>(studentService.save(student), HttpStatus.CREATED);
	}


	@GetMapping("{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable("id") long studentId){
		return new ResponseEntity<Student>(studentService.getStudentById(studentId), HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable("id") long id){

		// delete employee from DB
		studentService.deleteStudent(id);

		return new ResponseEntity<String>("Student deleted successfully!.", HttpStatus.OK);
	}

	@PutMapping("{id}")
	public ResponseEntity<Student> update(@PathVariable("id") long id
			,@RequestBody Student student){
		return new ResponseEntity<Student>(studentService.updateStudent(student, id), HttpStatus.OK);
	}

	//	@GetMapping	
	//	public List<Student> getStudents() {
	//		return List.of(
	//				new Student(1L, 
	//						"Anita", 
	//						LocalDate.of(2000, Month.FEBRUARY, 15),
	//						25));
	//	}
}
