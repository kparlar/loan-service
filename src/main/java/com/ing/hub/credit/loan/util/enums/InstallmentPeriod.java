package com.ing.hub.credit.loan.util.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum InstallmentPeriod {

    SIX(6),
    NINE(9),
    TWELVE(12),
    TWENTY_FOUR(24);

    private final int value;

    InstallmentPeriod(int value) {
        this.value = value;
    }

    public static Optional<InstallmentPeriod> findByValue(int value) {
        var installmentPeriodOptional = Arrays.stream(values()).filter(e -> e.value == value).findFirst();
        return installmentPeriodOptional.or(() -> Optional.of(SIX));
    }




}
