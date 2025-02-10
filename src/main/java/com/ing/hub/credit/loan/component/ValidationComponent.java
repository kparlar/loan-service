package com.ing.hub.credit.loan.component;

import static com.ing.hub.credit.loan.util.constants.ErrorMessage.*;
import static com.ing.hub.credit.loan.util.constants.ErrorMessage.BUSINESS_ERROR__NOT_ENOUGH_CREDIT;

import com.ing.hub.credit.loan.dto.LoanRequestDTO;
import com.ing.hub.credit.loan.entity.LoanEntity;
import com.ing.hub.credit.loan.exception.BusinessException;
import com.ing.hub.credit.loan.exception.EntityNotFoundException;
import com.ing.hub.credit.loan.exception.ValidationException;
import com.ing.hub.credit.loan.util.enums.InstallmentPeriod;
import com.ing.hub.credit.loan.util.enums.InterestRate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationComponent {
    private static final List<Integer> installmentPeriods = Arrays.stream(InstallmentPeriod.values()).map(InstallmentPeriod::getValue).toList();

    public void validateLoan(LoanRequestDTO loanRequestDTO) {
        if (!installmentPeriods.contains(loanRequestDTO.numberOfInstallment())) {
            throw new ValidationException(INVALID__NUMBER_OF_INSTALLMENTS);
        }
        if (loanRequestDTO.interestRate() < InterestRate.MIN.getValue() || loanRequestDTO.interestRate() > InterestRate.MAX.getValue()) {
            throw new ValidationException(INVALID__INTEREST_RATE);
        }
    }
    public void validateCredit(double creditLimit, double usedCreditLimit, double totalLoanAmount) {
        if (creditLimit - usedCreditLimit < totalLoanAmount) {
            throw new BusinessException(BUSINESS_ERROR__NOT_ENOUGH_CREDIT);
        }
    }
    public void validateLoan(Optional<LoanEntity> loanOptional) {
        if (loanOptional.isEmpty()) {
            throw new EntityNotFoundException("Loan not found.");
        }
        if (loanOptional.get().isPaid()) {
            throw new BusinessException("Loan has already been paid.");
        }

    }

}
