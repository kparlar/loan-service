package com.ing.hub.credit.loan.service;

import static com.ing.hub.credit.loan.constant.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ing.hub.credit.loan.component.CustomerComponent;
import com.ing.hub.credit.loan.component.InstallmentComponent;
import com.ing.hub.credit.loan.component.LoanComponent;
import com.ing.hub.credit.loan.component.ValidationComponent;
import com.ing.hub.credit.loan.dto.LoanDTO;
import com.ing.hub.credit.loan.dto.LoanRequestDTO;
import com.ing.hub.credit.loan.entity.CustomerEntity;
import com.ing.hub.credit.loan.entity.LoanEntity;
import com.ing.hub.credit.loan.repository.CustomerRepository;
import com.ing.hub.credit.loan.repository.InstallmentRepository;
import com.ing.hub.credit.loan.repository.LoanRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

  private CreditService creditService;

  @Mock
  ValidationComponent validationComponent;
  @Mock
  LoanComponent loanComponent;
  @Mock
  InstallmentComponent installmentComponent;
  @Mock
  CustomerComponent customerComponent;


  @Mock InstallmentRepository installmentRepository;

  @Mock LoanRepository loanRepository;

  @Mock CustomerRepository customerRepository;

  @BeforeEach
  void setup() {
    creditService = new CreditService(validationComponent, loanComponent, installmentComponent, customerComponent);
  }

  

  

  

  
  @Test
  public void create_givenValidCustomerWithValidCredit_whenCustomerCoversCreditConditions_thenSuccess() {
    var loanRequestDTO = LoanRequestDTO.builder().numberOfInstallment(6).interestRate(0.1).amount(100).build();
    double totalLoanAmount = loanRequestDTO.amount() * (1 + loanRequestDTO.interestRate());
    var customerEntity = CustomerEntity.builder()
            .id(VALID_CUSTOMER_ID).usedCreditLimit(0).creditLimit(10000).build();
    LoanDTO expectedResponse = LoanDTO.builder()
            .paid(false)
            .loanAmount(100)
            .numberOfInstallment(6)
            .customerId(VALID_CUSTOMER_ID)
            .build();
    LoanEntity loanEntity = LoanEntity.builder()
            .customerId(VALID_CUSTOMER_ID)
            .id(VALID_LOAN_ID)
            .paid(false)
            .numberOfInstallment(loanRequestDTO.numberOfInstallment())
            .loanAmount(loanRequestDTO.amount())
            .build();

    when(customerComponent.findById(VALID_CUSTOMER_ID)).thenReturn(customerEntity);
    when(loanComponent.create(ArgumentMatchers.any(LoanRequestDTO.class), ArgumentMatchers.any(CustomerEntity.class), ArgumentMatchers.any(OffsetDateTime.class))).thenReturn(loanEntity);
    doNothing().when(validationComponent).validateLoan(loanRequestDTO);
    doNothing().when(validationComponent).validateCredit(customerEntity.getCreditLimit(), customerEntity.getUsedCreditLimit(), totalLoanAmount);
    when(installmentComponent.create(
            ArgumentMatchers.any(LoanRequestDTO.class),
            ArgumentMatchers.anyDouble(),
            ArgumentMatchers.any(LoanEntity.class),
            ArgumentMatchers.any(OffsetDateTime.class)))
        .thenReturn(new ArrayList<>());

    doNothing().when(customerComponent).create(ArgumentMatchers.any(CustomerEntity.class));
    LoanDTO response = creditService.create(VALID_CUSTOMER_ID,
            loanRequestDTO);

    assertThat(response)
            .usingRecursiveComparison().ignoringFields("id", "createDate")
            .isEqualTo(expectedResponse);

  }



}
