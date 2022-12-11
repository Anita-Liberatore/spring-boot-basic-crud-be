package com.dev.talking.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.talking.spring.entity.Student;
import com.dev.talking.spring.exception.ResourceNotFoundException;
import com.dev.talking.spring.repository.StudentRepository;
import com.dev.talking.spring.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepo;

	@Override
	public List<Student> getStudents() {
		return studentRepo.findAll();
	}

	@Override
	public Student save(Student student) {
		return studentRepo.save(student);
	}

	@Override
	public Student getStudentById(Long id) {
		return studentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student", "Id", id));
	}
	
	@Override
	public Student updateStudent(Student student, Long id) {
		
		Student existingStudent = studentRepo.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Student", "Id", id)); 
		
		existingStudent.setName(student.getName());
		existingStudent.setSurname(student.getSurname());
		existingStudent.setEmail(student.getEmail());
		existingStudent.setAge(student.getAge());

		studentRepo.save(existingStudent);
		return existingStudent;
	}
	
	@Override
	public void deleteStudent(Long id) {
		
		// check whether a employee exist in a DB or not
		studentRepo.findById(id).orElseThrow(() -> 
								new ResourceNotFoundException("Student", "Id", id));
		studentRepo.deleteById(id);
	}
}