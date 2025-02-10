package com.ing.hub.credit.loan.dto;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder(toBuilder = true)
public record InstallmentDTO(String id, String loanId, double amount, double paidAmount, OffsetDateTime dueDate, OffsetDateTime paymentDate, boolean paid) {
}
