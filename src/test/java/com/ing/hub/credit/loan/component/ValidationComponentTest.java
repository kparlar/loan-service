package com.ing.hub.credit.loan.component;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ing.hub.credit.loan.dto.LoanRequestDTO;
import com.ing.hub.credit.loan.exception.BusinessException;
import com.ing.hub.credit.loan.exception.ValidationException;
import com.ing.hub.credit.loan.util.constants.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ValidationComponentTest {

    private ValidationComponent validationComponent;

    @BeforeEach
    void setup() {
        validationComponent = new ValidationComponent();
    }

    @Test
    public void validateLoan_givenInvalidInstallmentNumber_whenInstallmentNumberHasToBeValid_thenThrowValidationException() {
        assertThrows(
                ValidationException.class,
                () -> validationComponent.validateLoan(LoanRequestDTO.builder().numberOfInstallment(3).build()),
                ErrorMessage.INVALID__NUMBER_OF_INSTALLMENTS);
    }

    @Test
    public void validateLoan_givenInvalidInterestRate_whenInterestRateBetweenOneAndFivePercentage_thenThrowValidationException() {
        assertThrows(
                ValidationException.class,
                () ->
                        validationComponent.validateLoan(
                                LoanRequestDTO.builder().numberOfInstallment(6).interestRate(0.01).build()),
                ErrorMessage.INVALID__INTEREST_RATE);
    }


    @Test
    public void validateCredit_givenValidCredit_whenCustomerCreditNotEnough_thenThrowBusinessException() {        
        assertThrows(
                BusinessException.class,
                () ->
                        validationComponent.validateCredit(
                                100, 100, 200),
                ErrorMessage.BUSINESS_ERROR__NOT_ENOUGH_CREDIT);
    }

}
