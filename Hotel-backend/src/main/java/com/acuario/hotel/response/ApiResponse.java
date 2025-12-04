package com.acuario.hotel.response;

import java.time.LocalDateTime;

public class ApiResponse<T> {
	private boolean success;
	private String message;
	private T data;
	private String error;
	private String errorCode;
	private LocalDateTime timestamp;

	private ApiResponse() {
		this.timestamp = LocalDateTime.now();
	}

	private ApiResponse(boolean success, String message, T data) {
		this();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	private ApiResponse(boolean success, String message, String error, String errorCode) {
		this();
		this.success = success;
		this.message = message;
		this.error = error;
		this.errorCode = errorCode;
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, "Operación exitosa", data);
	}

	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}

	public static <T> ApiResponse<T> success(String message) {
		return new ApiResponse<>(true, message, null);
	}

	public static <T> ApiResponse<T> error(String message, String error) {
		return new ApiResponse<>(false, message, error, null);
	}

	public static <T> ApiResponse<T> error(String message, String error, String errorCode) {
		return new ApiResponse<>(false, message, error, errorCode);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}

