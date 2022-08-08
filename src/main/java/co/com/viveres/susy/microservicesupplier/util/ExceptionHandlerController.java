package co.com.viveres.susy.microservicesupplier.util;

import co.com.viveres.susy.microservicecommons.dto.NotificationDto;
import co.com.viveres.susy.microservicecommons.exception.BusinessException;
import co.com.viveres.susy.microservicecommons.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	private static final String SEVERITY_ERROR = "ERROR";

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<NotificationDto> handleAll(Exception e) {

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		NotificationDto notification = NotificationDto.builder()
				.uuid(UUID.randomUUID().toString())
				.timeStamp(LocalDateTime.now())
				.severity(SEVERITY_ERROR)
				.code(String.valueOf(httpStatus.value()))
				.message(e.getMessage())
				.build();

		return new ResponseEntity<>(
				notification,
				new HttpHeaders(),
				httpStatus);
	}

	@ExceptionHandler({ BusinessException.class })
	public ResponseEntity<Object> businessExceptionHandler(BusinessException e) {

		NotificationDto notification = NotificationDto.builder()
				.uuid(UUID.randomUUID().toString())
				.timeStamp(LocalDateTime.now())
				.severity(SEVERITY_ERROR)
				.code(String.valueOf(HttpStatus.CONFLICT.value()))
				.message(e.getMessage())
				.build();

		return ResponseEntity.badRequest().body(notification);
	}

	@ExceptionHandler({ NotFoundException.class })
	public ResponseEntity<Object> illegalArgumentExceptionHandler(NotFoundException e) {

		NotificationDto notification = NotificationDto.builder()
				.uuid(UUID.randomUUID().toString())
				.timeStamp(LocalDateTime.now())
				.severity(SEVERITY_ERROR)
				.code(String.valueOf(HttpStatus.CONFLICT.value()))
				.message(e.getMessage())
				.build();

		return ResponseEntity.badRequest().body(notification);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException e, HttpHeaders headers,
														 HttpStatus status, WebRequest request) {

		List<String> errors = new ArrayList<>();
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			errors.add(error.getField().concat(":").concat(error.getDefaultMessage()));
		}
		for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName().concat(":").concat(error.getDefaultMessage()));
		}

		NotificationDto notification = NotificationDto.builder()
				.uuid(UUID.randomUUID().toString())
				.timeStamp(LocalDateTime.now())
				.severity(SEVERITY_ERROR)
				.code(String.valueOf(status.value()))
				.message(e.getMessage())
				.metadata(errors)
				.build();

		return handleExceptionInternal(e, notification, headers,
				status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errors = new ArrayList<>();
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			errors.add(error.getField().concat(":").concat(error.getDefaultMessage()));
		}
		for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName().concat(":").concat(error.getDefaultMessage()));
		}

		NotificationDto notification = NotificationDto.builder()
				.uuid(UUID.randomUUID().toString())
				.timeStamp(LocalDateTime.now())
				.severity(SEVERITY_ERROR)
				.code(String.valueOf(status.value()))
				.message(e.getMessage())
				.metadata(errors)
				.build();

		return handleExceptionInternal(e, notification, headers,
				status, request);
	}
  
}
