package com.dev.talking.spring.service;

import java.util.List;

import com.dev.talking.spring.model.StudentDto;
import com.dev.talking.spring.model.StudentStatus;
import com.dev.talking.spring.model.request.CreateStudentRequest;
import com.dev.talking.spring.model.request.UpdateStudentRequest;

public interface StudentService {

	List<StudentDto> getStudents(StudentStatus status);

	StudentDto getStudentById(Long id);

	StudentDto createStudent(CreateStudentRequest request);

	StudentDto updateStudent(Long id, UpdateStudentRequest request);

	StudentDto deactivateStudent(Long id);
}
