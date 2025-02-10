package com.ing.hub.credit.loan.exception;

public class UnAuthorizedException extends LoanServiceException {
  public UnAuthorizedException(String message) {
    super(message);
  }
}
