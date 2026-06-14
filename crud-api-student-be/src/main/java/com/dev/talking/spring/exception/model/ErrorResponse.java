package com.dev.talking.spring.exception.model;

import java.time.LocalDateTime;
import java.util.List;

import com.dev.talking.spring.exception.ErrorCode;

public record ErrorResponse(
		LocalDateTime timestamp,
		int status,
		String error,
		ErrorCode code,
		String message,
		String path,
		List<ApiError> errors
) {
	public static ErrorResponse of(
			int status,
			String error,
			ErrorCode code,
			String message,
			String path,
			List<ApiError> errors
	) {
		return new ErrorResponse(LocalDateTime.now(), status, error, code, message, path, errors);
	}
}
