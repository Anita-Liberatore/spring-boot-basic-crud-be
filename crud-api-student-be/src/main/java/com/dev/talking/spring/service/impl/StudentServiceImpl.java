package com.dev.talking.spring.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.talking.spring.entity.Student;
import com.dev.talking.spring.exception.ResourceNotFoundException;
import com.dev.talking.spring.mapper.StudentMapper;
import com.dev.talking.spring.model.StudentDto;
import com.dev.talking.spring.model.StudentStatus;
import com.dev.talking.spring.model.request.CreateStudentRequest;
import com.dev.talking.spring.model.request.UpdateStudentRequest;
import com.dev.talking.spring.repository.StudentRepository;
import com.dev.talking.spring.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	private static final String STUDENT_RESOURCE = "Student";

	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;

	public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
		this.studentRepository = studentRepository;
		this.studentMapper = studentMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudentDto> getStudents(StudentStatus status) {
		List<Student> students = status == null
				? studentRepository.findAll()
				: studentRepository.findAllByStatus(status);

		return students.stream()
				.map(studentMapper::toDto)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public StudentDto getStudentById(Long id) {
		return studentMapper.toDto(findStudentById(id));
	}

	@Override
	@Transactional
	public StudentDto createStudent(CreateStudentRequest request) {
		Student student = studentMapper.toEntity(request);
		student.setStatus(StudentStatus.ACTIVE);

		return saveAndMap(student);
	}

	@Override
	@Transactional
	public StudentDto updateStudent(Long id, UpdateStudentRequest request) {
		Student student = findStudentById(id);
		studentMapper.updateEntity(request, student);

		return saveAndMap(student);
	}

	@Override
	@Transactional
	public StudentDto deactivateStudent(Long id) {
		Student student = findStudentById(id);
		student.setStatus(StudentStatus.INACTIVE);

		return saveAndMap(student);
	}

	private Student findStudentById(Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(STUDENT_RESOURCE, "id", id));
	}

	private StudentDto saveAndMap(Student student) {
		Student savedStudent = studentRepository.save(student);
		return studentMapper.toDto(savedStudent);
	}
}
