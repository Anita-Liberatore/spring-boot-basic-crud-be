package com.dev.talking.spring.exception.model;

public sealed interface ApiError permits FieldValidationError, GenericApiError {

	String code();

	String message();
}
