package com.dev.talking.spring.model;

import org.springframework.hateoas.server.core.Relation;

import lombok.Data;

@Data
@Relation(itemRelation = "student", collectionRelation = "students")
public class StudentDto {

	private Long id;
	private String name;
	private String surname;
	private Integer age;
	private String email;
	private StudentStatus status;
}
