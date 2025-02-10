package com.ing.hub.credit.loan.entity;

import com.ing.hub.credit.loan.dto.LoanDTO;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanEntity {

    private String id;
    private String customerId;
    private double loanAmount;
    private int numberOfInstallment;
    private OffsetDateTime createDate;
    private boolean paid;

    public LoanDTO toDTO() {
        return LoanDTO.builder()
                .id(this.id)
                .customerId(this.customerId)
                .loanAmount(this.loanAmount).
                numberOfInstallment(this.numberOfInstallment)
                .createDate(this.createDate)
                .paid(this.paid)
                .build();
    }
}
