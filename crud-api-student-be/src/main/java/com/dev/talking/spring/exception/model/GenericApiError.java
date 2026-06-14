package com.dev.talking.spring.exception.model;

public record GenericApiError(
		String code,
		String message
) implements ApiError {
}
