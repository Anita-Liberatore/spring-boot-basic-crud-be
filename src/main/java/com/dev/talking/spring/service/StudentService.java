package com.dev.talking.spring.service;

import java.util.List;

import com.dev.talking.spring.entity.Student;

public interface StudentService {

	public List<Student> getStudents();

	public Student save(Student student);

	public void deleteStudent(Long id);
	
	public Student getStudentById(Long id);
	
	public Student updateStudent(Student student, Long id);
}
