package com.ing.hub.credit.loan.repository;

import com.ing.hub.credit.loan.entity.CustomerEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface CustomerRepository {
    void upsert(CustomerEntity customerEntity);
    Optional<CustomerEntity> findById(String id);
    Optional<CustomerEntity> findByLoanId(String loanId);
}
