package com.dev.talking.spring.exception;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.dev.talking.spring.exception.model.ApiError;
import com.dev.talking.spring.exception.model.ErrorResponse;
import com.dev.talking.spring.exception.model.FieldValidationError;
import com.dev.talking.spring.exception.model.GenericApiError;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
			MethodArgumentNotValidException exception,
			HttpServletRequest request
	) {
		List<ApiError> errors = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> (ApiError) new FieldValidationError(
						error.getCode(),
						error.getField(),
						error.getRejectedValue(),
						error.getDefaultMessage()
				))
				.toList();

		log.warn(
				"Validation failed for {} {} with {} field error(s): {}",
				request.getMethod(),
				request.getRequestURI(),
				errors.size(),
				errors
		);

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				ErrorCode.VALIDATION_ERROR,
				"Request validation failed",
				request.getRequestURI(),
				errors
		);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(
			ConstraintViolationException exception,
			HttpServletRequest request
	) {
		List<ApiError> errors = exception.getConstraintViolations()
				.stream()
				.map(violation -> (ApiError) new FieldValidationError(
						violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
						violation.getPropertyPath().toString(),
						violation.getInvalidValue(),
						violation.getMessage()
				))
				.toList();

		log.warn(
				"Constraint violation for {} {} with {} error(s): {}",
				request.getMethod(),
				request.getRequestURI(),
				errors.size(),
				errors
		);

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				ErrorCode.VALIDATION_ERROR,
				"Request validation failed",
				request.getRequestURI(),
				errors
		);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(
			ResourceNotFoundException exception,
			HttpServletRequest request
	) {
		ApiError error = new FieldValidationError(
				ErrorCode.RESOURCE_NOT_FOUND.name(),
				exception.getFieldName(),
				exception.getFieldValue(),
				exception.getMessage()
		);

		log.warn(
				"Resource not found for {} {}: resource={}, field={}, value={}",
				request.getMethod(),
				request.getRequestURI(),
				exception.getResourceName(),
				exception.getFieldName(),
				exception.getFieldValue()
		);

		return buildResponse(
				HttpStatus.NOT_FOUND,
				ErrorCode.RESOURCE_NOT_FOUND,
				exception.getMessage(),
				request.getRequestURI(),
				List.of(error)
		);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
			HttpMessageNotReadableException exception,
			HttpServletRequest request
	) {
		log.warn(
				"Malformed request body for {} {}: {}",
				request.getMethod(),
				request.getRequestURI(),
				exception.getMostSpecificCause().getMessage()
		);

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				ErrorCode.MALFORMED_REQUEST,
				"Request body is missing or malformed",
				request.getRequestURI(),
				List.of(new GenericApiError(ErrorCode.MALFORMED_REQUEST.name(), "Invalid JSON request body"))
		);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException exception,
			HttpServletRequest request
	) {
		String expectedType = exception.getRequiredType() == null
				? "valid type"
				: exception.getRequiredType().getSimpleName();

		ApiError error = new FieldValidationError(
				ErrorCode.TYPE_MISMATCH.name(),
				exception.getName(),
				exception.getValue(),
				"Parameter must be a " + expectedType
		);

		log.warn(
				"Type mismatch for {} {}: parameter={}, value={}, expectedType={}",
				request.getMethod(),
				request.getRequestURI(),
				exception.getName(),
				exception.getValue(),
				expectedType
		);

		return buildResponse(
				HttpStatus.BAD_REQUEST,
				ErrorCode.TYPE_MISMATCH,
				"Request parameter type mismatch",
				request.getRequestURI(),
				List.of(error)
		);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
			DataIntegrityViolationException exception,
			HttpServletRequest request
	) {
		log.warn(
				"Data integrity violation for {} {}: {}",
				request.getMethod(),
				request.getRequestURI(),
				exception.getMostSpecificCause().getMessage()
		);

		return buildResponse(
				HttpStatus.CONFLICT,
				ErrorCode.DATA_INTEGRITY_VIOLATION,
				"Request conflicts with existing data",
				request.getRequestURI(),
				List.of(new GenericApiError(
						ErrorCode.DATA_INTEGRITY_VIOLATION.name(),
						"Data integrity constraint violated"
				))
		);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFound(
			NoResourceFoundException exception,
			HttpServletRequest request
	) {
		log.warn(
				"Endpoint not found for {} {}. Spring detail: {}",
				request.getMethod(),
				request.getRequestURI(),
				exception.getMessage()
		);

		return buildResponse(
				HttpStatus.NOT_FOUND,
				ErrorCode.ENDPOINT_NOT_FOUND,
				"Endpoint not found",
				request.getRequestURI(),
				List.of(new GenericApiError(
						ErrorCode.ENDPOINT_NOT_FOUND.name(),
						"No API endpoint matches " + request.getMethod() + " " + request.getRequestURI()
				))
		);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(
			HttpRequestMethodNotSupportedException exception,
			HttpServletRequest request
	) {
		log.warn(
				"HTTP method not allowed for {} {}. Supported methods: {}",
				request.getMethod(),
				request.getRequestURI(),
				exception.getSupportedHttpMethods()
		);

		return buildResponse(
				HttpStatus.METHOD_NOT_ALLOWED,
				ErrorCode.METHOD_NOT_ALLOWED,
				"HTTP method not allowed",
				request.getRequestURI(),
				List.of(new GenericApiError(
						ErrorCode.METHOD_NOT_ALLOWED.name(),
						"Supported methods: " + exception.getSupportedHttpMethods()
				))
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(
			Exception exception,
			HttpServletRequest request
	) {
		log.error(
				"Unexpected error while handling {} {}",
				request.getMethod(),
				request.getRequestURI(),
				exception
		);

		return buildResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				ErrorCode.INTERNAL_SERVER_ERROR,
				"An unexpected error occurred",
				request.getRequestURI(),
				List.of(new GenericApiError(
						ErrorCode.INTERNAL_SERVER_ERROR.name(),
						"Please contact support if the problem persists"
				))
		);
	}

	private ResponseEntity<ErrorResponse> buildResponse(
			HttpStatus status,
			ErrorCode code,
			String message,
			String path,
			List<ApiError> errors
	) {
		ErrorResponse response = ErrorResponse.of(
				status.value(),
				status.getReasonPhrase(),
				code,
				message,
				path,
				errors
		);

		return ResponseEntity.status(status).body(response);
	}
}
