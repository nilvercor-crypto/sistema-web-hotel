package com.acuario.hotel.config;

import com.acuario.hotel.exception.BusinessException;
import com.acuario.hotel.exception.ErrorCode;
import com.acuario.hotel.exception.ResourceNotFoundException;
import com.acuario.hotel.exception.ValidationException;
import com.acuario.hotel.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
		ApiResponse<Object> response = ApiResponse.error(
				ex.getMessage(),
				ex.getMessage(),
				ErrorCode.RESOURCE_NOT_FOUND.getCode()
		);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
		String errorCode = ex.getErrorCode() != null ? ex.getErrorCode() : ErrorCode.BUSINESS_RULE_VIOLATION.getCode();
		ApiResponse<Object> response = ApiResponse.error(
				ex.getMessage(),
				ex.getMessage(),
				errorCode
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiResponse<Object>> handleValidationException(ValidationException ex) {
		ApiResponse<Object> response;
		if (ex.getValidationErrors() != null && !ex.getValidationErrors().isEmpty()) {
			String errorDetails = String.join("; ", ex.getValidationErrors());
			response = ApiResponse.error(
					ex.getMessage(),
					errorDetails,
					ErrorCode.VALIDATION_ERROR.getCode()
			);
		} else {
			response = ApiResponse.error(
					ex.getMessage(),
					ex.getMessage(),
					ErrorCode.VALIDATION_ERROR.getCode()
			);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.toList());

		String errorDetails = String.join("; ", errors);
		ApiResponse<Object> response = ApiResponse.error(
				"Error de validación",
				errorDetails,
				ErrorCode.VALIDATION_ERROR.getCode()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler({ AuthenticationException.class, BadCredentialsException.class })
	public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
		ApiResponse<Object> response = ApiResponse.error(
				"Credenciales inválidas",
				"El nombre de usuario o contraseña son incorrectos",
				ErrorCode.BUSINESS_RULE_VIOLATION.getCode()
		);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
		ApiResponse<Object> response = ApiResponse.error(
				ex.getMessage(),
				ex.getMessage(),
				ErrorCode.BAD_REQUEST.getCode()
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
		ApiResponse<Object> response = ApiResponse.error(
				"Error interno del servidor",
				ex.getMessage() != null ? ex.getMessage() : "Error desconocido",
				ErrorCode.INTERNAL_SERVER_ERROR.getCode()
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
