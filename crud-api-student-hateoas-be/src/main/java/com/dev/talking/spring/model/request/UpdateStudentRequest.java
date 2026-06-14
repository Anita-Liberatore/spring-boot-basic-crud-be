package com.dev.talking.spring.model.request;

import com.dev.talking.spring.model.StudentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentRequest extends StudentRequest {

	@NotNull(message = "Status is required")
	private StudentStatus status;
}
