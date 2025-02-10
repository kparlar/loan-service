package com.ing.hub.credit.loan.entity;

import java.time.OffsetDateTime;

import com.ing.hub.credit.loan.dto.InstallmentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InstallmentEntity {

    private String id;
    private String loanId;
    private double amount;
    private double paidAmount;
    private OffsetDateTime dueDate;
    private OffsetDateTime paymentDate;
    private boolean paid;

    public InstallmentDTO toDTO(){
        return InstallmentDTO.builder()
                .id(this.id)
                .loanId(this.loanId)
                .amount(this.amount)
                .paidAmount(this.paidAmount)
                .dueDate(this.dueDate)
                .paymentDate(this.paymentDate)
                .paid(this.paid)
                .build();
    }
}
