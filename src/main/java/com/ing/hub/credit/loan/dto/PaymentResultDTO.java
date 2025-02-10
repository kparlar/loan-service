package com.ing.hub.credit.loan.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record PaymentResultDTO(int installmentsPaid,double totalPaidAmount,boolean paid) {
}
