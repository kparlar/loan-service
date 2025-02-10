package com.ing.hub.credit.loan.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record LoanRequestDTO(double amount, double interestRate, int numberOfInstallment) {}
