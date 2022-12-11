package com.dev.talking.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.dev.talking.spring.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
