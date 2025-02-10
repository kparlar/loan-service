package com.ing.hub.credit.loan.config;

import com.ing.hub.credit.loan.exception.BusinessException;
import com.ing.hub.credit.loan.exception.EntityNotFoundException;
import com.ing.hub.credit.loan.exception.UnAuthorizedException;
import com.ing.hub.credit.loan.exception.ValidationException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Hidden
@RestControllerAdvice(basePackages = "com.ing.hub.credit.loan.controller")
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
    return new ResponseEntity<>(message, status);
  }
  ;

  @ExceptionHandler(UnAuthorizedException.class)
  protected ResponseEntity<Object> handleUnAuthorizedException(UnAuthorizedException ex) {
    log.error(ex.getMessage());
    return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
    log.error(ex.getMessage());
    return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
  }

  /**
   * Client Error Message HTTP Status Code: 412 : PRECONDITION_FAILED
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(ValidationException.class)
  protected ResponseEntity<Object> handleValidationException(ValidationException ex) {
    log.error(ex.getMessage());
    return buildResponseEntity(HttpStatus.PRECONDITION_FAILED, ex.getLocalizedMessage());
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<Object> handleBusinessException(BusinessException ex) {
    log.error(ex.getMessage());
    return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  protected ResponseEntity<Object> handleUnsupportedOperationException(
      UnsupportedOperationException ex) {
    log.error(ex.getMessage());
    return buildResponseEntity(HttpStatus.NOT_ACCEPTABLE, ex.getLocalizedMessage());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleException(Exception ex) {
    log.error(ex.getMessage());
    return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
  }
}
