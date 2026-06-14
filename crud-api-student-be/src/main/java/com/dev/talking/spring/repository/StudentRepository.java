package com.dev.talking.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.talking.spring.entity.Student;
import com.dev.talking.spring.model.StudentStatus;

public interface StudentRepository extends JpaRepository<Student, Long> {

	List<Student> findAllByStatus(StudentStatus status);
}
