package com.ing.hub.credit.loan.service;

import com.ing.hub.credit.loan.component.CustomerComponent;
import com.ing.hub.credit.loan.component.InstallmentComponent;
import com.ing.hub.credit.loan.component.LoanComponent;
import com.ing.hub.credit.loan.component.ValidationComponent;
import com.ing.hub.credit.loan.dto.LoanDTO;
import com.ing.hub.credit.loan.dto.LoanRequestDTO;
import com.ing.hub.credit.loan.entity.CustomerEntity;
import com.ing.hub.credit.loan.entity.LoanEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final ValidationComponent validationComponent;
    private final LoanComponent loanComponent;
    private final InstallmentComponent  installmentComponent;
    private final CustomerComponent customerComponent;
    /*
        added transactional if something wrong dont want to put anything related with loan.
        UUID is used for id cause. with this id we make it unique without dependending anyplace. And also with this id we dont need to
        add this responsibility to DB cause in the future maybe we want to migrate this one to any other rdbms or nosql ones.
        And also with creating id advance we have the ability to run cumulative batch insertions. which gives us flexibility
     */
    @Transactional
    public LoanDTO create(String customerId, LoanRequestDTO loanRequestDTO) {
        validationComponent.validateLoan(loanRequestDTO);
        double totalLoanAmount = loanRequestDTO.amount() * (1 + loanRequestDTO.interestRate());
        CustomerEntity customerEntity = customerComponent.findById(customerId);
        validationComponent.validateCredit(customerEntity.getCreditLimit(), customerEntity.getUsedCreditLimit(), totalLoanAmount);
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        LoanEntity loanEntity = loanComponent.create(loanRequestDTO, customerEntity, offsetDateTime);
        installmentComponent.create(loanRequestDTO, totalLoanAmount, loanEntity, offsetDateTime);
        customerEntity = customerEntity.toBuilder().usedCreditLimit(customerEntity.getUsedCreditLimit() + totalLoanAmount).build();
        customerComponent.create(customerEntity);
        return loanEntity.toDTO();
    }


}
