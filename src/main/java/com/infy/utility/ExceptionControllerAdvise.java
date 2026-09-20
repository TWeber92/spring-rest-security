package com.infy.utility;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.infy.exception.InfyBankException;

@RestControllerAdvice
public class ExceptionControllerAdvise {

	private static final Log LOGGER = LogFactory.getLog(ExceptionControllerAdvise.class);

	@Autowired
	Environment environment;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> exceptionHandler(Exception exception) {
		ErrorInfo errInfo = new ErrorInfo();
		errInfo.setErrorMessage(environment.getProperty("General.EXCEPTION_MESSAGE"));
		errInfo.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errInfo.setTimeStamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(errInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class,
			InfyBankException.class })
	public ResponseEntity<ErrorInfo> constraintAndManvExceptionHandler(Exception exception) {
		LOGGER.error(exception.getMessage(), exception);
		String errMsg;
		if (exception instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException manve = (MethodArgumentNotValidException) exception;
			errMsg = manve.getBindingResult().getAllErrors().stream()
					.map(err -> err.getDefaultMessage()).collect(Collectors.joining(", "));
		} else if (exception instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) exception;
			errMsg = cve.getConstraintViolations().stream()
					.map(err -> err.getMessage()).collect(Collectors.joining(", "));
		} else {
			InfyBankException ibe = (InfyBankException) exception;
			errMsg = environment.getProperty(ibe.getMessage());
		}
		ErrorInfo errInfo = new ErrorInfo();
		errInfo.setErrorMessage(errMsg);
		errInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errInfo.setTimeStamp(LocalDateTime.now());
		return new ResponseEntity<ErrorInfo>(errInfo, HttpStatus.BAD_REQUEST);
	}
}
