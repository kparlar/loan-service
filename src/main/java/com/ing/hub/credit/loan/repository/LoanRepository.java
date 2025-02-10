package com.ing.hub.credit.loan.repository;

import com.ing.hub.credit.loan.entity.LoanEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LoanRepository {
    void upsert(LoanEntity loan);
    List<LoanEntity> findByCustomerId(String customerId);
    Optional<LoanEntity> findById(String id);
}
