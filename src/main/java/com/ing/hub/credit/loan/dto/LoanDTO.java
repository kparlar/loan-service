package com.ing.hub.credit.loan.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Date;

@Builder(toBuilder = true)
public record LoanDTO(String id, String customerId, double loanAmount, int numberOfInstallment, OffsetDateTime createDate, boolean paid) {}
