package com.acuario.hotel.exception;

import java.util.List;

public class ValidationException extends RuntimeException {
	private final List<String> validationErrors;

	public ValidationException(String message) {
		super(message);
		this.validationErrors = null;
	}

	public ValidationException(String message, List<String> validationErrors) {
		super(message);
		this.validationErrors = validationErrors;
	}

	public List<String> getValidationErrors() {
		return validationErrors;
	}
}

