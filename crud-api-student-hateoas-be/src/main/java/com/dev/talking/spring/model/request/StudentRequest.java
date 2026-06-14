package com.dev.talking.spring.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class StudentRequest {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Surname is required")
	private String surname;

	@NotNull(message = "Age is required")
	@Min(value = 1, message = "Age must be greater than 0")
	@Max(value = 120, message = "Age must be less than or equal to 120")
	private Integer age;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	private String email;
}
