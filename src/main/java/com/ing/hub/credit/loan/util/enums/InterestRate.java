package com.ing.hub.credit.loan.util.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum InterestRate {

    MIN(0.1),
    MAX(0.5);

    private final double value;
    InterestRate(double value) {
        this.value = value;
    }

    public static Optional<InterestRate> findByValue(int value) {
        var interestRateOptional = Arrays.stream(values()).filter(e -> e.value == value).findFirst();
        return interestRateOptional.or(() -> Optional.of(MIN));
    }


}
