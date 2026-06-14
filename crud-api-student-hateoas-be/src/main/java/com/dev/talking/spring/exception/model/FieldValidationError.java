package com.dev.talking.spring.exception.model;

public record FieldValidationError(
		String code,
		String field,
		Object rejectedValue,
		String message
) implements ApiError {
}
