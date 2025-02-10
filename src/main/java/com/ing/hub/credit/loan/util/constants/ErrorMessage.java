package com.ing.hub.credit.loan.util.constants;

public interface ErrorMessage {

    String BUSINESS_ERROR__NOT_ENOUGH_CREDIT = "Customer does not have enough credit limit.";
    String INVALID__INTEREST_RATE = "Interest rate must be between 0.1 and 0.5.";
    String INVALID__NUMBER_OF_INSTALLMENTS = "Invalid number of installments. Valid values are: 6, 9, 12, 24.";
    String NOT_FOUND__CUSTOMER = "Customer not found";
    String UNAUTHORIZED = "You are not authorized to perform this action";


}
