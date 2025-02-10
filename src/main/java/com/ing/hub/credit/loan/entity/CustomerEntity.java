package com.ing.hub.credit.loan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomerEntity {

    private String id;
    private String username;
    private String name;
    private String surname;
    private double creditLimit;
    private double usedCreditLimit;



}
