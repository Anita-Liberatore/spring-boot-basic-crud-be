package com.dev.talking.spring.entity;

import com.dev.talking.spring.model.StudentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String surname;
	private Integer age;
	private String email;

	@Enumerated(EnumType.STRING)
	private StudentStatus status;

	@PrePersist
	void assignDefaultStatus() {
		if (status == null) {
			status = StudentStatus.ACTIVE;
		}
	}
}
